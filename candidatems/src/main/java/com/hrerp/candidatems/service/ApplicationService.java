package com.hrerp.candidatems.service;

import com.hrerp.candidatems.dto.*;
import com.hrerp.candidatems.jobPosting.JobPostingClient;
import com.hrerp.candidatems.mapper.ApplicationMapper;
import com.hrerp.candidatems.mapper.CandidateMapper;
import com.hrerp.candidatems.model.Applications;
import com.hrerp.candidatems.model.Candidate;
import com.hrerp.candidatems.model.Connections;
import com.hrerp.candidatems.repository.ApplicationRepository;
import com.hrerp.candidatems.repository.CandidateRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Application;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationService implements ApplicationServiceImpl {

    private  final ApplicationRepository applicationRepository;
    private  final  ApplicationMapper applicationMapper;
    private final JobPostingClient jobPostingClient;
    private  final  CandidateRepository candidateRepository;

    public ApplicationService(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper, JobPostingClient jobPostingClient, CandidateRepository candidateRepository) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
        this.jobPostingClient = jobPostingClient;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public ResponseEntity<ApiResponse> createApplication(@Valid  ApplicationRequestDTO applicationRequestDTO,Long jobPostingId) {
        Optional<Candidate> candidate = candidateRepository.findById(applicationRequestDTO.getCandidateId());

        String appliedPositionName = jobPostingClient.getJobTitleForValidationOnAppliedPosition(jobPostingId);

        boolean hasAlreadyApplied = candidate.get().getApplications().stream().anyMatch(a -> a.getJobPostingId().equals(jobPostingId));
        if (hasAlreadyApplied){
            return ResponseEntity.internalServerError().body(ApiResponse.error("Halihazırdaki şirkete başvurunuz değerlendirilmektedir",List.of("error"),HttpStatus.CONFLICT));

        }
        if (candidate.isPresent()) {
            Candidate presentCandidate = candidate.get();
            Applications applicationRequest =   applicationMapper.toApplication(applicationRequestDTO,jobPostingId,presentCandidate,appliedPositionName);

            if (applicationRequestDTO.getCandidateId() == null) {
                throw new IllegalArgumentException("Candidate ID must not be null");
            }
            applicationRepository.save(applicationRequest);

            jobPostingClient.incrementApplication(jobPostingId);

            return ResponseEntity.ok(ApiResponse.success(applicationRepository.save(applicationRequest)));

        }

        return ResponseEntity.internalServerError().body(ApiResponse.error("Error",List.of("error"),HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @Override
    public ResponseEntity getApplicationsBasedOnJobId(Long jobPostingId) {
        List<Applications> applications = applicationRepository.findAllByJobPostingId(jobPostingId);
        List<ApplicationsOnSpesificJobPostingDTO> applicationListOnSpesificJobPostingDTO =
                applications
                .stream()
                .map(application -> {
                    ApplicationsOnSpesificJobPostingDTO applicationListOnSpesificJobPostingDTOSingle = new ApplicationsOnSpesificJobPostingDTO();
                    applicationListOnSpesificJobPostingDTOSingle.setApplicationId(application.getId());
                    applicationListOnSpesificJobPostingDTOSingle.setApplicationDate(application.getApplicationDate());
                    applicationListOnSpesificJobPostingDTOSingle.setCandidateId(application.getCandidate().getId());
                    applicationListOnSpesificJobPostingDTOSingle.setCandidateFullName(application.getCandidate().getFirstName() + application.getCandidate().getLastName());
                    applicationListOnSpesificJobPostingDTOSingle.setCandidateEmail(application.getCandidate().getEmail());
                    return  applicationListOnSpesificJobPostingDTOSingle;

                })
                .collect(Collectors.toList());

        return  ResponseEntity.ok(applicationListOnSpesificJobPostingDTO);
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
            applicationDetailJobPostingDTO.setCandidateCity(candidate.get().getAddress() != null ? candidate.get().getAddress().getCity() : null);
            applicationDetailJobPostingDTO.setCandidateCountry(candidate.get().getAddress() != null ? candidate.get().getAddress().getCountry() : null);
            applicationDetailJobPostingDTO.setCandidateAddress(candidate.get().getAddress() != null ? candidate.get().getAddress().getAddress() : null);
            applicationDetailJobPostingDTO.setCoverLetter(applications.get().getCoverLetter());
            applicationDetailJobPostingDTO.setLinkedinUrl(candidate.get().getConnections().getLinkedinUrl());
            applicationDetailJobPostingDTO.setLinkedinUrl(candidate.get().getConnections().getInstagramUrl());
            applicationDetailJobPostingDTO.setLinkedinUrl(candidate.get().getConnections().getFacebookUrl());
            applicationDetailJobPostingDTO.setPhoneNumber(candidate.get().getConnections() != null ? candidate.get().getConnections().getPhoneNumber() : null);
            applicationDetailJobPostingDTO.setCvUrl(candidate.get().getCvUrl());

            System.out.println(applicationDetailJobPostingDTO);
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


}
