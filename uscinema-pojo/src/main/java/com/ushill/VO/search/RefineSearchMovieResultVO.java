package com.ushill.VO.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ushill.DTO.search.item.MovieRefineSearchResItemDTO;
import com.ushill.DTO.search.MovieSearchRefineResultDTO;
import com.ushill.VO.search.item.MovieRefineSearchItemVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefineSearchMovieResultVO {

    List<MovieRefineSearchItemVO> data = new ArrayList<>();

    @JsonProperty(value = "has_next")
    boolean hasNext;

    public RefineSearchMovieResultVO(MovieSearchRefineResultDTO resultDTO){
        List<MovieRefineSearchResItemDTO> movieList = resultDTO.getMovieResList();
        movieList.forEach(movie -> {
            MovieRefineSearchItemVO movieRes = new MovieRefineSearchItemVO();
            BeanUtils.copyProperties(movie.getMovie(), movieRes);
            BeanUtils.copyProperties(movie, movieRes);
            this.data.add(movieRes);
        });
        this.hasNext = resultDTO.isHasNext();
    }
}
