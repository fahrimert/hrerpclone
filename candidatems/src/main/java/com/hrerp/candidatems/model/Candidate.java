package com.hrerp.candidatems.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private  String firstName;
    private  String lastName;

    @Embedded
    private Address address;

    private List<String> skills;

    @Email
    private  String email;

    @Embedded
    private  Connections connections;

    private  String cvUrl;


    private String  createdAt ;


    @JsonIgnore
    @OneToMany(mappedBy = "candidate",cascade = CascadeType.ALL)
    private List<Applications> applications = new ArrayList<>();



}
