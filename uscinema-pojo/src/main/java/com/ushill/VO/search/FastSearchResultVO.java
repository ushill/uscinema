package com.ushill.VO.search;

import com.ushill.DTO.search.item.MovieFastSearchResItemDTO;
import com.ushill.DTO.search.item.UserFastSearchResItemDTO;
import com.ushill.VO.search.item.MovieFastSearchResultItemVO;
import com.ushill.VO.search.item.UserFastSearchResultItemVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FastSearchResultVO {
    int seq;
    List<MovieFastSearchResultItemVO> movies = new ArrayList<>();
    List<UserFastSearchResultItemVO> critics = new ArrayList<>();
    List<UserFastSearchResultItemVO> users = new ArrayList<>();

    public FastSearchResultVO(int seq, List<MovieFastSearchResItemDTO> movies,
                              List<UserFastSearchResItemDTO> critics, List<UserFastSearchResItemDTO> users,
                              List<String> paraList){
        this.seq = seq;
        movies.forEach(movie -> {
            MovieFastSearchResultItemVO movieRes = new MovieFastSearchResultItemVO();
            BeanUtils.copyProperties(movie.getMovie(), movieRes);
            BeanUtils.copyProperties(movie, movieRes);
            this.movies.add(movieRes);
        });
        critics.forEach(critic -> {
            UserFastSearchResultItemVO criticRes = new UserFastSearchResultItemVO();
            BeanUtils.copyProperties(critic.getUser(), criticRes);
            BeanUtils.copyProperties(critic, criticRes);
            this.critics.add(criticRes);
        });
        users.forEach(user -> {
            UserFastSearchResultItemVO userRes = new UserFastSearchResultItemVO();
            BeanUtils.copyProperties(user.getUser(), userRes);
            BeanUtils.copyProperties(user, userRes);
            this.users.add(userRes);
        });
    }
}
