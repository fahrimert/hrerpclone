package com.hrerp.candidatems.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrerp.candidatems.dto.*;
import com.hrerp.candidatems.jobPosting.JobPostingClient;
import com.hrerp.candidatems.mapper.ApplicationMapper;
import com.hrerp.candidatems.mapper.CandidateMapper;
import com.hrerp.candidatems.model.*;
import com.hrerp.candidatems.repository.ApplicationRepository;
import com.hrerp.candidatems.repository.CandidateRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationService implements ApplicationServiceImpl {

    private  final ApplicationRepository applicationRepository;
    private  final  ApplicationMapper applicationMapper;
    private  final  CandidateMapper candidateMapper;
    private final JobPostingClient jobPostingClient;
    private  final  CandidateRepository candidateRepository;

    public ApplicationService(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper, CandidateMapper candidateMapper, JobPostingClient jobPostingClient, CandidateRepository candidateRepository) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
        this.candidateMapper = candidateMapper;
        this.jobPostingClient = jobPostingClient;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public ResponseEntity<ApiResponse> createApplication(@Valid  ApplicationRequestDTO applicationRequestDTO,Long jobPostingId){
        try{


        Optional<Candidate> candidate = candidateRepository.findById(applicationRequestDTO.getCandidateId());
        if (candidate.isEmpty() || candidate.get().getId() == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("Candidate Not Found",List.of("error"),HttpStatus.CONFLICT));

        }
        Optional<String> appliedPositionName = jobPostingClient.getJobTitleForValidationOnAppliedPosition(jobPostingId);
        if (appliedPositionName.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("Applied Position Name Not Found",List.of("error"),HttpStatus.CONFLICT));
        }

        Candidate existCandidate = candidate.get();
        String existAppliedPosition = appliedPositionName.get();

        boolean hasAlreadyApplied = existCandidate.getApplications().stream().anyMatch(a -> a.getJobPostingId().equals(jobPostingId));
        if (hasAlreadyApplied){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("Halihazırdaki şirkete başvurunuz değerlendirilmektedir",List.of("error"),HttpStatus.CONFLICT));

        }
        if (candidate.isPresent()) {
            Candidate presentCandidate = candidate.get();
            Applications applicationRequest =   applicationMapper.toApplication(applicationRequestDTO,jobPostingId,presentCandidate,existAppliedPosition);


            Applications savedApplication = applicationRepository.save(applicationRequest);

            jobPostingClient.incrementApplication(jobPostingId);

            return ResponseEntity.ok(ApiResponse.success(savedApplication));

        }

        return ResponseEntity.internalServerError().body(ApiResponse.error("Error",List.of("error"),HttpStatus.INTERNAL_SERVER_ERROR));
        }catch (FeignException e) {
            return ResponseEntity.status(409)
                    .body(ApiResponse.error(
                           e.getMessage(),
                            null,
                            HttpStatus.valueOf(409)
                    ));
        }
    }

    @Override
    public ResponseEntity getApplicationsBasedOnJobId(Long jobPostingId) {
        List<Applications> applications = applicationRepository.findAllByJobPostingId(jobPostingId);
        List<ApplicationsOnSpesificJobPostingDTO> applicationListOnSpesificJobPostingDTO =
                applications
                .stream()
                        .filter(Objects::nonNull)
                        .filter(app -> app.getCandidate()!= null)
                        .map(application -> {
                    ApplicationsOnSpesificJobPostingDTO applicationListOnSpesificJobPostingDTOSingle = new ApplicationsOnSpesificJobPostingDTO();
                    applicationListOnSpesificJobPostingDTOSingle.setApplicationId(application.getId());
                    applicationListOnSpesificJobPostingDTOSingle.setApplicationDate(application.getApplicationDate());
                    applicationListOnSpesificJobPostingDTOSingle.setCandidateId(application.getCandidate().getId());
                    applicationListOnSpesificJobPostingDTOSingle.setCandidateFullName(application.getCandidate().getFirstName() + application.getCandidate().getLastName());
                    applicationListOnSpesificJobPostingDTOSingle.setCandidateEmail(application.getCandidate().getEmail());
                    return  applicationListOnSpesificJobPostingDTOSingle;
                })
                        .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return   ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(applicationListOnSpesificJobPostingDTO));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getApplicationBasedOnJobId(Long jobPostingId,Long candidateId) {
        try{
        Optional<Candidate> candidate = candidateRepository.findById(candidateId);
        Optional<Applications> applications = applicationRepository.findByIdAndJobPostingId(jobPostingId,candidateId);

            if (candidate.isEmpty() || applications.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Candidate or Application not found",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }
        ApplicationDetailJobPostingDTO applicationDetailJobPostingDTO = new ApplicationDetailJobPostingDTO();
            applicationDetailJobPostingDTO.setApplicationId(applications.get().getId());
        applicationDetailJobPostingDTO.setApplicationDate(applications.get().getApplicationDate());
        applicationDetailJobPostingDTO.setCandidateId(candidate.get().getId());
        applicationDetailJobPostingDTO.setCandidateEmail(candidate.get().getEmail());
            applicationDetailJobPostingDTO.setCandidateFullName(candidate.get().getFirstName() + candidate.get().getLastName());
            applicationDetailJobPostingDTO.setCandidateCity(
                    Optional.ofNullable(candidate.get().getAddress())
                            .map(Address::getCity)
                            .orElse(null)
            );
            applicationDetailJobPostingDTO.setCandidateCountry(      Optional.ofNullable(candidate.get().getAddress())
                    .map(Address::getCountry)
                    .orElse(null));
            applicationDetailJobPostingDTO.setCandidateAddress(    Optional.ofNullable(candidate.get().getAddress())
                    .map(Address::getAddress)
                    .orElse(null));
            applicationDetailJobPostingDTO.setCoverLetter(applications.get().getCoverLetter());

            applicationDetailJobPostingDTO.setLinkedinUrl(    Optional.ofNullable(candidate.get().getConnections())
                    .map(Connections::getLinkedinUrl)
                    .orElse(null));
            applicationDetailJobPostingDTO.setInstagramUrl(    Optional.ofNullable(candidate.get().getConnections())
                    .map(Connections::getInstagramUrl)
                    .orElse(null));;
            applicationDetailJobPostingDTO.setFacebookUrl(    Optional.ofNullable(candidate.get().getConnections())
                    .map(Connections::getFacebookUrl)
                    .orElse(null));;
            applicationDetailJobPostingDTO.setPhoneNumber(    Optional.ofNullable(candidate.get().getConnections())
                    .map(Connections::getPhoneNumber)
                    .orElse(null));;
            applicationDetailJobPostingDTO.setCvUrl((    Optional.ofNullable(candidate.get().getCvUrl())
                    .orElse(null)));

        return   ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                applicationDetailJobPostingDTO
        ));

        } catch (FeignException e) {
            return ResponseEntity.status(e.status())
                    .body(ApiResponse.error(
                            "Candidate or application not found: " + e.getMessage(),
                            null,
                            HttpStatus.valueOf(e.status())
                    ));
        }
    }


        @Override
        public ResponseEntity<ApiResponse> getTheProperCandidates(Long jobPostingId) {
      try {
          StandardResponse<JobPostingResponseDTO> response = jobPostingClient.getJobPostingById(jobPostingId);
          ObjectMapper mapper = new ObjectMapper();

          if (!response.isSuccess()){
              return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(ApiResponse.error(response.getMessage(), null,HttpStatus.NOT_FOUND));
          }
          JobPostingResponseDTO jobPosting = response.getData();



              List<Candidate> allCandidates = candidateRepository.findAll();
            if (allCandidates.isEmpty()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("Candidate List is empty", null,HttpStatus.CONFLICT));

            }
              List<CandidateResponseDTO> matchedCandidates = allCandidates.stream()
                              .filter( a-> a.getSkills() != null && !a.getSkills().isEmpty())
                              .filter(candidate  -> candidate.getSkills().stream().anyMatch(    jobPosting.getRequiredSkillsList()::contains) )
                              .map(candidateMapper::fromCandidate).toList();
          return ResponseEntity.ok(ApiResponse.success(matchedCandidates));

      }
      catch (FeignException.NotFound e) {
          return ResponseEntity.status(e.status())
                  .body(   ApiResponse.error(
                          "Candidate or application not found: ",
                          null,
                          HttpStatus.NOT_FOUND
                  ));
      }
      catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(
                          ApiResponse.error(
                                  "Unexpected error: " + e.getMessage(),
                                  null,
                                  HttpStatus.INTERNAL_SERVER_ERROR
                          ));

      }
    }


    @Override
    public ResponseEntity<ApiResponse> updateTheCandidateApplicationStatus(Long candidateId, ApplicationStatus applicationStatusRequest) {
        Optional<Applications> applications = applicationRepository.findByCandidateId(candidateId);

        if (applications.isPresent() && applications.isEmpty() == false){
            Applications applications1 = applications.get();

            applications1.setApplicationStatus(applicationStatusRequest);

            return   ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                    applications1.getApplicationStatus()
            ));

        }

        else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            "There is no application spesified on candidate",
                            null,
                            HttpStatus.CONFLICT
                    ));
        }


    }


}
