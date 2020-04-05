package com.ushill.service.impl.comments;

import com.github.pagehelper.PageHelper;
import com.ushill.DTO.UserCommentDTO;
import com.ushill.DTO.UserCommentSummaryDTO;
import com.ushill.DTO.UserStatDTO;
import com.ushill.mapper.CommentsSummaryMapper;
import com.ushill.mapper.UserCommentsMapper;
import com.ushill.models.UserComments;
import com.ushill.service.interfaces.comments.UserCommentsService;
import com.ushill.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/4/3 下午6:27
 */
@Service
public class UserCommentsServiceImpl implements UserCommentsService {

    private final CommentsSummaryMapper commentsSummaryMapper;
    private final UserCommentsMapper userCommentsMapper;

    @Value("${uscinema.limit.comments.user-page.summary}")
    private int commentNumUserSummaryPage;

    @Value("${uscinema.limit.comments.user-page.page}")
    private int commentNumUserPage;

    public UserCommentsServiceImpl(CommentsSummaryMapper commentsSummaryMapper, UserCommentsMapper userCommentsMapper) {
        this.commentsSummaryMapper = commentsSummaryMapper;
        this.userCommentsMapper = userCommentsMapper;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult<UserCommentDTO> getUserComments(int userId, int page) {
        PageHelper.startPage(page, this.commentNumUserPage);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<UserCommentDTO> list = commentsSummaryMapper.getUserComments(map);
        return PagedGridResult.setPagedGrid(list, page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult<UserCommentSummaryDTO> getUserCommentsSummary(int userId, int page) {
        PageHelper.startPage(page, this.commentNumUserSummaryPage);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<UserCommentSummaryDTO> list = commentsSummaryMapper.getUserCommentsSummary(map);
        return PagedGridResult.setPagedGrid(list, page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<UserStatDTO> getCriticsStat(int reqType) {
        return commentsSummaryMapper.getCriticsStat(reqType);
    }

    //    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult<UserComments> getUserCommentsPage(int userId, int reqType, int page) {
        int perPage = reqType == 2? commentNumUserSummaryPage: commentNumUserPage;
        PageHelper.startPage(page, perPage);
        Example example = new Example(UserComments.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1);
        criteria.andEqualTo("userId", userId);
        example.setOrderByClause("comment_time desc");
        return PagedGridResult.setPagedGrid(userCommentsMapper.selectByExample(example), page);
    }
}
