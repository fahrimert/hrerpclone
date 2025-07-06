package com.hrerp.candidatems.controller;


import com.hrerp.candidatems.dto.*;
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
    //burayı bitirdim 
    @PostMapping("/createApplication/{jobPostingId}")
    public  ResponseEntity<ApiResponse> createApplicationToJobPosting(@Valid @RequestBody  ApplicationRequestDTO applicationRequestDTO, @PathVariable Long jobPostingId){

       return  applicationService.createApplication(applicationRequestDTO,jobPostingId);

    }


        @GetMapping("/{jobId}/getApplications")
    public ResponseEntity getAllApplicationsBasedOnJobPosting(@PathVariable Long jobId){
        return  applicationService.getApplicationsBasedOnJobId(jobId);
    }

    @GetMapping("/{jobPostingId}/getApplication/{candidateId}")
    public  ResponseEntity<ApiResponse<?>>  getAllApplicationsBasedOnJobPosting(
            @PathVariable Long jobPostingId,
            @PathVariable Long candidateId){
        return  applicationService.getApplicationBasedOnJobId(jobPostingId,candidateId);
    }




}
