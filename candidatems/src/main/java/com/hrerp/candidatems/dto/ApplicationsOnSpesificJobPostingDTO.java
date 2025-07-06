package com.hrerp.candidatems.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ApplicationsOnSpesificJobPostingDTO {
    private  Long applicationId;
    private LocalDate applicationDate;
    private Long candidateId;
    private  String candidateFullName;
    private  String candidateEmail;
}
