package com.ushill.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum RefineSearchType {
    ALL(1, "all"),
    MOVIES_BY_NAME(2, "movies_by_name"),
    MOVIES_BY_PEOPLE(3, "movies_by_people"),
    CRITICS(4, "critics"),
    USERS(5, "users");

    private int matchType;
    private String matchTypeName;
}
