package com.hrerp.candidatems.jobPosting;

import com.hrerp.candidatems.dto.ApiResponse;
import com.hrerp.candidatems.dto.JobPostingResponseDTO;
import com.hrerp.candidatems.dto.StandardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import javax.swing.text.html.Option;
import java.util.Optional;

@FeignClient(
        name = "jobms",
        url = "http://localhost:8083"
)


public interface JobPostingClient {
    @PutMapping("/api/v1/jobPostings/{id}/incrementApplication")
    void  incrementApplication(@PathVariable Long id);

    @GetMapping("/api/v1/jobPostings/{jobPostingId}")
    StandardResponse<JobPostingResponseDTO> getJobPostingById(@PathVariable Long jobPostingId);


    @GetMapping("/api/v1/jobPostings/{jobId}/getJobTitle")
    Optional<String> getJobTitleForValidationOnAppliedPosition(@PathVariable Long jobId);
}
