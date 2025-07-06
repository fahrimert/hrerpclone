package com.hrerp.candidatems.dto;


import com.hrerp.candidatems.model.Address;
import com.hrerp.candidatems.model.Connections;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRequestDTO {
    private  Long id;

    private  String firstName;
    private  String lastName;

    private Address address;
    private  String email;
    private Connections connections;

    private  String cvUrl;

    private LocalDateTime createdAt = LocalDateTime.now();


}
