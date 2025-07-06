package com.hrerp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPostingRequestRecruiterSpesificDTO {
    private Long internalJobId;
    @NotNull(message = "Job Description should be present")
    @NotEmpty(message = "Job Description  should be present")
    @NotBlank(message = "Job Description should be present")

    @Size(min = 6,max = 50, message = "İnternal Description must be between 6 and 50 characterss")
    private  String internalDescripton;

    private  String hiringManagerName;
    private  boolean isReplacement = false;
    private String replacementFor ;

    private String internalHrNote;

    @CreatedDate
    private LocalDate internalPostingDate;}
