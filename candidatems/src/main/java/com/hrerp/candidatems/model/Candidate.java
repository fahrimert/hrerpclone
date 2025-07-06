package com.hrerp.candidatems.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
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

    @Email
    private  String email;

    @Embedded
    private  Connections connections;

    private  String cvUrl;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    @JsonIgnore
    @OneToMany(mappedBy = "candidate",cascade = CascadeType.ALL)
    private List<Applications> applications = new ArrayList<>();



}
