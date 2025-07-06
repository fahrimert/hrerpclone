package com.hrerp.candidatems.mapper;


import com.hrerp.candidatems.dto.CandidateRequestDTO;
import com.hrerp.candidatems.dto.CandidateResponseDTO;
import com.hrerp.candidatems.model.Candidate;
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
                .connections(candidateRequestDTO.getConnections())
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
                candidate.getConnections(),
                candidate.getCvUrl(),
                candidate.getCreatedAt()
        );
    }


}
