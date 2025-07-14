package com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs;

import com.hrerp.model.enums.InterviewQuestions;
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
public class InterviewTechnicalInterviewRequestDTO {

    private  String interviewerName;
    private  Long candidateId;

    @Size(min = 5,max = 100)
    private  String  interviewRatingQuote;
    private InterviewScoreDTO interviewScore;


    String codeExerciseUrl;
    Integer codeQualityScore;
    String technicalNotes;
    String candidateTechnicalBackgroundNote;
    TechnicalKnowledgeScore technicalKnowledgeScore;

    private List<InterviewQuestions> interviewQuestions;

    private  Date interviewScheduleTime;
    @CreatedDate
    private Date createdAt ;
    private LocalDateTime lastUpdated;
}
