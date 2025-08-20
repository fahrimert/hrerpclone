package com.hrerp.candidatems.controller;


import com.hrerp.candidatems.dto.*;
import com.hrerp.candidatems.model.ApplicationStatus;
import com.hrerp.candidatems.model.Candidate;
import com.hrerp.candidatems.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    private ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/createApplication/{jobPostingId}")
    public  ResponseEntity<ApiResponse> createApplicationToJobPosting(@Valid @RequestBody  ApplicationRequestDTO applicationRequestDTO, @PathVariable Long jobPostingId){

       return  applicationService.createApplication(applicationRequestDTO,jobPostingId);

    }


            @GetMapping("/{jobId}/getApplications")
        public ResponseEntity<ApiResponse> getAllApplicationsBasedOnJobPosting(@PathVariable Long jobId){
            return  applicationService.getApplicationsBasedOnJobId(jobId);
        }

    @GetMapping("/{jobPostingId}/getApplication/{candidateId}")
    public  ResponseEntity<ApiResponse<?>>  getApplicationBasedOnJobPosting(
            @PathVariable Long jobPostingId,
            @PathVariable Long candidateId){
        return  applicationService.getApplicationBasedOnJobId(jobPostingId,candidateId);
    }


        @GetMapping("/{jobPostingId}/getTheProperCandidates")
    public  ResponseEntity<ApiResponse>   getTheProperCandidates(@PathVariable Long jobPostingId){
        return  applicationService.getTheProperCandidates(jobPostingId);
    }

            @PutMapping("/{candidateId}/updateTheCandidateApplicationStatus")
    public  ResponseEntity<ApiResponse>   updateTheCandidateApplicationStatus(@PathVariable Long candidateId, @RequestBody ApplicationStatusUpdateDTO  applicationStatus){
        return  applicationService.updateTheCandidateApplicationStatus(candidateId,applicationStatus.getApplicationStatus());
    }



}
