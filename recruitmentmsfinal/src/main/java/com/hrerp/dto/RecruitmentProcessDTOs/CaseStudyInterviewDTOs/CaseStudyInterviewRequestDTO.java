package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs;

import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.EvaluateCaseStudyDTO.EvaluateCaseStudyInterviewDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.SolutionCaseStudyDTO.SolutionCaseStudyInterviewDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InterviewScoreDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.TechnicalKnowledgeScore;
import com.hrerp.model.enums.InterviewQuestions;
import com.hrerp.model.enums.InterviewScore;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

//case studylerin scorelarını ve scheduled timelarını girmeyi yapamadım garip bi şekilde kuramadım burayı bir türlü
public class CaseStudyInterviewRequestDTO {

    private  String interviewerName;
    private  Long candidateId;

    private  InterviewScoreDTO interviewScore;

    private  String interviewScheduleTime;


    @CreatedDate
    private Date createdAt ;
    private LocalDateTime lastUpdated;
}
