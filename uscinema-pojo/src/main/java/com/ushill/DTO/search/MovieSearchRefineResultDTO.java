package com.ushill.DTO.search;

import com.ushill.DTO.search.item.MovieRefineSearchResItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearchRefineResultDTO {
    private List<MovieRefineSearchResItemDTO> movieResList;
    private boolean hasNext;
}
