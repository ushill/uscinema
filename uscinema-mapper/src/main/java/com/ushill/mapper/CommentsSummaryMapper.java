package com.ushill.mapper;

import com.ushill.DTO.*;

import java.util.List;
import java.util.Map;

public interface CommentsSummaryMapper {

    List<CommentSummaryDTO> getCommentsSummary(Map map);

    List<CommentSummaryDTO> getComments(Map map);

    CommentRatioDTO getCommentsRatio(Map map);

    CheckRatingDTO checkRating(Map map);

    int updateUserRating(Map map);

    int updateCriticRating(Map map);

    List<UserCommentDTO> getUserComments(Map map);

    List<UserCommentSummaryDTO> getUserCommentsSummary(Map map);

    List<UserStatDTO> getCriticsStat(int reqType);

    UserStatDTO getUserStat(int userId);
}