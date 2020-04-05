package com.ushill.service.interfaces.search;

import com.ushill.DTO.search.UserSearchRefineResultDTO;
import com.ushill.DTO.search.item.UserFastSearchResItemDTO;

import java.util.List;

public interface UserSearchService {

    public List<UserFastSearchResItemDTO> fastSearch(List<String> paraList, boolean isCritic);

    public UserSearchRefineResultDTO refineSearchCritics(List<String> paraList, int page);

    public UserSearchRefineResultDTO refineSearchUsers(List<String> paraList, int page);

}
