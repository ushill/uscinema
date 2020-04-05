package com.ushill.DTO.search.item;

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
public class UserFastSearchResItemDTO {
    private User user;
    private List<String> paraList;
}

