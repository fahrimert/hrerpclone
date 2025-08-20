package com.hrerp.candidatems.service;

import com.hrerp.candidatems.dto.ApiResponse;
import com.hrerp.candidatems.dto.CandidateRequestDTO;
import com.hrerp.candidatems.dto.CandidateResponseDTO;
import com.hrerp.candidatems.dto.CounterOfferCandidateDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface CandidateServiceImpl {

    ResponseEntity<ApiResponse> findAllCandidates();

    ResponseEntity<ApiResponse> createCandidate(@Valid CandidateRequestDTO candidateRequestDTO);

    ResponseEntity<ApiResponse> findCandidateById(Long id);

    ResponseEntity<ApiResponse> updateCandidateById(Long id, CandidateRequestDTO updatedCandidate);

    ResponseEntity<ApiResponse> deleteCandidate(Long id);

    boolean candidateExistsById(Long id);

    ResponseEntity<ApiResponse> getMyOffers(Long candidateId);


    ResponseEntity<ApiResponse> getMyInduvualOffer(Long candidateId);


    ResponseEntity<ApiResponse> candidateMakeCounterOffer(CounterOfferCandidateDTO counterOfferDTO , Long candidateId);


}
