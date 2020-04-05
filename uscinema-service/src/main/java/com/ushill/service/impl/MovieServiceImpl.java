package com.ushill.service.impl;

import com.github.pagehelper.PageHelper;
import com.ushill.DTO.MovieSummaryDTO;
import com.ushill.enums.MovieListType;
import com.ushill.exception.http.NotFoundException;
import com.ushill.exception.http.ParameterException;
import com.ushill.mapper.ComingSoonMapper;
import com.ushill.mapper.MovieMapper;
import com.ushill.mapper.NowplayingMapper;
import com.ushill.models.ComingSoon;
import com.ushill.models.Movie;
//import com.ushill.repository.MoviesRepository;
import com.ushill.models.Nowplaying;
import com.ushill.service.interfaces.MovieService;
import com.ushill.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    @Value("${uscinema.limit.movies.list-page}")
    private Integer moviesPerPage;

    @Value("${uscinema.limit.movies.top}")
    private int topMoviesTotal;

    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private NowplayingMapper nowplayingMapper;
    @Autowired
    private ComingSoonMapper comingSoonMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Movie getMovie(int id) {
        Movie movie = movieMapper.selectByPrimaryKey(id);
        if (movie == null || !movie.getStatus()){
            throw new NotFoundException(20001);
        }
        return movie;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult<Movie> getAllMovies(int pageNum, boolean scoped) {
        PageHelper.startPage(pageNum, this.moviesPerPage);
        List<Movie> moviesList = movieMapper.selectByExample(getSqlExample(MovieListType.ALL, scoped));
        return PagedGridResult.setPagedGrid(moviesList, pageNum);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult<Movie> getComingSoonMovies(int pageNum) {
        PageHelper.startPage(pageNum, this.moviesPerPage);
        List<Movie> moviesList = movieMapper.selectByExample(getSqlExample(MovieListType.COMING_SOON));
        return PagedGridResult.setPagedGrid(moviesList, pageNum);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult<Movie> getBestNewMovies(int pageNum) {
        PageHelper.startPage(pageNum, this.moviesPerPage);
        List<Movie> moviesList = movieMapper.selectByExample(getSqlExample(MovieListType.BNM));
        return PagedGridResult.setPagedGrid(moviesList, pageNum);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult<Movie> getTopMovies(int pageNum, int type) {
        if ((pageNum - 1) * moviesPerPage > topMoviesTotal){
            throw new ParameterException(10001);
        }

        PageHelper.startPage(pageNum, this.moviesPerPage);
        MovieListType movieListType = type == 1? MovieListType.TOP: MovieListType.WORST;
        List<Movie> moviesList = movieMapper.selectByExample(getSqlExample(movieListType));
        PagedGridResult<Movie> grid = PagedGridResult.setPagedGrid(moviesList, pageNum);
        grid.setRecords(topMoviesTotal);

        if (pageNum * moviesPerPage > topMoviesTotal){
            int count = Math.min(topMoviesTotal + moviesPerPage - pageNum * moviesPerPage, moviesList.size());
            grid.setRows(moviesList.subList(0, count));
        }
        return grid;
    }

    private static Example getSqlExample(MovieListType movieListType){
        return MovieServiceImpl.getSqlExample(movieListType, false);
    }

    private static Example getSqlExample(MovieListType movieListType, boolean scoped){
        Example example = new Example(Movie.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1);
        switch (movieListType){
            case ALL:
                criteria.andLessThanOrEqualTo("releaseDateCn", new Date());
                if (scoped){
                    criteria.andIsNotNull("usccRating");
                }
                example.setOrderByClause("release_date_cn desc");
                break;
            case COMING_SOON:
                criteria.andGreaterThan("releaseDateCn", new Date());
                example.setOrderByClause("release_date_cn asc");
                break;
            case BNM:
                criteria.andLessThanOrEqualTo("releaseDateCn", new Date());
                criteria.andEqualTo("bnm", true);
                example.setOrderByClause("release_date_cn desc");
                break;
            case TOP:
                criteria.andLessThanOrEqualTo("releaseDateCn", new Date());
                criteria.andGreaterThanOrEqualTo("firstYear", 2002);
                example.setOrderByClause("uscc_rating desc");
                break;
            case WORST:
                criteria.andLessThanOrEqualTo("releaseDateCn", new Date());
                criteria.andGreaterThanOrEqualTo("firstYear", 2002);
                criteria.andIsNotNull("usccRating");
                criteria.andGreaterThan("usccRatingCnt", 6);
                example.setOrderByClause("uscc_rating asc");
                break;
        }
        return example;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<MovieSummaryDTO> getNowPlayingMoviesSummary() {
        Example example = new Example(Nowplaying.class);
        example.setOrderByClause("weight asc");
        List<MovieSummaryDTO> list = nowplayingMapper.getNowplayingSummary();
        list = list.stream().
                filter(m->m.getWeight() < 12 || m.getScore() != null).
                collect(Collectors.toList());
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<MovieSummaryDTO> getComingSoonMoviesSummary() {
        Example example = new Example(ComingSoon.class);
        example.setOrderByClause("weight asc");
        List<MovieSummaryDTO> list = comingSoonMapper.getComingSoonSummary();
        if(list.size() < 18){
            return list;
        }
        return list.subList(0, 18);
    }
}
