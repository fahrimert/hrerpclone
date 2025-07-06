package com.hrerp.jobposting.infrastructure.mapper;


import com.hrerp.jobposting.application.dto.JobPostingRequestDTO;
import com.hrerp.jobposting.application.dto.JobPostingResponseDTO;
import com.hrerp.jobposting.infrastructure.persistence.JobPosting;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JobPostingMapper {
    public JobPosting toJobPosting(@Valid JobPostingRequestDTO jobPostingRequestDTO){
        return  JobPosting.builder()
                .jobTitle(jobPostingRequestDTO.getJobTitle())
                .jobPostingDescription(jobPostingRequestDTO.getJobDescription())
                .salary(jobPostingRequestDTO.getSalary())
                .jobType(jobPostingRequestDTO.getJobType())
                .requiredSkillsList(jobPostingRequestDTO.getRequiredSkillsList())
                .department(jobPostingRequestDTO.getDepartment())
                .location(jobPostingRequestDTO.getLocation())
                .postingStatus(jobPostingRequestDTO.getJobPostingStatus())
                .jobPostingDeadline(jobPostingRequestDTO.getJobPostingDeadline())
                .jobPostingDate(new Date())
                .build();
    }

    public JobPostingResponseDTO fromJobPosting(JobPosting jobPosting){
        return  new JobPostingResponseDTO(
                jobPosting.getJobTitle(),
                jobPosting.getJobPostingDescription(),
                jobPosting.getSalary(),
                jobPosting.getJobType(),
                jobPosting.getRequiredSkillsList(),
                jobPosting.getDepartment(),
                jobPosting.getLocation(),
                jobPosting.getPostingStatus(),
                jobPosting.getJobPostingDeadline()
        );
    }
//    public JobPostingWithApplicationsResponseDTO fromJobPostingAndApplications(JobPosting jobPosting , List<ApplicationListDTO> applicationDTOList){
//        return  new JobPostingWithApplicationsResponseDTO(
//               jobPosting.getId(),
//                jobPosting.getJobTitle(),
//                jobPosting.getLocation().getCity(),
//                jobPosting.getPostingStatus(),
//                jobPosting.getJobPostingDate(),
//                jobPosting.getJobPostingDeadline(),
//                applicationDTOList
//
//        );
//    }
//    public JobPostingWithSingleApplicationResponseDTO fromJobPostingAndApplicationSingle(JobPosting jobPosting , ApplicationsDTO application){
//        return  new JobPostingWithSingleApplicationResponseDTO(
//                jobPosting.getId(),
//                jobPosting.getJobTitle(),
//                jobPosting.getLocation().getCity(),
//                jobPosting.getPostingStatus(),
//                jobPosting.getJobPostingDate(),
//                jobPosting.getJobPostingDeadline(),
//                application
//
//        );


}
