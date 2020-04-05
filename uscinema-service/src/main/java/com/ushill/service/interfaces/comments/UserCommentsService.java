package com.ushill.service.interfaces.comments;

import com.ushill.DTO.UserCommentDTO;
import com.ushill.DTO.UserCommentSummaryDTO;
import com.ushill.DTO.UserStatDTO;
import com.ushill.utils.PagedGridResult;

import java.util.List;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/4/3 下午6:26
 */
public interface UserCommentsService {

    public PagedGridResult<UserCommentDTO> getUserComments(int userId, int page);

    public PagedGridResult<UserCommentSummaryDTO> getUserCommentsSummary(int userId, int page);

    public List<UserStatDTO> getCriticsStat(int reqType);

//    public PagedGridResult<UserComments> getUserCommentsPage(int userId, int reqType, int page);
}
