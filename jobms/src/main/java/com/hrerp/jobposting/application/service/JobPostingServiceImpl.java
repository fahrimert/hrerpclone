package com.hrerp.jobposting.application.service;

import com.hrerp.jobposting.application.dto.ApiResponse;
import com.hrerp.jobposting.application.dto.JobPostingRequestDTO;
import com.hrerp.jobposting.application.dto.JobPostingRequestRecruiterSpesificDTO;
import com.hrerp.jobposting.application.dto.JobPostingResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface JobPostingServiceImpl {

    ResponseEntity<List<JobPostingResponseDTO>> findAllJobPostings();

    ResponseEntity<ApiResponse> createJobPosting(JobPostingRequestDTO jobPostingRequestDTO);

    ResponseEntity<JobPostingResponseDTO> findJobById(Long id);

    ResponseEntity<JobPostingResponseDTO> updateJobById(Long id, JobPostingRequestDTO updatedJobPosting);

    ResponseEntity<ApiResponse> deleteJobPosting(Long id);

    ResponseEntity incrementApplication(Long id);

    String  getJobTitle(Long jobId);

    ResponseEntity<ApiResponse> getApplication(Long jobId, Long candidateId);

    ResponseEntity<ApiResponse> recruiterSpesificUpdate(@Valid JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO, Long jobPostingId);
}
