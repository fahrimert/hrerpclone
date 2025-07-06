package com.hrerp.candidatems.jobPosting;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(
        name = "jobms",
        url = "http://localhost:8083"
)


public interface JobPostingClient {
    @PutMapping("/api/v1/jobPostings/{id}/incrementApplication")
    void  incrementApplication(@PathVariable Long id);

    @GetMapping("/api/v1/jobPostings/{jobId}/getJobTitle")
    String  getJobTitleForValidationOnAppliedPosition(@PathVariable Long jobId);
}
