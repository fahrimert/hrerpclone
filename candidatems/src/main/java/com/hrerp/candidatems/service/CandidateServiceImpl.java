package com.hrerp.candidatems.service;

import com.hrerp.candidatems.dto.ApiResponse;
import com.hrerp.candidatems.dto.CandidateRequestDTO;
import com.hrerp.candidatems.dto.CandidateResponseDTO;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CandidateServiceImpl {

    ResponseEntity<List<CandidateResponseDTO>> findAllCandidates();

    ResponseEntity<ApiResponse> createCandidate(CandidateRequestDTO candidateRequestDTO);

    ResponseEntity<CandidateResponseDTO> findCandidateById(Long id);

    ResponseEntity<CandidateResponseDTO> updateCandidateById(Long id, CandidateRequestDTO updatedCandidate);

    ResponseEntity<ApiResponse> deleteCandidate(Long id);

}
