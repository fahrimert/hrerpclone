package com.hrerp.candidatems.service;

import com.hrerp.candidatems.dto.ApiResponse;
import com.hrerp.candidatems.dto.CandidateRequestDTO;
import com.hrerp.candidatems.dto.CandidateResponseDTO;
import com.hrerp.candidatems.mapper.CandidateMapper;
import com.hrerp.candidatems.model.Candidate;
import com.hrerp.candidatems.model.Connections;
import com.hrerp.candidatems.repository.CandidateRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CandidateService implements CandidateServiceImpl {

    private  final CandidateRepository candidateRepository;
    private  final CandidateMapper candidateMapper;

    public CandidateService(CandidateRepository candidateRepository, CandidateMapper candidateMapper) {
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
    }


    public ResponseEntity<List<CandidateResponseDTO>> findAllCandidates() {
        return  ResponseEntity.ok(candidateRepository.findAll()
                .stream()
                .map(candidateMapper::fromCandidate)
                .collect(Collectors.toList()));
    }

    @Transactional
    public ResponseEntity<ApiResponse> createCandidate(@Valid CandidateRequestDTO candidateRequestDTO) {
        Candidate candidate =candidateMapper.toCandidate(candidateRequestDTO);
        candidateRepository.save(candidate);
return  ResponseEntity.status(HttpStatus.ACCEPTED)
        .body(ApiResponse.success(candidate.getFirstName()));
    }

    @Override
    public ResponseEntity<CandidateResponseDTO> findCandidateById(Long id) {
   return  ResponseEntity.ok(candidateMapper.fromCandidate( candidateRepository.findById(id).orElse(null)));

    }

    @Override
    public ResponseEntity<CandidateResponseDTO> updateCandidateById(Long id,CandidateRequestDTO updatedCandidate) {
        Candidate candidate = candidateRepository.findById(id).orElse(null);
         candidate.setFirstName(updatedCandidate.getFirstName());
        candidate.setAddress(updatedCandidate.getAddress());
        candidate.setEmail(updatedCandidate.getEmail());
        candidate.setConnections(
                Connections.builder()
                        .linkedinUrl(updatedCandidate.getLinkedin_url())
                        .instagramUrl(updatedCandidate.getInstagram_url())
                        .facebookUrl(updatedCandidate.getFacebook_url())
                        .build());
        candidate.setCvUrl(updatedCandidate.getCvUrl());
        candidate.setCreatedAt(updatedCandidate.getCreatedAt());

       ResponseEntity<CandidateResponseDTO> candidateResponse =
               ResponseEntity.ok( candidateMapper.fromCandidate(candidate));
    return  candidateResponse;
    }

    @Override
    public  ResponseEntity<ApiResponse> deleteCandidate(Long id) {
        Optional<Candidate> jobPosting = candidateRepository.findById(id);

        if (jobPosting.isPresent()){

            candidateRepository.deleteById(id);

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            "Delete candidate succesfully applied",
                            null,
                            HttpStatus.CONFLICT
                    ));


        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            "Candidate does not exists",
                            null,
                            HttpStatus.CONFLICT
                    ));
        }
    }


}
