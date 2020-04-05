package com.ushill.service.interfaces.comments;

import com.ushill.DTO.CommentRatioDTO;
import com.ushill.DTO.CommentSummaryDTO;
import java.util.List;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/4/3 下午6:26
 */
public interface MovieCommentsService {

    public List<CommentSummaryDTO> getMovieCommentsSummary(int movieId, boolean isCritic);

    public List<CommentSummaryDTO> getMovieComments(int movieId, boolean isCritic, int page);

    public CommentRatioDTO getMovieCommentsRatio(int movieId, boolean isCritic);
}
