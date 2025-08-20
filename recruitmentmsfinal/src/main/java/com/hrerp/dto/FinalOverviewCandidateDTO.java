package com.hrerp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalOverviewCandidateDTO {
    private  CandidateResponseDTO candidateResponseDTO;
    private  Double HR_SCREENING;
    private  Double TECHNICAL;
    private  Double CASE_STUDY;
    private  Double INIT_CASE_STUDY;
    private  Double EVALUATION_CASE_STUDY;
    private  Double AVERAGE_SCORE;
}
