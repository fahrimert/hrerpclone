package com.hrerp.Client;

import com.hrerp.dto.*;
import com.hrerp.dto.JobPostingDTOs.JobPostingRequestRecruiterSpesificDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "jobms",
        url = "http://localhost:8083"
)


public interface JobPostingClient {


    @GetMapping("/api/v1/jobPostings/existsById/{jobPostingId}")
    boolean jobPostingExistsById(@PathVariable Long jobPostingId);




    @PutMapping("/api/v1/jobPostings/{jobPostingId}/recruiterSpesificUpdate")
    ResponseEntity<ApiResponse>  recruiterSpesificUpdate(@RequestBody JobPostingRequestRecruiterSpesificDTO requestRecruiterSpesificDTO ,
                                                         @PathVariable Long jobPostingId);

    @GetMapping("/api/v1/jobPostings/internal/{jobPostingId}")
    ResponseEntity<ApiResponse> recruiterSpesificFetch(@PathVariable  Long jobPostingId);


    //    @GetMapping("/api/v1/jobPostings/{jobId}")
//    ResponseEntity<JobPostingResponseDTO>  getJobTitleForValidationOnAppliedPosition(@PathVariable Long jobId);
//
//    @PostMapping("/api/v1/jobPostings/")
//    ResponseEntity<JobPostingResponseDTO>  createJobPosting(@PathVariable Long jobId);
}
