package com.ushill.DTO.search;

import com.ushill.DTO.search.item.UserRefineSearchResItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchRefineResultDTO {
    private List<UserRefineSearchResItemDTO> userResList;
    private boolean hasNext;
}
