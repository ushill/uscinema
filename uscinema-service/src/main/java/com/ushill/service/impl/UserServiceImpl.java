package com.ushill.service.impl;

import com.ushill.DTO.UserStatDTO;
import com.ushill.DTO.user.UserRegisterByEmailDTO;
import com.ushill.exception.http.NotFoundException;
import com.ushill.mapper.CommentsSummaryMapper;
import com.ushill.mapper.UserMapper;
import com.ushill.models.User;
import com.ushill.service.interfaces.UserService;
import com.ushill.utils.FtpUtils;
import com.ushill.utils.JwtToken;
import com.ushill.utils.PBKDF2Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final CommentsSummaryMapper commentsSummaryMapper;

    private final PBKDF2Util pbkdf2Util;

    @Value("${uscinema.img-dir.storage-path}")
    private String storagePath;

    @Value("${uscinema.img-dir.user-path}")
    private String userImagePath;

    public UserServiceImpl(UserMapper userMapper, CommentsSummaryMapper commentsSummaryMapper, PBKDF2Util pbkdf2Util) {
        this.userMapper = userMapper;
        this.commentsSummaryMapper = commentsSummaryMapper;
        this.pbkdf2Util = pbkdf2Util;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User getUser(int id) {
        User User = userMapper.selectByPrimaryKey(id);
        if (User == null || !User.getStatus()){
            throw new NotFoundException(40001);
        }
        return User;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User findUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return userMapper.selectOne(user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User findUserByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        return userMapper.selectOne(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean register(UserRegisterByEmailDTO userDTO) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(pbkdf2Util.getEncryptedPassword(userDTO.getPassword()));

        return userMapper.insertSelective(user) > 0;
    }

    public boolean checkPassword(String attempedPassword, String pbkdfPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return pbkdfPassword != null && !pbkdfPassword.equals("")
                && pbkdf2Util.authenticate(attempedPassword, pbkdfPassword);
    }

    @Override
    public String login(User user, String attempedPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if(!checkPassword(attempedPassword, user.getPassword())){
            return null;
        }
        String token = JwtToken.makeToken(Long.valueOf(user.getId()), user.getAuthority());
        return token;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public int initPasswordTest(){
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
//        criteria.andIsNull("password");
        criteria.andEqualTo("isCritic", true);
        AtomicInteger ret = new AtomicInteger();

        List<User> users = userMapper.selectByExample(example);
        users.forEach(user->{
            try {
                String encryptedPwd = pbkdf2Util.getEncryptedPassword(user.getUsername() + "123");
                user.setPassword(encryptedPwd);
                ret.addAndGet(userMapper.updateByPrimaryKey(user));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return ret.get();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int authorize(User user, int scope){
        user.setAuthority(scope);
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int modifyInfo(User user, String nickname) {
        user.setNickname(nickname);
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> uploadImage(User user, MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));

        // 重新生成文件名
        fileName = user.getId() + "-" + UUID.randomUUID().toString() + suffixName;

        // 文件存储到本地服务器
        // String filePath = "/xxx/xxx";
        // file.transferTo(new File(filePath + fileName));

        // 文件存储到远端服务器
        try {
            String partPath = userImagePath;
            FtpUtils.FtpRetCode code = FtpUtils.uploadFile(partPath, file.getBytes(), fileName);
            if(code == FtpUtils.FtpRetCode.OK){
                user.setImageStorePath(fileName);
                if(userMapper.updateByPrimaryKeySelective(user) > 0){
                    map.put("result", true);
                    map.put("message", storagePath + userImagePath + "/" + fileName);
                }else {
                    map.put("result", false);
                    map.put("message", "更新图片失败");
                }
            }else{
                map.put("result", false);
                map.put("message", code.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", false);
            map.put("message", "操作异常");
            return map;
        }
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserStatDTO getUserStat(int userId) {
        return commentsSummaryMapper.getUserStat(userId);
    }

    @Override
    public int changeCritic(User user, boolean isAdd) {
        user.setIsCritic((byte)(isAdd? 1: 0));
        return userMapper.updateByPrimaryKeySelective(user);
    }
}
