package com.hrerp.Client;

import com.hrerp.common.dto.JobPostingResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "jobms",
        url = "http://localhost:8083"
)


public interface JobPostingClient {
    @GetMapping("/api/v1/jobPostings")
    ResponseEntity<List<JobPostingResponseDTO>> getJobPostings();

//    @GetMapping("/api/v1/jobPostings/{jobId}")
//    ResponseEntity<JobPostingResponseDTO>  getJobTitleForValidationOnAppliedPosition(@PathVariable Long jobId);
//
//    @PostMapping("/api/v1/jobPostings/")
//    ResponseEntity<JobPostingResponseDTO>  createJobPosting(@PathVariable Long jobId);
}
