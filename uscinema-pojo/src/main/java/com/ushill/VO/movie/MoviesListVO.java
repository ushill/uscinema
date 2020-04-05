package com.ushill.VO.movie;

import com.ushill.DTO.MovieDTO;
import com.ushill.models.Movie;
import com.ushill.utils.PagedGridResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoviesListVO implements Serializable {
    List<MovieDTO> movies;
    int per_page;
    int total;

    public MoviesListVO(PagedGridResult<Movie> paged){
        List<Movie> list = paged.getRows();
        this.movies = new ArrayList<>();
        list.forEach(m->{
            MovieDTO md = new MovieDTO();
            BeanUtils.copyProperties(m, md);
            this.movies.add(md);
        });
        this.per_page = paged.getPer();
        this.total = (int)paged.getRecords();
    }
}
