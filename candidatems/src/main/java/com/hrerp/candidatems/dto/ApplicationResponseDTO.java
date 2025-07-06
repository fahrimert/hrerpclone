package com.hrerp.candidatems.dto;

import com.hrerp.candidatems.model.ApplicationStatus;
import com.hrerp.candidatems.model.Candidate;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@AllArgsConstructor
public class ApplicationResponseDTO {
    private  Long applicationId;
    private LocalDate applicationDate;
    private  String appliedPosition;
    private String coverLetter;
    private ApplicationStatus applicationStatusName;
    private Candidate candidate;


}
