package com.hrerp.candidatems.service;

import com.hrerp.candidatems.dto.*;
import com.hrerp.candidatems.model.ApplicationStatus;
import com.hrerp.candidatems.model.Candidate;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ApplicationServiceImpl {

//    ResponseEntity<List<CandidateResponseDTO>> findAllCandidates();

    ResponseEntity<ApiResponse> createApplication(ApplicationRequestDTO applicationRequestDTO,Long jobPostingId);

    ResponseEntity  getApplicationsBasedOnJobId(Long jobPostingId);

    ResponseEntity<ApiResponse<?>> getApplicationBasedOnJobId(Long jobId,Long candidateId);

    ResponseEntity getTheProperCandidates(Long jobPostingId);

    ResponseEntity<ApiResponse> updateTheCandidateApplicationStatus(Long candidateId, ApplicationStatus applicationStatusRequest);
//
//    ResponseEntity<CandidateResponseDTO> findCandidateById(Long id);
//
//    ResponseEntity<CandidateResponseDTO> updateCandidateById(Long id, CandidateRequestDTO updatedCandidate);
//
//    ResponseEntity<ApiResponse> deleteCandidate(Long id);

}
