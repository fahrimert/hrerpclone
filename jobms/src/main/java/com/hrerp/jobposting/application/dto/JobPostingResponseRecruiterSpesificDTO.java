package com.hrerp.jobposting.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPostingResponseRecruiterSpesificDTO {

    private Long internalJobId;


    private  String hiringManagerName;
    private  boolean isReplacement = false;
    private String replacementFor ;

    private String internalHrNote;

    @CreatedDate
    private LocalDate internalPostingDate;
}
