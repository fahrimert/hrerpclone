package com.hrerp.dto;

import com.hrerp.model.RecruitmentProcess;
import com.hrerp.model.enums.InterviewProcesses;
import com.hrerp.model.enums.InterviewQuestions;
import com.hrerp.model.enums.InterviewScore;
import jakarta.validation.constraints.NotEmpty;
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
public class InterviewInitiateResponseDTO {
    @Size(min = 5,max = 100)
    private  Long candidateId;


    private InterviewProcesses interviewProcesses;
    private  String  interviewRatingQuote;
    private List<InterviewQuestions> interviewQuestions;
    private  String interviewerName;
    private InterviewScore interviewScore;
    private RecruitmentProcess process;


    private Date interviewScheduleTime;
    private Date createdAt ;
    private LocalDateTime lastUpdated;
}
