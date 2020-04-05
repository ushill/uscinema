package com.ushill.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MovieSearchMatchType {
    TITLE(1, "title"),
    NICKNAME(2, "nickname"),
    DIRECTORS(3, "directorsName"),
    ACTORS(4, "actorsName");

    private int matchType;
    private String propertyName;
}
