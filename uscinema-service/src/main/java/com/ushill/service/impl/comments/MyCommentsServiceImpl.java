package com.ushill.service.impl.comments;

import com.ushill.mapper.UserCommentsMapper;
import com.ushill.models.UserComments;
import com.ushill.service.interfaces.comments.MyCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/4/3 下午6:27
 */
@Service
public class MyCommentsServiceImpl implements MyCommentsService {

    private final UserCommentsMapper userCommentsMapper;

    public MyCommentsServiceImpl(UserCommentsMapper userCommentsMapper) {
        this.userCommentsMapper = userCommentsMapper;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserComments getUserValidComment(int userId, int movieId) {
        UserComments userComments = new UserComments();
        userComments.setUserId(userId);
        userComments.setMovieId(movieId);
        userComments.setStatus((byte)1);
        return userCommentsMapper.selectOne(userComments);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteComment(int userId, int movieId) {
        Example example = new Example(UserComments.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1);
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("movieId", movieId);

        UserComments userComments = new UserComments();
        userComments.setStatus((byte)0);
        return userCommentsMapper.updateByExampleSelective(userComments, example);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateComment(int userId, int movieId, Double score, String comment) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        UserComments userComments = getUserComment(userId, movieId);
        if(userComments == null){
            userComments = new UserComments();
            userComments.setUserId(userId);
            userComments.setMovieId(movieId);
        }
        if(score == null){
            userComments.setScore(null);
        }else {
            userComments.setScore(new BigDecimal(score / 2));
        }
        if(userComments.getStatus() == null || userComments.getStatus() == 0){
            userComments.setCommentTime(df.format(new Date()));
            userComments.setStatus((byte)1);
        }
        userComments.setComment(comment);
        userComments.setUpdateTime(new Date());
        return userComments.getId() != null?
                userCommentsMapper.updateByPrimaryKey(userComments):
                userCommentsMapper.insertSelective(userComments);
    }

    public UserComments getUserComment(int userId, int movieId) {
        UserComments userComments = new UserComments();
        userComments.setUserId(userId);
        userComments.setMovieId(movieId);
        return userCommentsMapper.selectOne(userComments);
    }
}
