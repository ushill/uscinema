package com.ushill.VO.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ushill.DTO.search.item.UserRefineSearchResItemDTO;
import com.ushill.DTO.search.UserSearchRefineResultDTO;
import com.ushill.VO.search.item.UserRefineSearchItemVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefineSearchUserResultVO {

    List<UserRefineSearchItemVO> data = new ArrayList<>();

    @JsonProperty(value = "has_next")
    boolean hasNext;

    public RefineSearchUserResultVO(UserSearchRefineResultDTO resultDTO){
        List<UserRefineSearchResItemDTO> userList = resultDTO.getUserResList();
        userList.forEach(user -> {
            UserRefineSearchItemVO userRes = new UserRefineSearchItemVO();
            BeanUtils.copyProperties(user.getUser(), userRes);
            BeanUtils.copyProperties(user, userRes);
            this.data.add(userRes);
        });
        this.hasNext = resultDTO.isHasNext();
    }
}
