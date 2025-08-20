package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs;

import com.hrerp.model.RecruitmentProcess;
import com.hrerp.model.enums.InterviewQuestions;
import com.hrerp.model.enums.InterviewScore;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseStudyResponseDTO {
    @Size(min = 5,max = 100)
    private  Long candidateId;

    private  String interviewerName;


    private Date interviewScheduleTime;
    private Date createdAt ;
    private LocalDateTime lastUpdated;
}
