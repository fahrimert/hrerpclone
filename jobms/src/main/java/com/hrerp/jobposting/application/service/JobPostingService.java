package com.hrerp.jobposting.application.service;

import com.hrerp.jobposting.infrastructure.Client.ApplicationsClient;
import com.hrerp.jobposting.application.dto.*;
import com.hrerp.jobposting.infrastructure.mapper.JobPostingMapper;
import com.hrerp.jobposting.infrastructure.persistence.JobPosting;
import com.hrerp.jobposting.infrastructure.repository.JobPostingRepository;
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
        private  final ApplicationsClient applicationsClient;
        public JobPostingService(JobPostingRepository jobPostingRepository, JobPostingMapper jobPostingMapper, ApplicationsClient applicationsClient) {
            this.jobPostingRepository = jobPostingRepository;
            this.jobPostingMapper = jobPostingMapper;
            this.applicationsClient = applicationsClient;
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
        public ResponseEntity<JobPostingResponseDTO> updateJobById(Long id, JobPostingRequestDTO updatedJobPosting) {
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
        public ResponseEntity<ApiResponse> deleteJobPosting(Long id) {
          Optional<JobPosting> jobPosting = jobPostingRepository.findById(id);

            if (jobPosting.isPresent()){

                jobPostingRepository.deleteById(id);

                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.success(
                                "Delete Job posting succesfully applied"
                        ));


            }
    else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Job Posting does not exists",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }

        }

        @Override
        public ResponseEntity incrementApplication(Long id) {
            Optional <JobPosting> jobPosting = jobPostingRepository.findById(id);

            if (jobPosting.isPresent()){
                JobPosting jobPostingUpdated = jobPosting.get();
                Integer currentCount = jobPostingUpdated.getApplicationCount() != null ? jobPostingUpdated.getApplicationCount()  : 0 ;
                jobPostingUpdated.setApplicationCount(currentCount + 1);
                JobPosting incrementUpdatedOne =  jobPostingRepository.save(jobPostingUpdated);
                System.out.println("New count after save: " + incrementUpdatedOne.getApplicationCount());

               return ResponseEntity.status(HttpStatus.OK)
                        .body(ResponseEntity.ok(ApiResponse.success(incrementUpdatedOne)
                        ));
            }

            else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Job Posting does not exists",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }

        }

        @Override
        public String getJobTitle(Long jobId) {
            Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobId);

            if (jobPosting.isPresent()){

            return  jobPosting.get().getJobTitle();


            }
            else {
                return "Job does not exists";
            }

        }

        public ResponseEntity<ApiResponse> getApplicationList(Long jobId) {
            try{

                Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobId);
                if (jobPosting.isPresent()){
                    List<ApplicationListDTO>  applicationsList = applicationsClient.getApplications(jobPosting.get().getId());
                    JobPostingWithApplicationsResponseDTO jobPostingWithApplicationsResponseDTO = new JobPostingWithApplicationsResponseDTO();
                    jobPostingWithApplicationsResponseDTO.setJobId(jobPosting.get().getId());
                    jobPostingWithApplicationsResponseDTO.setJobTitle(jobPosting.get().getJobTitle());
                    jobPostingWithApplicationsResponseDTO.setCity(jobPosting.get().getLocation().getCity());
                    jobPostingWithApplicationsResponseDTO.setJobStatus(jobPosting.get().getPostingStatus());
                    jobPostingWithApplicationsResponseDTO.setJobPostingDate(jobPosting.get().getJobPostingDate());
                    jobPostingWithApplicationsResponseDTO.setJobPostingDeadline(jobPosting.get().getJobPostingDeadline());
                    jobPostingWithApplicationsResponseDTO.setCandidateList(applicationsList);


                            return   ResponseEntity.status(HttpStatus.OK)
                            .body(ApiResponse.success(
                                    jobPostingWithApplicationsResponseDTO
                            ));
                }
                else {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(ApiResponse.error(
                                    "Job posting does not exists",
                                    null,
                                    HttpStatus.CONFLICT
                            ));
                }
            }
                catch (Exception e){
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(ApiResponse.error(
                                    "Server Error",
                                    null,
                                    HttpStatus.INTERNAL_SERVER_ERROR
                            ));
                }
        }


        @Override
        public ResponseEntity<ApiResponse> getApplication(Long jobId, Long candidateId) {
            Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobId);

            if (jobPosting.isPresent()){
                ApiResponse<ApplicationsDTO> response = applicationsClient.getApplicationSingle(jobPosting.get().getId(), candidateId);
                ApplicationsDTO dto = response.getData();
                System.out.println("ApplicationsDto" + dto);

                JobPostingWithSingleApplicationResponseDTO jobPostingWithApplicationsResponseDTO = new JobPostingWithSingleApplicationResponseDTO();
                jobPostingWithApplicationsResponseDTO.setJobId(jobPosting.get().getId());
                jobPostingWithApplicationsResponseDTO.setJobTitle(jobPosting.get().getJobTitle());
                jobPostingWithApplicationsResponseDTO.setCity(jobPosting.get().getLocation().getCity());
                jobPostingWithApplicationsResponseDTO.setJobStatus(jobPosting.get().getPostingStatus());
                jobPostingWithApplicationsResponseDTO.setJobPostingDate(jobPosting.get().getJobPostingDate());
                jobPostingWithApplicationsResponseDTO.setJobPostingDeadline(jobPosting.get().getJobPostingDeadline());
                jobPostingWithApplicationsResponseDTO.setCandidate(dto);


                return   ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success(
                                jobPostingWithApplicationsResponseDTO
                        ));
            }
            else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Job posting does not exists",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }
        }

    @Override
    public ResponseEntity<ApiResponse> recruiterSpesificUpdate(JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO, Long jobPostingId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId).orElse(null);

        jobPosting.setInternalJobId(jobPostingRequestRecruiterSpesificDTO.getInternalJobId());
        jobPosting.setInternalPostingDate(jobPostingRequestRecruiterSpesificDTO.getInternalPostingDate());
        jobPosting.setInternalDescripton(jobPostingRequestRecruiterSpesificDTO.getInternalDescripton());
        jobPosting.setHiringManagerName(jobPostingRequestRecruiterSpesificDTO.getHiringManagerName());
        jobPosting.setInternalHrNote(jobPostingRequestRecruiterSpesificDTO.getInternalHrNote());
        jobPosting.setReplacement(jobPostingRequestRecruiterSpesificDTO.isReplacement());

        JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO1 = new JobPostingRequestRecruiterSpesificDTO();
        jobPostingRequestRecruiterSpesificDTO1.setInternalJobId(jobPosting.getInternalJobId());
        jobPostingRequestRecruiterSpesificDTO1.setReplacement(jobPosting.isReplacement());
        jobPostingRequestRecruiterSpesificDTO1.setInternalPostingDate(jobPosting.getInternalPostingDate());
        jobPostingRequestRecruiterSpesificDTO1.setInternalHrNote(jobPosting.getInternalHrNote());
        jobPostingRequestRecruiterSpesificDTO1.setReplacementFor(jobPosting.getReplacementFor());
        jobPostingRequestRecruiterSpesificDTO1.setHiringManagerName(jobPosting.getHiringManagerName());

        return ResponseEntity.ok(ApiResponse.success(jobPostingRequestRecruiterSpesificDTO1));
    }

}
