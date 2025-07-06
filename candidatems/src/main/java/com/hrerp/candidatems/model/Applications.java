package com.hrerp.candidatems.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Applications {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;


    private  LocalDate applicationDate;
    private  String appliedPosition;
    private String coverLetter;


    @Enumerated(EnumType.STRING)
    private  ApplicationStatus applicationStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private  Candidate candidate;

    private  Long jobPostingId;
}
