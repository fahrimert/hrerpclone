package com.hrerp.candidatems.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrerp.candidatems.model.Address;
import com.hrerp.candidatems.model.Applications;
import com.hrerp.candidatems.model.Connections;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class CandidateResponseDTO {

    private  Long id;
    private  String firstName;
    private  String lastName;
    private Address address;
    private  String email;
    private  String linkedin_url;
    private  String instagram_url;
    private  String facebook_url;
    private  String phoneNumber;
    private  String cvUrl;
    private LocalDateTime createdAt = LocalDateTime.now();


}
