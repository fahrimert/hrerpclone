package com.hrerp.candidatems.mapper;


import com.hrerp.candidatems.dto.CandidateRequestDTO;
import com.hrerp.candidatems.dto.CandidateResponseDTO;
import com.hrerp.candidatems.model.Candidate;
import com.hrerp.candidatems.model.Connections;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class CandidateMapper {
    public Candidate toCandidate(@Valid CandidateRequestDTO candidateRequestDTO){
        return  Candidate.builder()
                .firstName(candidateRequestDTO.getFirstName())
                .lastName(candidateRequestDTO.getLastName())
                .address(candidateRequestDTO.getAddress())
                .email(candidateRequestDTO.getEmail())
                .connections(Connections.builder()
                        .linkedinUrl(candidateRequestDTO.getLinkedin_url())
                        .instagramUrl(candidateRequestDTO.getInstagram_url())
                        .facebookUrl(candidateRequestDTO.getFacebook_url())
                        .phoneNumber(candidateRequestDTO.getPhoneNumber())
                        .build())
                .cvUrl(candidateRequestDTO.getCvUrl())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public CandidateResponseDTO fromCandidate(Candidate candidate){
        return  new CandidateResponseDTO(
                candidate.getId(),
                candidate.getFirstName(),
                candidate.getLastName(),
                candidate.getAddress(),
                candidate.getEmail(),
                candidate.getConnections().getLinkedinUrl(),
                candidate.getConnections().getInstagramUrl(),
                candidate.getConnections().getFacebookUrl(),
                candidate.getConnections().getPhoneNumber(),
                candidate.getCvUrl(),
                candidate.getCreatedAt()
        );
    }


}
