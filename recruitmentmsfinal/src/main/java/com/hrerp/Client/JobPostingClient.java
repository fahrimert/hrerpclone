package com.hrerp.Client;

import com.hrerp.dto.ApiResponse;
import com.hrerp.dto.JobPostingRequestDTO;
import com.hrerp.dto.JobPostingRequestRecruiterSpesificDTO;
import com.hrerp.dto.JobPostingResponseDTO;
import jakarta.validation.Valid;
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
    ResponseEntity<List<com.hrerp.dto.JobPostingResponseDTO>> getJobPostings();


    @GetMapping("api/v1/jobPostings/{jobPostingId}")
    ResponseEntity<com.hrerp.dto.JobPostingResponseDTO> getJobPostingById(@PathVariable Long jobPostingId);



    @PostMapping("api/v1/jobPostings")
    ResponseEntity<JobPostingRequestDTO> createJobPosting(@RequestBody @Valid JobPostingRequestDTO jobPostingRequestDTO);


    @PutMapping("api/v1/jobPostings/{jobPostingId}")
    ResponseEntity<JobPostingResponseDTO> updateJobPosting(@PathVariable Long jobPostingId,@RequestBody @Valid JobPostingRequestDTO jobPostingRequestDTO);

    @DeleteMapping("api/v1/jobPostings/{jobPostingId}")
    void deleteJobPosting(Long jobPostingid);


    @PutMapping("api/v1/jobPostings/{jobPostingId}/recruiterSpesificUpdate")
    ResponseEntity<ApiResponse> recruiterSpesificUpdate(JobPostingRequestRecruiterSpesificDTO requestRecruiterSpesificDTO , Long jobPostingId);

//    @GetMapping("/api/v1/jobPostings/{jobId}")
//    ResponseEntity<JobPostingResponseDTO>  getJobTitleForValidationOnAppliedPosition(@PathVariable Long jobId);
//
//    @PostMapping("/api/v1/jobPostings/")
//    ResponseEntity<JobPostingResponseDTO>  createJobPosting(@PathVariable Long jobId);
}
