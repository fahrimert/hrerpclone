package com.hrerp.dto;

import com.hrerp.model.enums.InterviewProcesses;
import com.hrerp.model.enums.InterviewQuestions;
import com.hrerp.model.enums.InterviewScore;
import jakarta.validation.constraints.NotNull;
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
public class InterviewInitiateRequestDTO {
    @Size(min = 5,max = 100)
    private  String  interviewRatingQuote;

    private InterviewScoreDTO interviewScore;

    @NotNull(message = "Candidate Id Cannot Be Null")
    private  Long candidateId ;
    private InterviewProcesses interviewProcesses;


    private Date interviewScheduleTime;
    private Date createdAt ;
    private LocalDateTime lastUpdated;
}
