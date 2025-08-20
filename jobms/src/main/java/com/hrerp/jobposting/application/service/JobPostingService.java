package com.hrerp.jobposting.application.service;

import com.hrerp.jobposting.application.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface JobPostingService {

    ResponseEntity<List<JobPostingResponseDTO>> findAllJobPostings();

    ResponseEntity<ApiResponse> createJobPosting(JobPostingRequestDTO jobPostingRequestDTO);

    ResponseEntity findJobById(Long id);

    ResponseEntity<JobPostingResponseDTO> updateJobById(Long id, JobPostingRequestDTO updatedJobPosting);

    ResponseEntity<ApiResponse> deleteJobPosting(Long id);

    ResponseEntity incrementApplication(Long id);

    String  getJobTitle(Long jobId);

    ResponseEntity<ApiResponse> getApplicationList(Long jobId);

    ResponseEntity<ApiResponse> getApplication(Long jobId, Long candidateId);
    ResponseEntity<ApiResponse>     recruiterSpesificUpdate(@Valid JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO, Long jobPostingId);

    ResponseEntity<ApiResponse> recruiterSpesificFetch(Long jobPostingId);

    boolean jobPostingExistsById(Long id);
}
