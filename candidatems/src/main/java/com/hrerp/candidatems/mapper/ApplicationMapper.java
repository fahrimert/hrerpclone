package com.hrerp.candidatems.mapper;


import com.hrerp.candidatems.dto.*;
import com.hrerp.candidatems.model.ApplicationStatus;
import com.hrerp.candidatems.model.Applications;
import com.hrerp.candidatems.model.Candidate;
import com.hrerp.candidatems.model.Connections;
import com.hrerp.candidatems.repository.CandidateRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApplicationMapper {

    private CandidateRepository candidateRepository ;

    public ApplicationMapper(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public Applications toApplication(@Valid ApplicationRequestDTO applicationRequestDTO, Long jobPostingId , Candidate candidate,String appliedPositionName){

        return  Applications.builder()
                .applicationDate(applicationRequestDTO.getApplicationDate())
                .appliedPosition(appliedPositionName)
                .coverLetter(applicationRequestDTO.getCoverLetter())
                .applicationStatus(applicationRequestDTO.getApplicationStatus())
                .candidate(candidate)
                .jobPostingId(jobPostingId)
                .build();
    }

    public ApplicationResponseDTO fromApplication(Applications applications){
        return  new ApplicationResponseDTO(
                applications.getId(),
                applications.getApplicationDate(),
                applications.getAppliedPosition(),
                applications.getCoverLetter(),
                applications.getApplicationStatus(),
                applications.getCandidate()
        );
    }

    public ApplicationsOnSpesificJobPostingDTO fromApplicationAndCandidates(Applications applications,Candidate candidate){
        return  new ApplicationsOnSpesificJobPostingDTO(
                applications.getId(),
                applications.getApplicationDate(),
                applications.getCandidate().getId(),
                applications.getCandidate().getFirstName() + applications.getCandidate().getLastName(),
                applications.getCandidate().getEmail()
        );
    }


}
