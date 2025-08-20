package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.EvaluateCaseStudyDTO;

import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyResponseScoreDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyScoreDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationCaseStudyJsonDTO {
    String givenCaseSolutionEvaluation;
    String givenCasePresentationEvaluation;
    String risksIdentified;
    String candidateAnalyticThinkingNote;
    CaseStudyScoreDTO caseStudyScore;

}
