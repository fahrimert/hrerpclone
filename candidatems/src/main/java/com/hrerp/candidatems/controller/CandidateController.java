package com.hrerp.candidatems.controller;

import com.hrerp.candidatems.dto.ApiResponse;
import com.hrerp.candidatems.dto.CandidateRequestDTO;
import com.hrerp.candidatems.dto.CandidateResponseDTO;
import com.hrerp.candidatems.service.CandidateService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates")
public class CandidateController {
    private CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }


    @GetMapping
    public ResponseEntity<List<CandidateResponseDTO>> getAllCandidates(){
        return  candidateService.findAllCandidates();
    }

    @GetMapping("/{id}")
    public  ResponseEntity<CandidateResponseDTO> getCandidateById(@PathVariable Long id){
        return  candidateService.findCandidateById(id);
    }


    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponse> createCandidate(
            @RequestBody @Valid CandidateRequestDTO candidateRequestDTO
    ){
        return  candidateService.createCandidate(candidateRequestDTO);
    }

    @PutMapping("/{id}")
    @Transactional
    public  ResponseEntity<CandidateResponseDTO> updateCandidate(@PathVariable Long id,@RequestBody @Valid CandidateRequestDTO candidateRequestDTO){
        return  candidateService.updateCandidateById(id,candidateRequestDTO);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public   ResponseEntity<ApiResponse> deleteJobPosting(@PathVariable Long id){
        return  candidateService.deleteCandidate(id);
    }

//
//    @GetMapping
//    public ResponseEntity<List<CandidateResponseDTO>> getAllJobPostings(){
//        return  jobPostingService.findAllJobPostings();
//    }
}
