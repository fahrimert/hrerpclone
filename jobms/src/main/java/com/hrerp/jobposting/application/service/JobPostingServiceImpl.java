package com.hrerp.jobposting.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrerp.jobposting.application.dto.enums.JobStatus;
import com.hrerp.jobposting.infrastructure.Client.ApplicationsClient;
import com.hrerp.jobposting.application.dto.*;
import com.hrerp.jobposting.infrastructure.mapper.JobPostingMapper;
import com.hrerp.jobposting.infrastructure.persistence.JobPosting;
import com.hrerp.jobposting.infrastructure.repository.JobPostingRepository;
import feign.FeignException;
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
    public class    JobPostingServiceImpl implements  JobPostingService    {

        private  final JobPostingRepository jobPostingRepository;
        private JobPostingMapper jobPostingMapper;
        private  final ApplicationsClient applicationsClient;

    public JobPostingServiceImpl(JobPostingRepository jobPostingRepository, JobPostingMapper jobPostingMapper, ApplicationsClient applicationsClient) {
        this.jobPostingRepository = jobPostingRepository;
        this.jobPostingMapper = jobPostingMapper;
        this.applicationsClient = applicationsClient;
    }

//servis unit testi için job posting mapper setter
    public void setJobPostingMapper(JobPostingMapper jobPostingMapper) {
        this.jobPostingMapper = jobPostingMapper;
    }

        @Override
        public ResponseEntity<List<JobPostingResponseDTO>>  findAllJobPostings() {
                return  ResponseEntity.ok(jobPostingRepository.findAll()
                        .stream()
                                .filter(a -> !a.getPostingStatus().equals(JobStatus.CLOSED))
                        .map(jobPostingMapper::fromJobPosting)
                        .collect(Collectors.toList()));
            }


        @Override
        @Transactional
        public ResponseEntity<ApiResponse> createJobPosting(@Valid JobPostingRequestDTO jobPostingRequestDTO) {
            JobPosting jobPosting =jobPostingMapper.toJobPosting(jobPostingRequestDTO);
            jobPostingRepository.save(jobPosting);
    return  ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(ApiResponse.success(jobPosting.getJobTitle()));
        }

        @Override
        public  ResponseEntity<?>  findJobById(Long id) {
            Optional<JobPosting> jobPosting  = jobPostingRepository.findById(id);
            if (jobPosting.isEmpty()){
                return  ResponseEntity.status(HttpStatus.NOT_FOUND) .body(ApiResponse.error(
                        "Job Posting not found",
                        null,
                        HttpStatus.NOT_FOUND
                ));
            };

            JobPosting existsJobPosting = jobPosting.get();
            if (existsJobPosting.getRequiredSkillsList().isEmpty()) {
              return   ResponseEntity.status(HttpStatus.NOT_FOUND) .body(ApiResponse.error(
                        "Required Skills Are Empty",
                        null,
                        HttpStatus.NOT_FOUND
                ));
            }
            //eğer closedsa da görmemesi lazım


            // bu kısımda heryerde eğer job posting null gelirse null exception atıyor
            // düzeltmeye çalıştım feignle alakalı hata veriyor bu sefer
            // normalde 409 vesaire döndürmesi gerekirken 500 döndürüyor feign yüzünden

            return  ResponseEntity.ok(ApiResponse.success(jobPostingMapper.fromJobPosting( existsJobPosting)));

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
        public ResponseEntity<ApiResponse> deleteJobPosting(Long id)    {
          Optional<JobPosting> jobPosting = jobPostingRepository.findById(id);

            if (jobPosting.isPresent()){

                jobPostingRepository.deleteById(id);

                return ResponseEntity.status(HttpStatus.OK)
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
        public ResponseEntity   incrementApplication(Long id) {
            Optional <JobPosting> jobPosting = jobPostingRepository.findById(id);

            if (jobPosting.isPresent()){
                JobPosting jobPostingUpdated = jobPosting.get();
                Integer currentCount = jobPostingUpdated.getApplicationCount() != null ? jobPostingUpdated.getApplicationCount()  : 0 ;
                jobPostingUpdated.setApplicationCount(currentCount + 1);
                JobPosting incrementUpdatedOne =  jobPostingRepository.save(jobPostingUpdated);
                return ResponseEntity.ok(ApiResponse.success("Application Count Incremented"));

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


        //en son bunun integrasyon testinde kaldım
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


        @Override
        public ResponseEntity<ApiResponse> getApplicationList(Long jobId) {


            try{

                Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobId);
                if (jobPosting.isPresent()){
                    List<ApplicationListDTO>  applicationsList = applicationsClient.getApplications(jobPosting.get().getId());
                    JobPostingWithApplicationsResponseDTO   jobPostingWithApplicationsResponseDTO = new JobPostingWithApplicationsResponseDTO();
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
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ApiResponse.error(
                                    "Server Error" + e.getMessage() ,
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

try{


        Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobPostingId);

        if (jobPosting.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                                "Updating  based on recruiter cant be applied ",
                            null,
                            HttpStatus.CONFLICT
                    ));
        }


        JobPosting existJobPosting = jobPosting.get();
        if (existJobPosting.getInternalJobId() != null){
            existJobPosting.setInternalJobId(jobPostingRequestRecruiterSpesificDTO.getInternalJobId());
        }
        if (jobPostingRequestRecruiterSpesificDTO.getHiringManagerName() != null) {
            existJobPosting.setHiringManagerName(jobPostingRequestRecruiterSpesificDTO.getHiringManagerName());
        }
        if (jobPostingRequestRecruiterSpesificDTO.getInternalHrNote() != null) {
            existJobPosting.setInternalHrNote(jobPostingRequestRecruiterSpesificDTO.getInternalHrNote());
        }
        existJobPosting.setReplacement(jobPostingRequestRecruiterSpesificDTO.isReplacement());
        if (jobPostingRequestRecruiterSpesificDTO.getInternalPostingDate() != null) {
            existJobPosting.setInternalPostingDate(jobPostingRequestRecruiterSpesificDTO.getInternalPostingDate());
        }
        jobPostingRepository.save(existJobPosting);

        return ResponseEntity.ok(ApiResponse.success(jobPostingMapper.fromJobPostingToRecruiterSpesificFetch(existJobPosting)));
}
catch (FeignException e) {
            String feignBody = e.contentUTF8();
            String message = feignBody;

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            message,
                            null,
                            HttpStatus.CONFLICT
                    ));
        }
catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            "Feign Client Error: " + e.getMessage(),
                            null,
                            HttpStatus.CONFLICT
                    ));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> recruiterSpesificFetch(Long jobPostingId) {
        try {
        Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobPostingId);



        if (jobPosting == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.error(
                                "Job Posting not found",
                            null,
                            HttpStatus.NO_CONTENT
                    ));
        }

        JobPosting jobPostingGet = jobPosting.get();



        return ResponseEntity.ok(ApiResponse.success(jobPostingMapper.fromJobPostingToRecruiterSpesificFetch(jobPostingGet)));
        }
catch (FeignException e) {
            String feignBody = e.contentUTF8();
            String message = feignBody;

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            message,
                            null,
                            HttpStatus.CONFLICT
                    ));
        }
catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            "Feign Client Error: " + e.getMessage(),
                            null,
                            HttpStatus.CONFLICT
                    ));
        }

    }


    @Override
    public boolean jobPostingExistsById(Long id) {
        return jobPostingRepository.existsById(id);
    }

}
