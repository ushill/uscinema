package com.ushill.VO.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ushill.DTO.search.MovieSearchRefineResultDTO;
import com.ushill.DTO.search.UserSearchRefineResultDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefineSearchAllResultVO {
    @JsonProperty(value = "movies_by_name")
    private RefineSearchMovieResultVO movieSearchByName;

    @JsonProperty(value = "movies_by_people")
    private RefineSearchMovieResultVO movieSearchByPeople;

    @JsonProperty(value = "critics")
    private RefineSearchUserResultVO userSearchByCritic;

    @JsonProperty(value = "users")
    private RefineSearchUserResultVO userSearchByUser;
}
