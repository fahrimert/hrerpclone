package com.hrerp.controller;

import com.hrerp.Client.JobPostingClient;
import com.hrerp.common.dto.JobPostingResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitment")
public class RecruitmentController {
    private JobPostingClient jobPostingClient;

    public RecruitmentController(JobPostingClient jobPostingClient) {
        this.jobPostingClient = jobPostingClient;
    }

    @GetMapping("/getJobPostings")
    public ResponseEntity<List<JobPostingResponseDTO>> getAllJobPostings(){
        return jobPostingClient.getJobPostings();
    }
}
