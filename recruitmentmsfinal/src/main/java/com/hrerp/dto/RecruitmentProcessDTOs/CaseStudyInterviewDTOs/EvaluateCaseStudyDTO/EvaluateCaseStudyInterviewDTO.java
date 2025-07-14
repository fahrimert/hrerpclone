package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.InitiateCaseStudyDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitiateCaseStudyInterviewDTO {

    String givenCaseUrl;
    String givenCaseTitle;
    String givenCaseContent;
    LocalDateTime givenCaseDeadline;
}
