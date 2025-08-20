package com.hrerp.dto.JobPostingDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class JobPostingResponseSpesificDTO {
    private Long internalJobId;


    private  String hiringManagerName;

    @JsonProperty("replacementFor")
    private  boolean isReplacement = false;

    private String replacementFor ;

    private String internalHrNote;

    private String internalPostingDate;
}
