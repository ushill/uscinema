package com.ushill.DTO.search.item;

import com.ushill.enums.MovieSearchMatchType;
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
public class MovieFastSearchResItemDTO {
    private Movie movie;
    private MovieSearchMatchType matchType;
    private List<String> paraList;
}
