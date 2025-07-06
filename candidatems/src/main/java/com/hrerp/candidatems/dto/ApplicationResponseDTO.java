package com.hrerp.candidatems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequestDTO {
    private  Long id;
    private LocalDate applicationDate;
    private  String appliedPosition;
    private String coverLetter;
    private  String applicationStatusName;
    private Long candidateId;


}
