package com.ushill.service.impl.search;

import com.github.pagehelper.PageHelper;
import com.ushill.DTO.search.item.MovieRefineSearchResItemDTO;
import com.ushill.DTO.search.MovieSearchRefineResultDTO;
import com.ushill.DTO.search.item.MovieFastSearchResItemDTO;
import com.ushill.enums.MovieSearchMatchType;
import com.ushill.enums.RefineSearchType;
import com.ushill.exception.http.ParameterException;
import com.ushill.mapper.MovieMapper;
import com.ushill.models.Movie;
import com.ushill.service.interfaces.search.MovieSearchService;
import com.ushill.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieSearchServiceImpl implements MovieSearchService {
    @Autowired
    private MovieMapper movieMapper;

    @Value("${uscinema.limit.movies.fast-search}")
    private int maxMoviesCnt;

    @Value("${uscinema.limit.movies.refine-search}")
    private int moviesPerPage;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<MovieFastSearchResItemDTO> fastSearch(List<String> paraList) {
        List<MovieFastSearchResItemDTO> list = new ArrayList<>();
        list = fastSearch(paraList, MovieSearchMatchType.TITLE);
        if(list.size() < maxMoviesCnt){
            list.addAll(fastSearch(paraList, MovieSearchMatchType.NICKNAME));
            if(list.size() < maxMoviesCnt){
                list.addAll(fastSearch(paraList, MovieSearchMatchType.DIRECTORS));
                if(list.size() < maxMoviesCnt){
                    list.addAll(fastSearch(paraList, MovieSearchMatchType.ACTORS));
                }
            }
        }
        if(list.size() > maxMoviesCnt){
            list = list.subList(0, maxMoviesCnt);
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public MovieSearchRefineResultDTO refineSearchByName(List<String> paraList, int page) {
        PageHelper.startPage(page, this.moviesPerPage);
        List<Movie> moviesList = refineSearch(paraList, RefineSearchType.MOVIES_BY_NAME);
        PagedGridResult<Movie> paged = PagedGridResult.setPagedGrid(moviesList, page);

        List<MovieRefineSearchResItemDTO> list = new ArrayList<>();
        paged.getRows().forEach(movie -> list.add(new MovieRefineSearchResItemDTO(movie, RefineSearchType.MOVIES_BY_NAME, paraList)));

        MovieSearchRefineResultDTO movieSearchRefineResultDTO = new MovieSearchRefineResultDTO();
        movieSearchRefineResultDTO.setMovieResList(list);
        movieSearchRefineResultDTO.setHasNext(page < paged.getTotal());

        return movieSearchRefineResultDTO;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public MovieSearchRefineResultDTO refineSearchByPeople(List<String> paraList, int page) {
        PageHelper.startPage(page, this.moviesPerPage);
        List<Movie> moviesList = refineSearch(paraList, RefineSearchType.MOVIES_BY_PEOPLE);
        PagedGridResult<Movie> paged = PagedGridResult.setPagedGrid(moviesList, page);

        List<MovieRefineSearchResItemDTO> list = new ArrayList<>();
        paged.getRows().forEach(movie -> list.add(new MovieRefineSearchResItemDTO(movie, RefineSearchType.MOVIES_BY_PEOPLE, paraList)));

        MovieSearchRefineResultDTO movieSearchRefineResultDTO = new MovieSearchRefineResultDTO();
        movieSearchRefineResultDTO.setMovieResList(list);
        movieSearchRefineResultDTO.setHasNext(page < paged.getTotal());

        return movieSearchRefineResultDTO;
    }

    private List<MovieFastSearchResItemDTO> fastSearch(List<String> paraList, MovieSearchMatchType type){
        List<MovieFastSearchResItemDTO> list = new ArrayList<>();

        PageHelper.startPage(1, maxMoviesCnt);
        Example example = new Example(Movie.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1);
        paraList.forEach(para->criteria.andLike(type.getPropertyName(), "%" + para + "%"));
        List<Movie> moviesList = movieMapper.selectByExample(example);
        moviesList.forEach(movie -> list.add(new MovieFastSearchResItemDTO(movie, type, paraList)));
        return list;
    }

    private List<Movie> refineSearch(List<String> paraList, RefineSearchType type){
        Example example = new Example(Movie.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1);
        paraList.forEach(para->criteria.andCondition(conditionByType(type, para)));
        List<Movie> moviesList = movieMapper.selectByExample(example);
        return moviesList;
    }

    private String conditionByType(RefineSearchType type, String para){
        String condition = "";
        switch (type){
            case MOVIES_BY_NAME:
                condition = "(title Like \"%" + para + "%\" or nickname Like \"%" + para + "%\")";
                break;
            case MOVIES_BY_PEOPLE:
                condition = "(directors_name Like \"%" + para + "%\" or actors_name Like \"%" + para + "%\")";
                break;
            default:
                throw new ParameterException(10001);
        }
        return condition;
    }
}
