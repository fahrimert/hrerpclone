package com.hrerp.dto.JobPostingDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class    JobPostingRequestRecruiterSpesificDTO {
    private Long internalJobId;


    private  String hiringManagerName;
    private  boolean isReplacement = false;
    private String replacementFor ;

    private String internalHrNote;

    private LocalDateTime internalPostingDate;

}
