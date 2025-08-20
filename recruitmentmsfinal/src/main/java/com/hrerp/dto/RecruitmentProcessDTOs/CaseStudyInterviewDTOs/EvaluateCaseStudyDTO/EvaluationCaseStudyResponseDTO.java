package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.EvaluateCaseStudyDTO;

import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyScore;
import com.hrerp.model.RecruitmentProcess;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationCaseStudyResponseDTO {
    @Size(min = 5,max = 100)
    private  Long candidateId;

    private  String interviewerName;
    private RecruitmentProcess process;

    String givenCaseSolutionEvaluation;
    String givenCasePresentationEvaluation;
    String risksIdentified;
    String candidateAnalyticThinkingNote;
     Double caseStudyScore;


    private Date interviewScheduleTime;
    private Date createdAt ;
    private LocalDateTime lastUpdated;
}
