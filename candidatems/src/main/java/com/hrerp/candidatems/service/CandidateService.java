package com.hrerpclone.service;

import com.hrerpclone.dto.ApiResponse;
import com.hrerpclone.dto.JobPostingRequestDTO;
import com.hrerpclone.dto.JobPostingResponseDTO;
import com.hrerpclone.mapper.JobPostingMapper;
import com.hrerpclone.model.JobPosting;
import com.hrerpclone.repository.JobPostingRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobPostingService implements  JobPostingServiceImpl{

    private  final JobPostingRepository jobPostingRepository;
    private  final JobPostingMapper jobPostingMapper;
    public JobPostingService(JobPostingRepository jobPostingRepository, JobPostingMapper jobPostingMapper) {
        this.jobPostingRepository = jobPostingRepository;
        this.jobPostingMapper = jobPostingMapper;
    }

    public ResponseEntity<List<JobPostingResponseDTO>> findAllJobPostings() {
        return  ResponseEntity.ok(jobPostingRepository.findAll()
                .stream()
                .map(jobPostingMapper::fromJobPosting)
                .collect(Collectors.toList()));
    }

    @Transactional
    public ResponseEntity<ApiResponse> createJobPosting(@Valid JobPostingRequestDTO jobPostingRequestDTO) {
        JobPosting jobPosting =jobPostingMapper.toJobPosting(jobPostingRequestDTO);
        jobPostingRepository.save(jobPosting);
return  ResponseEntity.status(HttpStatus.ACCEPTED)
        .body(ApiResponse.success(jobPosting.getJobTitle()));
    }

    @Override
    public ResponseEntity<JobPostingResponseDTO> findJobById(Long id) {
   return  ResponseEntity.ok(jobPostingMapper.fromJobPosting( jobPostingRepository.findById(id).orElse(null)));

    }

    @Override
    public ResponseEntity<JobPostingResponseDTO> updateJobById(Long id,JobPostingRequestDTO updatedJobPosting) {
        JobPosting jobPosting = jobPostingRepository.findById(id).orElse(null);
         jobPosting.setId(updatedJobPosting.getId());
         jobPosting.setJobTitle(updatedJobPosting.getJobTitle());
        jobPosting.setJobPostingDescription(updatedJobPosting.getJobDescription());
        jobPosting.setSalary(updatedJobPosting.getSalary());
        jobPosting.setJobType(updatedJobPosting.getJobType());
        jobPosting.setRequiredSkillsList(updatedJobPosting.getRequiredSkillsList());
        jobPosting.setDepartment(updatedJobPosting.getDepartment());
        jobPosting.setLocation(updatedJobPosting.getLocation());
        jobPosting.setPostingStatus(updatedJobPosting.getJobPostingStatus());
        jobPosting.setDepartment(updatedJobPosting.getDepartment());
        jobPosting.setJobPostingDeadline(updatedJobPosting.getJobPostingDeadline());
        jobPosting.setJobPostingDate(new Date());

       ResponseEntity<JobPostingResponseDTO> jobPostingResponse = ResponseEntity.ok( jobPostingMapper.fromJobPosting(jobPosting));
    return  jobPostingResponse;
    }

    @Override
    public void deleteJobPosting(Long id) {
        jobPostingRepository.deleteById(id);
    }


}
