package com.ushill.mapper;

import com.ushill.DTO.MovieSummaryDTO;
import com.ushill.my.mapper.MyMapper;
import com.ushill.models.ComingSoon;

import java.util.List;

public interface ComingSoonMapper extends MyMapper<ComingSoon> {
    public List<MovieSummaryDTO> getComingSoonSummary();
}