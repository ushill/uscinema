package com.ushill.controller.v1.rating;

import com.ushill.DTO.CheckRatingDTO;
import com.ushill.DTO.CommentSummaryDTO;
import com.ushill.interceptor.ScopeLevel;
import com.ushill.service.interfaces.RatingService;
import com.ushill.utils.BoolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/rating")
@Validated
public class RatingController {

    private RatingService ratingService;

    @Autowired
    public RatingController(@Qualifier("ratingServiceImpl") RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @ScopeLevel(4)
    @GetMapping("/check/{movieId}")
    public CheckRatingDTO checkRating(
            @PathVariable @Min(value = 10000000, message = "电影ID不合法")
            @Max(value = 200000000, message = "电影ID不合法") String movieId,
            @RequestParam(defaultValue = "1") String isCritic){

        return ratingService.checkMovieRating(Integer.parseInt(movieId),
                BoolUtils.trueOrFalse(isCritic, 30001));
    }

    @ScopeLevel(4)
    @GetMapping("/update/{movieId}")
    public Map<String, Integer> updateRating(
            @PathVariable @Min(value = 10000000, message = "电影ID不合法")
            @Max(value = 200000000, message = "电影ID不合法") String movieId,
            @RequestParam(defaultValue = "1") String isCritic){

        int res = ratingService.updateMovieRating(Integer.parseInt(movieId), BoolUtils.trueOrFalse(isCritic, 30001));
        Map<String, Integer> map = new HashMap<>();
        map.put("affected rows", res);
        return map;
    }

    @ScopeLevel(8)
    @GetMapping("/update/all")
    public String updateAll(){
        ratingService.updateAll();
        return "ok";
    }
}
