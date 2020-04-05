package com.ushill.controller.v1.search;

import com.ushill.DTO.search.item.MovieFastSearchResItemDTO;
import com.ushill.DTO.search.item.UserFastSearchResItemDTO;
import com.ushill.VO.search.FastSearchResultVO;
import com.ushill.VO.search.RefineSearchAllResultVO;
import com.ushill.VO.search.RefineSearchMovieResultVO;
import com.ushill.VO.search.RefineSearchUserResultVO;
import com.ushill.exception.http.ParameterException;
import com.ushill.service.interfaces.search.MovieSearchService;
import com.ushill.service.interfaces.search.UserSearchService;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/search")
@Validated
public class SearchController {

    private MovieSearchService movieSearchService;
    private UserSearchService userSearchService;

    @Autowired
    public SearchController(@Qualifier("movieSearchServiceImpl") MovieSearchService movieSearchService,
                            @Qualifier("userSearchServiceImpl") UserSearchService userSearchService) {
        this.movieSearchService = movieSearchService;
        this.userSearchService = userSearchService;
    }

    @GetMapping("/fast")
    public FastSearchResultVO fastSearch(
            @RequestParam @Min(0) String seq,
            @RequestParam @NotBlank(message = "请输入搜索内容") @Length(max = 64) String para){

        List<String> paraList = Arrays.asList(para.split(" "));
        List<MovieFastSearchResItemDTO> movieList = movieSearchService.fastSearch(paraList);
        List<UserFastSearchResItemDTO> criticList = userSearchService.fastSearch(paraList, true);
        List<UserFastSearchResItemDTO> userList = userSearchService.fastSearch(paraList, false);

        return new FastSearchResultVO(Integer.parseInt(seq), movieList, criticList, userList, paraList);
    }

    @GetMapping("/refine")
    public Object refineSearch(
            @RequestParam(defaultValue = "1") @Range(min = 1, max = 5) String type,
            @RequestParam(defaultValue = "1") @Range(min = 1, max = 25) String page,
            @RequestParam @NotBlank(message = "请输入搜索内容")  @Length(max = 64) String para){

        List<String> paraList = Arrays.stream(para.split(" "))
                .map(String::trim)
                .filter(s -> {return s.length()>0;})
                .collect(Collectors.toList());

        switch (Integer.parseInt(type)){
            case 1:
                RefineSearchMovieResultVO movieSearchByName =
                        new RefineSearchMovieResultVO(movieSearchService.refineSearchByName(paraList, Integer.parseInt(page)));
                RefineSearchMovieResultVO movieSearchByPeople =
                        new RefineSearchMovieResultVO(movieSearchService.refineSearchByPeople(paraList, Integer.parseInt(page)));
                RefineSearchUserResultVO userSearchByCritic =
                        new RefineSearchUserResultVO(userSearchService.refineSearchCritics(paraList, Integer.parseInt(page)));
                RefineSearchUserResultVO userSearchByUser =
                        new RefineSearchUserResultVO(userSearchService.refineSearchUsers(paraList, Integer.parseInt(page)));
                RefineSearchAllResultVO refineSearchAllResultVO = new RefineSearchAllResultVO(movieSearchByName, movieSearchByPeople, userSearchByCritic, userSearchByUser);
                return refineSearchAllResultVO;

            case 2:
                return new RefineSearchMovieResultVO(movieSearchService.refineSearchByName(paraList, Integer.parseInt(page)));
            case 3:
                return new RefineSearchMovieResultVO(movieSearchService.refineSearchByPeople(paraList, Integer.parseInt(page)));
            case 4:
                return new RefineSearchUserResultVO(userSearchService.refineSearchCritics(paraList, Integer.parseInt(page)));
            case 5:
                return new RefineSearchUserResultVO(userSearchService.refineSearchUsers(paraList, Integer.parseInt(page)));
            default:
                throw new ParameterException(10001);
        }
    }

}
