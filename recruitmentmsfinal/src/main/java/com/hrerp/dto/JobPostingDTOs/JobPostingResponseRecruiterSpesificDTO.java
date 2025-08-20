package com.hrerp.dto.JobPostingDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class JobPostingResponseRecruiterSpesificDTO {

    private Long internalJobId;


    private  String hiringManagerName;

    @JsonProperty("replacementFor")
    private  boolean isReplacement = false;

    private String replacementFor ;

    private String internalHrNote;

    @CreatedDate
    private LocalDateTime internalPostingDate;
}
