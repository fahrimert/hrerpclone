package com.hrerp.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class   CandidateResponseDTO {

    private  Long id;
    private  String firstName;
    private  String lastName;
    private com.hrerp.dto.enums.Address address;
    private  String email;
    private  String linkedin_url;
    private  String instagram_url;
    private  String facebook_url;
    private  String phoneNumber;
    private  String cvUrl;
    private String createdAt ;


}
