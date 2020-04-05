package com.ushill.service.interfaces;

import com.ushill.DTO.CheckRatingDTO;
import com.ushill.DTO.CommentRatioDTO;

public interface RatingService {

    public CheckRatingDTO checkMovieRating(int movieId, boolean isCritic);

    public int updateMovieRating(int movieId, boolean isCritic);

    public void updateAll();
}
