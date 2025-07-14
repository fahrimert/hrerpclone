package com.hrerp.dto;

import com.hrerp.model.Interview;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class RecruitmentProcessInitiateRequestDTO {

    @NotNull(message = "Candidate Id Cant Be Null")
    private  Long candidateId;
    @NotNull(message = "Job Posting Id Cant Be Null")
    private  Long jobPostingId;
    @NotNull(message = "Initial Interview Request Cant Be Null")
    @NotEmpty(message = "Initial Interview Request Cannot Be Empty")
    private List<InterviewInitiateRequestDTO> interviews;
    @CreatedDate
    private Date createdAt ;
    private LocalDateTime lastUpdated;

}
