package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.InitiateDTO;

import com.hrerp.model.enums.InterviewScore;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitiateCaseStudyInterviewDTO {

    String givenCaseUrl;
    String givenCaseTitle;
    String givenCaseContent;
    LocalDateTime givenCaseDeadline;
}
