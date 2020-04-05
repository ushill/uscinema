package com.ushill.service.interfaces.comments;

import com.ushill.models.UserComments;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/4/3 下午6:26
 */
public interface MyCommentsService {

    public UserComments getUserValidComment(int userId, int movieId);

    public int updateComment(int userId, int movieId, Double score, String comment);

    public int deleteComment(int userId, int movieId);

}
