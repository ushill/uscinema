package com.ushill.DTO.search.item;

import com.ushill.enums.RefineSearchType;
import com.ushill.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRefineSearchResItemDTO {
    private User user;
    private RefineSearchType searchType;
    private List<String> paraList;
}