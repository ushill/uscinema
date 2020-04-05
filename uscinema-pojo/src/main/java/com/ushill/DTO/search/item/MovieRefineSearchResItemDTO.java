package com.ushill.DTO.search.item;

import com.ushill.enums.RefineSearchType;
import com.ushill.models.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieRefineSearchResItemDTO {
    private Movie movie;
    private RefineSearchType searchType;
    private List<String> paraList;
}