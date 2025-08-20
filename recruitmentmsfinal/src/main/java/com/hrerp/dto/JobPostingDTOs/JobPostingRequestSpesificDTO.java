package com.hrerp.dto.JobPostingDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class JobPostingRequestSpesificDTO {
    private Long internalJobId;


    private  String hiringManagerName;
    private  boolean isReplacement = false;
    private String replacementFor ;

    private String internalHrNote;
    private String internalPostingDate;

}
