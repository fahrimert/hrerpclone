package com.hrerpclone.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ApplicationsDTO {
    private  Long applicationId;
    private LocalDate applicationDate;
    private Long candidateId;
    private  String candidateFullName;
    private  String candidateEmail;
    private  String coverLetter;
    private  String candidateCity;
    private String candidateCountry;
    private  String candidateAddress;
    private  String linkedinUrl;
    private String instagramUrl;
    private String facebookUrl;
    private String phoneNumber;
    private  String cvUrl;
}
