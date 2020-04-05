package com.ushill.controller.v1.user;

import com.ushill.DTO.user.UserLoginByEmailDTO;
import com.ushill.DTO.user.UserLoginByUsernameDTO;
import com.ushill.DTO.user.UserRegisterByEmailDTO;
import com.ushill.VO.user.UserCheckVO;
import com.ushill.VO.user.UserAuthVO;
import com.ushill.exception.http.ForbiddenException;
import com.ushill.interceptor.LocalUser;
import com.ushill.interceptor.ScopeLevel;
import com.ushill.models.User;
import com.ushill.service.interfaces.UserService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

/**
 * @author ：五羊
 * @description：注册/登录/获取令牌
 * @date ：2020/3/29 下午9:25
 */

@RestController
@RequestMapping("/v1/auth")
@Validated
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/by/email")
    public UserAuthVO registerByEmail(@Validated UserRegisterByEmailDTO userDTO)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        if(userService.findUserByEmail(userDTO.getEmail()) != null
                || userService.findUserByUsername(userDTO.getUsername()) != null){
            return new UserAuthVO(false, "该邮箱或用户名已被注册");
        }else if(userService.register(userDTO)){
            return new UserAuthVO(true, "注册成功");
        }
        return new UserAuthVO(false, "注册失败，请稍后再试");
    }

    @GetMapping("/check/email")
    public UserCheckVO checkEmail(@RequestParam @Email @NotBlank
                                      @Length(min = 8, max = 64, message = "邮箱长度应限制在8-64之间") String email){
        if(userService.findUserByEmail(email) != null){
            return new UserCheckVO(false, "该邮箱已被注册");
        }
        return new UserCheckVO(true, "该邮箱可以注册");
    }

    @GetMapping("/check/username")
    public UserCheckVO checkUsername(@NotBlank @Length(min = 6, max = 24, message = "用户名长度应限制在6-24之间")
                                         @Pattern(regexp = "^[A-Za-z0-9_\\-.]{6,24}$", message = "用户名格式不符合规范")
                                         String username){
        if(userService.findUserByUsername(username) != null){
            return new UserCheckVO(false, "该用户名已被注册");
        }
        return new UserCheckVO(true, "该用户名可以注册");
    }

    @PostMapping("/login/by/email")
    public UserAuthVO loginByEmail(@Validated UserLoginByEmailDTO userDTO) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User user = userService.findUserByEmail(userDTO.getInfo());
        if(user == null){
            return new UserAuthVO(false, "邮箱或密码错误");
        }else if(!user.getStatus()){
            return new UserAuthVO(false, "用户已被注销，不可登录");
        }

        String token = userService.login(user, userDTO.getPassword());
        if(token != null){
            return new UserAuthVO(true, token);
        }
        return new UserAuthVO(false, "邮箱或密码错误");
    }

    @PostMapping("/login/by/username")
    public UserAuthVO loginByUsername(@Validated UserLoginByUsernameDTO userDTO) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User user = userService.findUserByUsername(userDTO.getInfo());
        if(user == null){
            return new UserAuthVO(false, "用户名或密码错误");
        }else if(!user.getStatus()){
            return new UserAuthVO(false, "用户已被注销，不可登录");
        }

        String token = userService.login(user, userDTO.getPassword());
        if(token != null){
            return new UserAuthVO(true, token);
        }
        return new UserAuthVO(false, "用户名或密码错误");
    }

    @GetMapping("/register/by/email")
    public Object registerByEmail(){
        throw new ForbiddenException(10002);
    }

    @GetMapping("/login/by/email")
    public Object loginByEmail(){
        throw new ForbiddenException(10002);
    }

    @GetMapping("/login/by/username")
    public Object loginByUsername(){
        throw new ForbiddenException(10002);
    }

//    @ScopeLevel(8)
//    @GetMapping("/init/password")
//    public Object initPasswordTest() throws InvalidKeySpecException, NoSuchAlgorithmException {
//        int i = userService.initPasswordTest();
//        HashMap<String, Integer> map = new HashMap<>();
//        map.put("res", i);
//        return map;
//    }

    @ScopeLevel(4)
    @GetMapping("/authorize")
    public Object setAuthority(@RequestParam @Min(value = 1, message = "用户ID不合法")
                                   @Max(value = 200000, message = "用户ID不合法") String userId,
                                   @RequestParam @Min(value = 1, message = "权限范围不合法")
                                   @Max(value = 8, message = "权限范围不合法") String authority){
        User user = userService.getUser(Integer.parseInt(userId));
        int adminScope = LocalUser.getScope();
        int attemptScope = Integer.parseInt(authority);
        HashMap<String, String> map = new HashMap<>();

        if(user == null){
            map.put("msg", "用户不存在");
        }else if(adminScope <= user.getAuthority() || adminScope <= attemptScope){
            map.put("msg", "只能修改更低权限用户的权限, 且最高不能超过自身的权限");
        }else if(user.getAuthority() == attemptScope){
            map.put("msg", "权限未变更");
        }else{
            map.put("msg", userService.authorize(user, attemptScope)>0? "更新成功": "更新失败");
        }
        return map;
    }
}
