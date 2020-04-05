package com.ushill.VO.userComments;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ushill.DTO.UserCommentDTO;
import com.ushill.utils.PagedGridResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCommentsVO {
    @JsonProperty(value = "comments")
    List<UserCommentDTO> userCommentsItems;

    @JsonProperty(value = "total")
    int totalSize;

    @JsonProperty(value = "has_next")
    boolean hasNext;

    @JsonProperty(value = "per_page")
    int perPageCount;

    public UserCommentsVO(PagedGridResult<UserCommentDTO> paged){
        this.userCommentsItems = paged.getRows();
        this.hasNext = paged.getPage() < paged.getTotal();
        this.perPageCount = paged.getPer();
        this.totalSize = (int)paged.getRecords();
    }
}
