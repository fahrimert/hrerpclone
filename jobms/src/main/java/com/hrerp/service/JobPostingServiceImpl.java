package com.hrerpclone.service;

import com.hrerpclone.common.dto.JobPostingResponseDTO;
import com.hrerpclone.dto.ApiResponse;
import com.hrerpclone.dto.JobPostingRequestDTO;
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
}
