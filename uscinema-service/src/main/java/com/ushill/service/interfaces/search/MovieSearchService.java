package com.ushill.service.interfaces.search;

import com.ushill.DTO.search.MovieSearchRefineResultDTO;
import com.ushill.DTO.search.item.MovieFastSearchResItemDTO;

import java.util.List;

public interface MovieSearchService {

    public List<MovieFastSearchResItemDTO> fastSearch(List<String> paraList);

    public MovieSearchRefineResultDTO refineSearchByName(List<String> paraList, int page);

    public MovieSearchRefineResultDTO refineSearchByPeople(List<String> paraList, int page);
}
