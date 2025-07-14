package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.InitiateCaseStudyDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitCaseStudyJsonDTO {
    private String givenCaseTitle;
    private String givenCaseContent;
    private String givenCaseDeadline;
    private String interviewerName;
}
