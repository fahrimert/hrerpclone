package com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos;

import com.hrerp.dto.RecruitmentProcessDTOs.InterviewScoreDTO;
import com.hrerp.model.enums.InterviewProcesses;
import com.hrerp.model.enums.InterviewQuestions;
import jakarta.validation.constraints.NotNull;
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
public class InterviewInitiateRequestDTO {
    @Size(min = 5,max = 100)
    private  String  interviewRatingQuote;

    private InterviewScoreDTO interviewScore;

    @NotNull(message = "Candidate Id Cannot Be Null")
    private  Long candidateId ;
    private InterviewProcesses interviewProcesses;

    private  String interviewerName;
    private  String generalImpression;
    private String candidateTeamCompabilityNote;
    private List<InterviewQuestions> interviewQuestions;

    private Boolean locatedInTheSameCity;
    private Boolean candidateCanWorkInTheOffice;
    private String candidateCareerGoals;
    private String salaryExpectation;
    private String availabilityToStart;

    private String interviewScheduleTime;
    private Date createdAt ;
    private LocalDateTime lastUpdated;
}
