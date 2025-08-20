package com.hrerp.jobposting.application.controller;

import com.hrerp.jobposting.application.dto.*;
import com.hrerp.jobposting.application.service.JobPostingService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/jobPostings")
public class    JobPostingController {

        private  final JobPostingService jobPostingService;

        public JobPostingController(JobPostingService    jobPostingService) {
            this.jobPostingService = jobPostingService;
        }


        @GetMapping
        public ResponseEntity<List<JobPostingResponseDTO>> getAllJobPostings(){
            return  jobPostingService.findAllJobPostings();
        }

        //sorun olursa burada olur
        @GetMapping("/{id}")
        public  ResponseEntity<?> getJobById(@PathVariable Long id){
            return  jobPostingService.findJobById(id);
        }


        @PostMapping
        @Transactional
        public ResponseEntity<ApiResponse> createJobPosting(
                @RequestBody @Valid JobPostingRequestDTO jobPostingRequestDTO
                ){
            return  jobPostingService.createJobPosting(jobPostingRequestDTO);
        }

        @PutMapping("/{id}")
    @Transactional
    public  ResponseEntity<JobPostingResponseDTO> updateJobPosting(@PathVariable Long id,@RequestBody @Valid JobPostingRequestDTO jobPostingRequestDTO){
        return  jobPostingService.updateJobById(id,jobPostingRequestDTO);
    }


    @PutMapping("/{id}/incrementApplication")
    @Transactional
    public  ResponseEntity  incrementApplication(@PathVariable Long id){
         return jobPostingService.incrementApplication(id);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public  ResponseEntity<ApiResponse> deleteJobPosting(@PathVariable Long id){
     return   jobPostingService.deleteJobPosting(id);
    }

    @GetMapping("/{jobId}/getJobTitle")
    @Transactional
    public  String  getJobTitle(@PathVariable Long jobId){
        return   jobPostingService.getJobTitle(jobId);
    }

    //bunun controller unit testinde kaldım testlerde
    @GetMapping("/{jobId}/getApplications")
    @Transactional
    public  ResponseEntity<ApiResponse> getApplicationsBasedOnJob(@PathVariable Long jobId){
        return   jobPostingService.getApplicationList(jobId);
    }

        @GetMapping("/{jobPostingId}/getApplication/{candidateId}")
    @Transactional
    public  ResponseEntity<ApiResponse> getApplicationsBasedOnJob(@PathVariable Long jobPostingId,@PathVariable Long candidateId){
        return   jobPostingService.getApplication(jobPostingId,candidateId);
    }

            @PutMapping("/{jobPostingId}/recruiterSpesificUpdate")
        @Transactional
        public  ResponseEntity<ApiResponse> recruiterSpesificUpdateOnJobPosting(
                @RequestBody @Valid JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO ,
                                                                                   @PathVariable Long jobPostingId){
            return   jobPostingService.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO, jobPostingId);
        }

    @GetMapping("/internal/{jobPostingId}")
    @Transactional
    public   ResponseEntity<ApiResponse> recruiterSpesificFetch(
            @PathVariable Long jobPostingId){
        return   jobPostingService.recruiterSpesificFetch( jobPostingId);
    }

    @GetMapping("/existsById/{id}")
    public  boolean jobPostingsExistById(@PathVariable Long id){
        return  jobPostingService.jobPostingExistsById(id);
    }

}
