package com.ushill.service.interfaces;

import com.ushill.DTO.UserStatDTO;
import com.ushill.DTO.user.UserRegisterByEmailDTO;
import com.ushill.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

//import org.springframework.data.domain.Page;

public interface UserService {

    public User getUser(int id);

    public User findUserByEmail(String email);

    public User findUserByUsername(String username);

    public boolean register(UserRegisterByEmailDTO userDTO) throws InvalidKeySpecException, NoSuchAlgorithmException;

    public String login(User user, String attempedPassword) throws InvalidKeySpecException, NoSuchAlgorithmException;

    public int initPasswordTest() throws InvalidKeySpecException, NoSuchAlgorithmException;

    public int authorize(User user, int scope);

    public int modifyInfo(User user, String nickname);

    public Map<String, Object> uploadImage(User user,  MultipartFile file);

    public UserStatDTO getUserStat(int userId);

    public int changeCritic(User user, boolean isAdd);
}
