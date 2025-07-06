package com.hrerpclone.service;

import com.hrerpclone.dto.ApiResponse;
import com.hrerpclone.dto.JobPostingRequestDTO;
import com.hrerpclone.dto.JobPostingResponseDTO;
import com.hrerpclone.model.JobPosting;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface JobPostingServiceImpl {

    ResponseEntity<List<JobPostingResponseDTO>> findAllJobPostings();

    ResponseEntity<ApiResponse> createJobPosting(JobPostingRequestDTO jobPostingRequestDTO);

    ResponseEntity<JobPostingResponseDTO> findJobById(Long id);

    ResponseEntity<JobPostingResponseDTO> updateJobById(Long id, JobPostingRequestDTO updatedJobPosting);

    void deleteJobPosting(Long id);

}
