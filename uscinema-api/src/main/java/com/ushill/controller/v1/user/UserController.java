package com.ushill.controller.v1.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ushill.DTO.UserStatDTO;
import com.ushill.VO.user.UserVO;
import com.ushill.exception.http.ParameterException;
import com.ushill.interceptor.LocalUser;
import com.ushill.interceptor.ScopeLevel;
import com.ushill.models.User;
import com.ushill.msg.producer.RabbitProducer;
import com.ushill.service.interfaces.UserService;
import com.ushill.utils.stat.CachedStatMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
@Validated
public class UserController {

    private final UserService userService;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final CachedStatMap cachedStatMap;
    private final RabbitProducer changeCriticMsgRabbitProducer;

    @Value("${uscinema.cached-key.v1.user-stat}")
    private String userStatKey;
    @Value("${uscinema.cached-key.v1.user.info}")
    private String userInfoKey;

    @Value("${uscinema.limit.filesize.image}")
    private int maxImageSize;

    @Value("${uscinema.message.change-critic.ex}")
    private String exchange;

    @Value("${uscinema.message.change-critic.rk}")
    private String routingKey;

    @Autowired
    UserController(@Qualifier("userServiceImpl") UserService userService,
                   StringRedisTemplate redisTemplate, ObjectMapper objectMapper, CachedStatMap cachedStatMap, RabbitProducer movieUpdateMsgRabbitProducer){
        this.userService = userService;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cachedStatMap = cachedStatMap;
        this.changeCriticMsgRabbitProducer = movieUpdateMsgRabbitProducer;
    }

    @GetMapping("/{userId}")
    public UserVO userInfo(@PathVariable @Min(value = 1, message = "用户ID不合法")
                          @Max(value = 200000, message = "用户ID不合法") String userId) throws IOException {
        String userInfo = redisTemplate.opsForValue().get(userInfoKey + ":" + userId);

        if(userInfo != null){
            cachedStatMap.hitIncrement(userInfoKey);
            return objectMapper.readValue(userInfo, UserVO.class);
        }

        UserVO userVO = new UserVO(userService.getUser(Integer.parseInt(userId)));
        redisTemplate.opsForValue().set(userInfoKey + ":" + userId, objectMapper.writeValueAsString(userVO));
        cachedStatMap.missIncrement(userInfoKey);
        return userVO;
    }

    @GetMapping("/")
    @ScopeLevel
    public UserVO currentUserInfo(){
        return new UserVO(LocalUser.getUser());
    }

    @GetMapping("/statistics/{userId}")
    public UserStatDTO userStat(@PathVariable @Min(value = 1, message = "用户ID不合法")
                                    @Max(value = 200000, message = "用户ID不合法") String userId) throws IOException {
        // key: "uscc:v1:user-stat"
        // value(hash):  userId1  {UserStatDTO1}
        //               userId2  {UserStatDTO2}
        String userStat = (String) redisTemplate.opsForHash().get(userStatKey, userId);

        if(userStat != null){
            cachedStatMap.hitIncrement(userStatKey);
            return objectMapper.readValue(userStat, UserStatDTO.class);
        }

        UserStatDTO userStatDTO = userService.getUserStat(Integer.parseInt(userId));
        redisTemplate.opsForHash().put(userStatKey, userId, objectMapper.writeValueAsString(userStatDTO));
        cachedStatMap.missIncrement(userStatKey);
        return userStatDTO;
    }

    @PostMapping("/modify/info")
    @ScopeLevel
    public Map<String, Object> modifyUserInfo(String nickname){
        int ret = 0;
        Map<String, Object> map = new HashMap<>();
        User user = LocalUser.getUser();
        if(nickname != null && nickname.trim().length() > 0 && nickname.length() <= 32){
            ret = userService.modifyInfo(user, nickname);
            map.put("result", ret > 0);
            map.put("message", ret > 0 ? "修改成功": "修改失败");

            if(ret > 0){
                redisTemplate.delete(userInfoKey + ":" + user.getId());
            }
            return map;
        }

        map.put("result", false);
        map.put("message", "修改失败");
        return map;
    }

    @PostMapping("/modify/image")
    @ScopeLevel
    public Map<String, Object> modifyUserImage(@RequestParam("file") MultipartFile file){
        Map<String, Object> map = new HashMap<>();
        User user = LocalUser.getUser();

        if(file.isEmpty()){
            throw new ParameterException(40003);
        }

        if(file.getSize() > maxImageSize){
            throw new ParameterException(40002);
        }

        map = userService.uploadImage(user, file);
        if(map != null && (boolean) map.get("result")){
            redisTemplate.delete(userInfoKey + ":" + user.getId());
        }
        return map;
    }

    @GetMapping("/critic/add/{userId}")
    @ScopeLevel(8)
    public Map<String, String> addCritic(@PathVariable @Min(value = 1, message = "用户ID不合法")
                                                @Max(value = 200000, message = "用户ID不合法") String userId){
        User user = userService.getUser(Integer.parseInt(userId));
        HashMap<String, String> map = new HashMap<>();
        int ret = 0;

        if(user == null || !user.getStatus()){
            map.put("msg", "用户不存在");
        }else if(user.getIsCritic() == 1){
            map.put("msg", "用户本来就是作者");
        }else{
            ret = userService.changeCritic(user, true);
            map.put("msg", ret > 0? "修改成功": "修改失败");
        }

        if(ret > 0){
            redisTemplate.delete(userInfoKey + ":" + user.getId());
            changeCriticMsgRabbitProducer.send(user.getId(), exchange, routingKey);
        }
        return map;
    }

    @GetMapping("/critic/remove/{userId}")
    @ScopeLevel(8)
    public Map<String, String> removeCritic(@PathVariable @Min(value = 1, message = "用户ID不合法")
                                         @Max(value = 200000, message = "用户ID不合法") String userId){
        User user = userService.getUser(Integer.parseInt(userId));
        HashMap<String, String> map = new HashMap<>();
        int ret = 0;

        if(user == null || !user.getStatus()){
            map.put("msg", "用户不存在");
        }else if(user.getIsCritic() == 0){
            map.put("msg", "用户本来就不是作者");
        }else{
            ret = userService.changeCritic(user, false);
            map.put("msg", ret > 0? "修改成功": "修改失败");
        }

        if(ret > 0){
            redisTemplate.delete(userInfoKey + ":" + user.getId());
            changeCriticMsgRabbitProducer.send(user.getId(), exchange, routingKey);
        }
        return map;
    }
}
