package com.ushill.mapper;

import com.ushill.DTO.MovieSummaryDTO;
import com.ushill.my.mapper.MyMapper;
import com.ushill.models.Nowplaying;

import java.util.List;

public interface NowplayingMapper extends MyMapper<Nowplaying> {
    public List<MovieSummaryDTO> getNowplayingSummary();
}