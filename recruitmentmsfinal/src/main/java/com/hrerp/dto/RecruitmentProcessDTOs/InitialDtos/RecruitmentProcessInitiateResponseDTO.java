package com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitmentProcessInitiateResponseDTO {

    private  Long candidateId;
    private  Long jobPostingId;
    private List<InterviewInitiateResponseDTO> interviews;
    private Date createdAt ;
    private LocalDateTime lastUpdated;

}
