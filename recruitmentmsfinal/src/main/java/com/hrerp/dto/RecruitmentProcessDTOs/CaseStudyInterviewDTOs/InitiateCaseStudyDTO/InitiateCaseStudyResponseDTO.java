package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.InitiateCaseStudyDTO;

import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyProcesses;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyScore;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyScoreDTO;
import com.hrerp.model.RecruitmentProcess;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitiateCaseStudyResponseDTO {
    @Size(min = 5,max = 100)
    private  Long candidateId;

    private  String interviewerName;
    private CaseStudyProcesses process;
    private  String givenCaseTitle;
    private Double caseStudyScore;
    private  String givenCaseContent;
    private  String givenCaseDeadline;
    private Date interviewScheduleTime;
    private Date createdAt ;
    private LocalDateTime lastUpdated;
}
