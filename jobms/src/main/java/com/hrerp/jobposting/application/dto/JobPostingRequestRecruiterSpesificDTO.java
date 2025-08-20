package com.hrerp.jobposting.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class    JobPostingRequestRecruiterSpesificDTO {

    @NotNull(message = "Internal Id must not be null")
    private Long internalJobId;

    @NotBlank(message = "Hiring manager name cannot be blank")
    private  String hiringManagerName;
    private  boolean isReplacement = false;
    private String replacementFor ;

    @Size(max = 200, message = "HR note can be at most 200 characters")
    private String internalHrNote;

    @NotNull(message = "Internal Posting Date is required")
    private LocalDate internalPostingDate;}
