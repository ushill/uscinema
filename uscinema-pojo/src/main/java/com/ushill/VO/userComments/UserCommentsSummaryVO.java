package com.ushill.VO.userComments;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ushill.DTO.UserCommentSummaryDTO;
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
public class UserCommentsSummaryVO {
    @JsonProperty(value = "comments")
    List<UserCommentSummaryDTO> userCommentsItems;

    @JsonProperty(value = "total")
    int totalSize;

    @JsonProperty(value = "has_next")
    boolean hasNext;

    @JsonProperty(value = "per_page")
    int perPageCount;

    public UserCommentsSummaryVO(PagedGridResult<UserCommentSummaryDTO> paged){
        this.userCommentsItems = paged.getRows();
        this.hasNext = paged.getPage() < paged.getTotal();
        this.perPageCount = paged.getPer();
        this.totalSize = (int)paged.getRecords();
    }
}
