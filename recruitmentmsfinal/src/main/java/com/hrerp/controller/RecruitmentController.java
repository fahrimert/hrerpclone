package com.hrerp.controller;

import com.hrerp.Client.JobPostingClient;
import com.hrerp.dto.ApiResponse;
import com.hrerp.dto.JobPostingRequestDTO;
import com.hrerp.dto.JobPostingRequestRecruiterSpesificDTO;
import com.hrerp.dto.JobPostingResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getJobPosting/{jobPostingId}")
    public  ResponseEntity<JobPostingResponseDTO> getJobPostingById(@PathVariable Long jobPostingId){
        return  jobPostingClient.getJobPostingById(jobPostingId);
    }

    @PostMapping("/createJobPosting")
    public  ResponseEntity<ApiResponse> createJobPosting(@RequestBody @Valid JobPostingRequestDTO jobPostingRequestDTO){
        return  ResponseEntity.ok(ApiResponse.success(jobPostingClient.createJobPosting(jobPostingRequestDTO)));
    }

    @PutMapping("/updateJobPosting")
    public  ResponseEntity<ApiResponse> updateJobPosting(@PathVariable Long id,@RequestBody @Valid JobPostingRequestDTO jobPostingRequestDTO){
        return  ResponseEntity.ok(ApiResponse.success(jobPostingClient.createJobPosting(jobPostingRequestDTO)));
    }

    @DeleteMapping("/{jobPostingId}")
    public  void deleteJobPosting(@PathVariable Long jobPostingId){
          jobPostingClient.deleteJobPosting(jobPostingId);
    }

    @PutMapping("/{jobPostingId}/updateRecruiterSpesific")
    public   ResponseEntity<ApiResponse> updateRecruiterSpesificSectionsOnJobPosting(JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO, @PathVariable Long jobPostingId){
        return  ResponseEntity.ok(ApiResponse.success(jobPostingClient.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO,jobPostingId)));
    }

}
