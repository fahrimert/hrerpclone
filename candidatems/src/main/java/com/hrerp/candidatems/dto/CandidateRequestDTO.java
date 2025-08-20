package com.hrerp.candidatems.dto;


import com.hrerp.candidatems.model.Address;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRequestDTO {
    @NotNull(message = "First name cannot be null")
    private  String firstName;
    @NotNull(message = "Last name cannot be null")
    private  String lastName;

    private Address address;
    @Email(message = "Email should be valid")
    private  String email;
    @NotNull
    private  String linkedin_url;
    @NotNull
    private  String instagram_url;
    @NotNull
    private  String facebook_url;
    @NotNull
    private  String phoneNumber;
    @NotNull
    private  String cvUrl;
    private String createdAt ;


}
