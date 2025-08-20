package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.SolutionCaseStudyDTO;

import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyScore;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyScoreDTO;
import com.hrerp.model.RecruitmentProcess;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolutionCaseStudyInterviewDTO {


    @NotNull(message = "Case Study Solution Title Must Not Be Null ")
    String caseStudySolutionTitle;
    @NotNull(message = "Case Study Description Must Not Be Null ")
    String caseStudySolutionDescriptino;

    private CaseStudyScoreDTO caseStudyScore;

    String interviewScheduleTime;
    String interviewerName;



}
