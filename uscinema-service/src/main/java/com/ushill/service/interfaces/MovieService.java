package com.ushill.service.interfaces;

import com.ushill.DTO.MovieSummaryDTO;
import com.ushill.models.Movie;
import com.ushill.utils.PagedGridResult;
//import org.springframework.data.domain.Page;

import java.util.List;

public interface MovieService {

    public Movie getMovie(int id);

    public PagedGridResult<Movie> getAllMovies(int pageNum, boolean scoped);

    public PagedGridResult<Movie> getComingSoonMovies(int pageNum);

    public PagedGridResult<Movie> getBestNewMovies(int pageNum);

    public PagedGridResult<Movie> getTopMovies(int pageNum, int type);

    public List<MovieSummaryDTO> getNowPlayingMoviesSummary();

    public List<MovieSummaryDTO> getComingSoonMoviesSummary();
}
