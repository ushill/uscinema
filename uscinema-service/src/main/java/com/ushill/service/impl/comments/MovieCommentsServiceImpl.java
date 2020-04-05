package com.ushill.service.impl.comments;

import com.ushill.DTO.CommentRatioDTO;
import com.ushill.DTO.CommentSummaryDTO;
import com.ushill.mapper.CommentsSummaryMapper;
import com.ushill.mapper.UserCommentsMapper;
import com.ushill.service.interfaces.comments.MovieCommentsService;
import com.ushill.utils.RandomSubList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/4/3 下午6:27
 */
@Service
public class MovieCommentsServiceImpl implements MovieCommentsService {

    private final CommentsSummaryMapper commentsSummaryMapper;

    @Value("${uscinema.limit.comments.summary.critic}")
    private int criticSummaryCommentsMaxNum;

    @Value("${uscinema.limit.comments.summary.user}")
    private int userSummaryCommentsMaxNum;

    @Value("${uscinema.limit.comments.page}")
    private int commentNumPerPage;

    public MovieCommentsServiceImpl(CommentsSummaryMapper commentsSummaryMapper) {
        this.commentsSummaryMapper = commentsSummaryMapper;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CommentSummaryDTO> getMovieCommentsSummary(int movieId, boolean isCritic){
        Map<String, Object> map = new HashMap<>();
        map.put("movieId", movieId);
        map.put("isCritic", isCritic);
        List<CommentSummaryDTO> list = commentsSummaryMapper.getCommentsSummary(map);
        return this.selectRandomComments(list, isCritic);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CommentSummaryDTO> getMovieComments(int movieId, boolean isCritic, int page){
        Map<String, Object> map = new HashMap<>();
        map.put("movieId", movieId);
        map.put("isCritic", isCritic);
        map.put("offset", (page - 1) * commentNumPerPage);
        map.put("limit", commentNumPerPage);
        return commentsSummaryMapper.getComments(map);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public CommentRatioDTO getMovieCommentsRatio(int movieId, boolean isCritic){
        Map<String, Object> map = new HashMap<>();
        map.put("movieId", movieId);
        map.put("isCritic", isCritic);
        CommentRatioDTO ratio = commentsSummaryMapper.getCommentsRatio(map);
        ratio.setDisplay(commentNumPerPage);
        return ratio;
    }

    private List<CommentSummaryDTO> selectRandomComments(List<CommentSummaryDTO> comments, boolean isCritic){
        if(isCritic){
            return this.selectRandomCriticComments(comments);
        }else{
            return this.selectRandomUserComments(comments);
        }
    }

    private List<CommentSummaryDTO> selectRandomCriticComments(List<CommentSummaryDTO> comments){
        List<CommentSummaryDTO> commented_comments;
        if(comments.size() > criticSummaryCommentsMaxNum){
            commented_comments = comments.stream().filter(cmt->!cmt.getComment().isEmpty()).collect(Collectors.toList());
            if(commented_comments.size() > criticSummaryCommentsMaxNum){
                comments = RandomSubList.randomSublist(commented_comments, criticSummaryCommentsMaxNum,
                        Comparator.comparing(cmt -> - Double.parseDouble(cmt.getScore())));
            }else{
                comments = comments.subList(0, criticSummaryCommentsMaxNum);
            }
        }
        return comments;
    }

    private List<CommentSummaryDTO> selectRandomUserComments(List<CommentSummaryDTO> comments){
        List<CommentSummaryDTO> commented_comments;
        if(comments.size() > userSummaryCommentsMaxNum){
            commented_comments = comments.stream().filter(cmt->cmt.getVotes() > 10).collect(Collectors.toList());
            if(commented_comments.size() > 2 * userSummaryCommentsMaxNum){
                comments = RandomSubList.randomSublist(commented_comments, userSummaryCommentsMaxNum,
                        Comparator.comparing(cmt -> - cmt.getVotes()));
            }else{
                comments = RandomSubList.randomSublist(comments, userSummaryCommentsMaxNum,
                        Comparator.comparing(cmt -> - cmt.getVotes()));
            }
        }
        return comments;
    }

}
