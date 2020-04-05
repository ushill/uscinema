package com.ushill.service.impl;

import com.ushill.DTO.CheckRatingDTO;
import com.ushill.exception.http.NotFoundException;
import com.ushill.mapper.CommentsSummaryMapper;
import com.ushill.mapper.MovieMapper;
import com.ushill.models.Movie;
import com.ushill.service.interfaces.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private CommentsSummaryMapper commentsSummaryMapper;

    @Autowired
    private MovieMapper movieMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CheckRatingDTO checkMovieRating(int movieId, boolean isCritic) {
        Map<String, Object> map = new HashMap<>();
        map.put("movieId", movieId);
        map.put("isCritic", isCritic);
        CheckRatingDTO checkRatingDTO = commentsSummaryMapper.checkRating(map);
        if(checkRatingDTO == null){
            throw new NotFoundException(30004);
        }
        return checkRatingDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateMovieUserRating(int movieId){
        CheckRatingDTO checkRatingDTO = this.checkMovieRating(movieId, false);
        double rating = userRatingCal(checkRatingDTO.getTotal(), checkRatingDTO.getRating().doubleValue());
        int res = 0;

        Map<String, Object> map = new HashMap<>();
        map.put("movieId", movieId);
        map.put("rating", rating >= 0? rating: null);
        try {
            res = commentsSummaryMapper.updateUserRating(map);
        }catch(Exception e){
            e.printStackTrace();
            throw new NotFoundException(30003);
        }
        return res;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateMovieCriticRating(int movieId){
        CheckRatingDTO checkRatingDTO = this.checkMovieRating(movieId, true);
        int res = 0;

        Map<String, Object> map = criticRatingCal(checkRatingDTO);
        try {
            res = commentsSummaryMapper.updateCriticRating(map);
        }catch(Exception e){
            e.printStackTrace();
            throw new NotFoundException(30003);
        }
        return res;
    }

    @Override
    public int updateMovieRating(int movieId, boolean isCritic) {
        if(isCritic) {
            return this.updateMovieCriticRating(movieId);
        }else {
            return this.updateMovieUserRating(movieId);
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void updateAll() {
        List<Movie> list = movieMapper.selectAll();
//        Example example = new Example(Movie.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andIsNotNull("usccRating");
//        criteria.andLessTandhan("usccRating", 5.0);
//        criteria.andLessThan("userRating", 1);
//        List<Movie> list = movieMapper.selectByExample(example);
        System.out.println(list.size());
        list.forEach(m->{
            updateMovieCriticRating(m.getId());
            updateMovieUserRating(m.getId());
            System.out.println("电影" + m.getTitle() + "已更新");
        });
    }

    private static double userRatingCal(int count, double origin){
        double ret;
        if(count < 6){
            return -1;
        }

        if(origin > 8.5){
            ret = 0.67 * origin + 3.33;
        }else if(origin > 5.5){
            ret = origin + 0.5;
        }else if(origin > 4){
            ret = 1.33 * (origin - 1);
        }else{
            ret = 1.6 * origin - 2.4;
        }
        return ret;
    }

    private static double criticRatingCal(int count, double origin, int first_year){
        double ret;
        if(origin > 8.5){
            ret = 0.67 * origin + 3.33;
        }else if(origin > 5.5){
            ret = origin + 0.5;
        }else if(origin > 4){
            ret = 1.33 * (origin - 1);
        }else{
//            ret = 1.6 * origin - 2.4;
            ret = origin - ((double)count - 8) / 36 + (origin - ((double)count - 8) / 36 - 3.55)/2;
        }

        if(count < 12){
            if(ret > 7.9){
                ret -= 0.1;
            }
            if(ret > 8.3) {
                ret -= 0.1;
            }
            if(ret > 8.6){
                ret -= 0.1;
            }
        }

//        if(ret < 1.5) {
//            ret -= Math.min((double)count / 4, 10) / 20;
//        }

        if(ret >= 7.0 && count >= 30 && first_year <= 2008){
            ret += 0.1;
        }
        return ret;
    }

    private static Map<String, Object> criticRatingCal(CheckRatingDTO checkRatingDTO){
        double origin = checkRatingDTO.getRating().doubleValue();
        int cnt = checkRatingDTO.getTotal();
        int first_year = checkRatingDTO.getFirstYear();
        int excellent = checkRatingDTO.getExcellent();
        int bnm = 0;

        Map<String, Object> map = new HashMap<>();
        map.put("movieId", checkRatingDTO.getMovieId());
        map.put("cnt", cnt);

        if(cnt < 6) {
            map.put("ratingOrigin", null);
            map.put("rating", null);
            map.put("bnm", 0);
            return map;
        }

//        System.out.println(origin);
        double rating = criticRatingCal(cnt, origin, first_year);
//        System.out.println(rating);

        if(rating >= 7.8 && cnt >= 15 && (rating >= 7.9 || excellent >= 6))
        {
            bnm = 1;
        }
        map.put("ratingOrigin", origin);
        map.put("rating", rating);
        map.put("bnm", bnm);
        return map;
    }
}
