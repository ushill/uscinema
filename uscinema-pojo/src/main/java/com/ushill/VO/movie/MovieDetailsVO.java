package com.ushill.VO.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ushill.DTO.MovieDTO;
import com.ushill.models.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailsVO implements Serializable {

    @JsonProperty("movie")
    MovieDTO movieDTO;

    public MovieDetailsVO(Movie movie){
        movieDTO = new MovieDTO();
        BeanUtils.copyProperties(movie, movieDTO);
    }
}
