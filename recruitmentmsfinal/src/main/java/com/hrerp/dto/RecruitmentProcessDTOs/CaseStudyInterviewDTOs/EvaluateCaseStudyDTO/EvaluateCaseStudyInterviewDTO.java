package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.EvaluateCaseStudyDTO;

import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyScore;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyScoreDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluateCaseStudyInterviewDTO {

    String givenCaseSolutionEvaluation;
    @NotNull(message = "Case Study Presentation Evaluatıon Must Not Be Null ")
    String givenCasePresentationEvaluation;
    String risksIdentified;
    private CaseStudyScoreDTO caseStudyScore;

    @NotNull(message = "Case Study Candidate Analythical Thinking Note Must Not Be Null ")
    String candidateAnalyticThinkingNote;
    String interviewScheduleTime;
    String interviewerName;

}
