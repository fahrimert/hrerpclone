package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.InitiateCaseStudyDTO;

import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyScore;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyScoreDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InterviewScoreDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitialCaseStudyInterviewDataDTO {

    @Null
    String givenCaseUrl;
    @NotNull(message = "Interviewer Name Cant Be null")
    String interviewerName;
    @NotNull(message = "Case Study  Title Cant Be null")
    String givenCaseTitle;
    @NotNull(message = "Case Study Content Cant Be null")
    String givenCaseContent;

    @NotNull(message = "Initial Case Study Schedule Time Cant Be null")
    private String interviewScheduleTime;

    private CaseStudyScoreDTO caseStudyScore;


    @NotNull(message = "Case Deadline Cant Be null")
    String givenCaseDeadline;
}
