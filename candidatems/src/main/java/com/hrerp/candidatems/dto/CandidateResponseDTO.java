package com.hrerp.candidatems.dto;


import com.hrerp.candidatems.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateResponseDTO {

    private  Long id;
    private  String firstName;
    private  String lastName;
    private Address address;
    private  String email;
    private  String linkedin_url;
    private List<String> skills;
    private  String instagram_url;
    private  String facebook_url;
    private  String phoneNumber;
    private  String cvUrl;
    private String createdAt;


}
