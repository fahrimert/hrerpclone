package com.hrerp.candidatems;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrerp.candidatems.dto.*;
import com.hrerp.candidatems.dto.enums.JobStatus;
import com.hrerp.candidatems.dto.enums.JobType;
import com.hrerp.candidatems.dto.enums.Location;
import com.hrerp.candidatems.jobPosting.JobPostingClient;
import com.hrerp.candidatems.mapper.ApplicationMapper;
import com.hrerp.candidatems.mapper.CandidateMapper;
import com.hrerp.candidatems.model.*;
import com.hrerp.candidatems.repository.ApplicationRepository;
import com.hrerp.candidatems.repository.CandidateRepository;
import com.hrerp.candidatems.service.ApplicationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@Transactional
public class ApplicationServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private ApplicationService applicationService;

    @Mock
    private JobPostingClient jobPostingClient;

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private CandidateRepository candidateRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ApplicationMapper applicationMapper;

    @Mock
    private CandidateMapper candidateMapper;

    @Test
    void  shouldCreateApplications() throws Exception{
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(112L);
        firstCandidate.setEmail("mockEmailFirstCandidate@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("3213214521")
                .linkedinUrl("mockLinkedinAccount")
                .instagramUrl("mockInstagramAccount")
                .facebookUrl("mockFacebookAccount")
                .build());
        firstCandidate.setCvUrl("https://cvurl.com/cv1.pdf");
        firstCandidate.setLastName("mockLastnameCandidate");
        firstCandidate.setFirstName("mockFirstnameCandidate");
        when(candidateRepository.findById(112L)).thenReturn(Optional.of(firstCandidate));



        when(jobPostingClient.getJobTitleForValidationOnAppliedPosition(50L)).thenReturn(Optional.of("Mock Job Title For Servis Test"));
        when(applicationRepository.save(any())).thenReturn(new Applications());


        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        applicationRequestDTO.setApplicationStatus(ApplicationStatus.APPLIED);
        applicationRequestDTO.setCandidateId(112L);
        applicationRequestDTO.setJobPostingId(50L);
        applicationRequestDTO.setApplicationDate(LocalDate.now());
        applicationRequestDTO.setCoverLetter("Mock Cover Letter");

        ResponseEntity<ApiResponse> response = applicationService.createApplication(applicationRequestDTO,50L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(jobPostingClient).incrementApplication(50L);
    }

    @Test
    void  createApplications_shouldHandleCandidateNotFound() throws Exception{

        when(candidateRepository.findById(112L)).thenReturn(Optional.empty());

        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        applicationRequestDTO.setApplicationStatus(ApplicationStatus.APPLIED);
        applicationRequestDTO.setCandidateId(112L);
        applicationRequestDTO.setJobPostingId(50L);
        applicationRequestDTO.setApplicationDate(LocalDate.now());
        applicationRequestDTO.setCoverLetter("Mock Cover Letter");

        ResponseEntity<ApiResponse> response = applicationService.createApplication(applicationRequestDTO,50L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getMessage()).isEqualTo("Candidate Not Found");

    }

    @Test
    void  createApplications_shouldHandleJobPostingNotFound() throws Exception{
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(112L);
        firstCandidate.setEmail("mockEmail@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneNumber")
                .linkedinUrl("mocklinkedin")
                .instagramUrl("mockİnstagram")
                .facebookUrl("mockFacebook")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName("mockLastname");
        firstCandidate.setFirstName("mockFirstname");
        when(candidateRepository.findById(112L)).thenReturn(Optional.of(firstCandidate));

        when(jobPostingClient.getJobTitleForValidationOnAppliedPosition(20L)).thenReturn(Optional.empty());

        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        applicationRequestDTO.setApplicationStatus(ApplicationStatus.APPLIED);
        applicationRequestDTO.setCandidateId(112L);
        applicationRequestDTO.setJobPostingId(20L);
        applicationRequestDTO.setApplicationDate(LocalDate.now());
        applicationRequestDTO.setCoverLetter("Mock Cover Letter");

        ResponseEntity<ApiResponse> response = applicationService.createApplication(applicationRequestDTO,20L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getMessage()).isEqualTo("Applied Position Name Not Found");

    }

    @Test
    void  createApplications_shouldHandleNullCandidateId() throws Exception{
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(null);
        firstCandidate.setEmail("mockEmail@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneNumber")
                .linkedinUrl("mocklinkedin")
                .instagramUrl("mockİnstagram")
                .facebookUrl("mockFacebook")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName("mockLastname");
        firstCandidate.setFirstName("mockFirstname");
        when(candidateRepository.findById(null)).thenReturn(Optional.of(firstCandidate));


        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        applicationRequestDTO.setApplicationStatus(ApplicationStatus.APPLIED);
        applicationRequestDTO.setCandidateId(null);
        applicationRequestDTO.setJobPostingId(20L);
        applicationRequestDTO.setApplicationDate(LocalDate.now());
        applicationRequestDTO.setCoverLetter("Cover Letter");

        ResponseEntity<ApiResponse> response = applicationService.createApplication(applicationRequestDTO,20L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getMessage()).isEqualTo("Candidate Not Found");

    }

    @Test
    void  getApplicationsBasedOnJobId() throws Exception {
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(112L);
        firstCandidate.setEmail("mockEmailBasedOnJobI@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneBasedOnJobINumber")
                .linkedinUrl("mocklinkedinBasedOnJobI")
                .instagramUrl("mockİnstagramBasedOnJobI")
                .facebookUrl("mockFacebookBasedOnJobI")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName("mockLastnameBasedOnJobI");
        firstCandidate.setFirstName("mockFirstnameBasedOnJobI");
        Applications applications = new Applications();
        applications.setId(1L);
        applications.setApplicationDate(LocalDate.now());
        applications.setCandidate(firstCandidate);
        applications.setAppliedPosition("Software Engineer");
        applications.setCoverLetter("I am very interested in this position and believe my skills match the requirements.");
        applications.setApplicationStatus(ApplicationStatus.APPLIED);
        ApplicationDetailJobPostingDTO applicationDetailJobPostingDTO = new ApplicationDetailJobPostingDTO();

        ArrayList<Applications> applicationsArrayList = new ArrayList<>();
        applicationsArrayList.add(applications);
        when(applicationRepository.findAllByJobPostingId(50L)).thenReturn(applicationsArrayList);

        ResponseEntity<ApiResponse<?>> response = applicationService.getApplicationsBasedOnJobId(50L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(applications.getId(),applicationsArrayList.get(0).getId());
        assertEquals(applications.getCandidate(),applicationsArrayList.get(0).getCandidate());

    }

    @Test
    void  getApplicationsBasedOnJobIdShouldReturnEmptyList() throws Exception {

        when(applicationRepository.findAllByJobPostingId(50L)).thenReturn(Collections.emptyList());

        ResponseEntity<ApiResponse<?>> response = applicationService.getApplicationsBasedOnJobId(50L);
    List<ApplicationsOnSpesificJobPostingDTO> responseBody = (List<ApplicationsOnSpesificJobPostingDTO>) response.getBody().getData();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(0,responseBody.size());


    }

    @Test
    void  getApplicationsShouldFilterNullApplication() throws Exception {
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(112L);
        firstCandidate.setEmail("mockEmailBasedOnJobI@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneBasedOnJobINumber")
                .linkedinUrl("mocklinkedinBasedOnJobI")
                .instagramUrl("mockİnstagramBasedOnJobI")
                .facebookUrl("mockFacebookBasedOnJobI")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName("mockLastnameBasedOnJobI");
        firstCandidate.setFirstName("mockFirstnameBasedOnJobI");
        Applications applications = new Applications();
        applications.setId(1L);
        applications.setApplicationDate(LocalDate.now());
        applications.setCandidate(firstCandidate);
        applications.setAppliedPosition("Software Engineer");
        applications.setCoverLetter("I am very interested in this position and believe my skills match the requirements.");
        applications.setApplicationStatus(ApplicationStatus.APPLIED);
        ApplicationDetailJobPostingDTO applicationDetailJobPostingDTO = new ApplicationDetailJobPostingDTO();

        ArrayList<Applications> applicationsArrayList = new ArrayList<>();
        applicationsArrayList.add(applications);
        applicationsArrayList.add(null);
        when(applicationRepository.findAllByJobPostingId(50L)).thenReturn(applicationsArrayList);

        ResponseEntity<ApiResponse<?>> response = applicationService.getApplicationsBasedOnJobId(50L);
        List<ApplicationsOnSpesificJobPostingDTO> responseBody = (List<ApplicationsOnSpesificJobPostingDTO>) response.getBody().getData();


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(1,responseBody.size());
        assertEquals(applications.getId(),responseBody.get(0).getApplicationId());
        assertEquals(applications.getCandidate().getId(),responseBody.get(0).getCandidateId());

    }

    @Test
    void  getApplicationsShouldHandleNullFields() throws Exception {
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(112L);
        firstCandidate.setEmail("mockEmailBasedOnJobI@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneBasedOnJobINumber")
                .linkedinUrl("mocklinkedinBasedOnJobI")
                .instagramUrl("mockİnstagramBasedOnJobI")
                .facebookUrl("mockFacebookBasedOnJobI")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName(null);
        firstCandidate.setFirstName(null);
        Applications applications = new Applications();
        applications.setId(1L);
        applications.setApplicationDate(LocalDate.now());
        applications.setCandidate(firstCandidate);
        applications.setAppliedPosition("Software Engineer");
        applications.setCoverLetter("I am very interested in this position and believe my skills match the requirements.");
        applications.setApplicationStatus(ApplicationStatus.APPLIED);
        ApplicationDetailJobPostingDTO applicationDetailJobPostingDTO = new ApplicationDetailJobPostingDTO();

        ArrayList<Applications> applicationsArrayList = new ArrayList<>();
        applicationsArrayList.add(applications);
        when(applicationRepository.findAllByJobPostingId(50L)).thenReturn(applicationsArrayList);

        ResponseEntity<ApiResponse<?>> response = applicationService.getApplicationsBasedOnJobId(50L);
        List<ApplicationsOnSpesificJobPostingDTO> responseBody = (List<ApplicationsOnSpesificJobPostingDTO>) response.getBody().getData();


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(1,responseBody.size());
        assertNull(applications.getCandidate().getFirstName());
        assertNull(applications.getCandidate().getLastName());


    }

    @Test
    void  shouldGetApplicationBasedOnJobId() throws Exception{
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(112L);
        firstCandidate.setEmail("mockEmailBasedOnJobI@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneBasedOnJobINumber")
                .linkedinUrl("mocklinkedinBasedOnJobI")
                .instagramUrl("mockİnstagramBasedOnJobI")
                .facebookUrl("mockFacebookBasedOnJobI")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName("mockLastnameBasedOnJobI");
        firstCandidate.setFirstName("mockFirstnameBasedOnJobI");
        when(candidateRepository.findById(112L)).thenReturn(Optional.of(firstCandidate));

        Applications applications = new Applications();
        applications.setId(1L);
        applications.setApplicationDate(LocalDate.now());
        applications.setAppliedPosition("Software Engineer");
        applications.setCoverLetter("I am very interested in this position and believe my skills match the requirements.");
        applications.setApplicationStatus(ApplicationStatus.APPLIED);

        Candidate candidate = new Candidate();
        candidate.setId(112L);
        candidate.setFirstName("John");
        candidate.setLastName("Doe");
        applications.setCandidate(candidate);

        applications.setJobPostingId(50L);

        when(applicationRepository.findByIdAndJobPostingId(50L,112L)).thenReturn(Optional.of(applications));


        ApplicationDetailJobPostingDTO applicationDetailJobPostingDTO = new ApplicationDetailJobPostingDTO();
        ResponseEntity<ApiResponse<?>> response = applicationService.getApplicationBasedOnJobId(50L,112L);
        ApplicationDetailJobPostingDTO applicationDetailJobPostingDTO1 = (ApplicationDetailJobPostingDTO) response.getBody().getData();


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(applications.getId(),applicationDetailJobPostingDTO1.getApplicationId());
        assertEquals(applications.getApplicationDate(),applicationDetailJobPostingDTO1.getApplicationDate());
        assertEquals(applications.getCandidate().getId(),applicationDetailJobPostingDTO1.getCandidateId());

    }

    @Test
    void  getApplicationBasedOnJobId_shouldHandleNoCandidate() throws Exception{
        when(candidateRepository.findById(112L)).thenReturn(Optional.empty());

        ApplicationDetailJobPostingDTO applicationDetailJobPostingDTO = new ApplicationDetailJobPostingDTO();
        ResponseEntity<ApiResponse<?>> response = applicationService.getApplicationBasedOnJobId(50L,112L);

        assertEquals("Candidate or Application not found",response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.value(),response.getBody().getStatus());

    }

    @Test
    void  getApplicationBasedOnJobId_shouldHandleNoApplications() throws Exception{
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(112L);
        firstCandidate.setEmail("mockEmailBasedOnJobI@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneBasedOnJobINumber")
                .linkedinUrl("mocklinkedinBasedOnJobI")
                .instagramUrl("mockİnstagramBasedOnJobI")
                .facebookUrl("mockFacebookBasedOnJobI")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName("mockLastnameBasedOnJobI");
        firstCandidate.setFirstName("mockFirstnameBasedOnJobI");
        when(candidateRepository.findById(112L)).thenReturn(Optional.of(firstCandidate));


        Applications applications = new Applications();
        applications.setId(1L);
        applications.setApplicationDate(LocalDate.now());
        applications.setAppliedPosition("Software Engineer");
        applications.setCoverLetter("I am very interested in this position and believe my skills match the requirements.");
        applications.setApplicationStatus(ApplicationStatus.APPLIED);
        ApplicationDetailJobPostingDTO applicationDetailJobPostingDTO = new ApplicationDetailJobPostingDTO();

        when(applicationRepository.findByIdAndJobPostingId(50L,112L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<?>> response = applicationService.getApplicationBasedOnJobId(50L,112L);

        assertEquals("Candidate or Application not found",response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.value(),response.getBody().getStatus());

    }

    @Test
    void  getApplicationBasedOnJobId_shouldHandleNullFields() throws Exception{
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(112L);
        firstCandidate.setEmail("mockEmailBasedOnJobI@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneBasedOnJobINumber")
                .linkedinUrl("mocklinkedinBasedOnJobI")
                .instagramUrl("mockİnstagramBasedOnJobI")
                .facebookUrl("mockFacebookBasedOnJobI")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName(null);
        firstCandidate.setFirstName(null);
        firstCandidate.setAddress(null);
        when(candidateRepository.findById(112L)).thenReturn(Optional.of(firstCandidate));

        Applications applications = new Applications();
        applications.setId(1L);
        applications.setApplicationDate(LocalDate.now());
        applications.setAppliedPosition("Software Engineer");
        applications.setCoverLetter("I am very interested in this position and believe my skills match the requirements.");
        applications.setApplicationStatus(ApplicationStatus.APPLIED);
        applications.setCandidate(firstCandidate);
        applications.setJobPostingId(50L);

        when(applicationRepository.findByIdAndJobPostingId(50L,112L)).thenReturn(Optional.of(applications));


        ApplicationDetailJobPostingDTO applicationDetailJobPostingDTO = new ApplicationDetailJobPostingDTO();
        ResponseEntity<ApiResponse<?>> response = applicationService.getApplicationBasedOnJobId(50L,112L);
        ApplicationDetailJobPostingDTO applicationDetailJobPostingDTO1 = (ApplicationDetailJobPostingDTO) response.getBody().getData();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(applications.getId(),applicationDetailJobPostingDTO1.getApplicationId());
        assertNull(applications.getCandidate().getAddress());
        assertNull(applications.getCandidate().getFirstName());
        assertNull(applications.getCandidate().getLastName());



    }

    @Test
    void getTheProperCandidates_whenValidRequest_shouldReturns200() throws  Exception{
        JobPostingResponseDTO jobPosting = new JobPostingResponseDTO();
        jobPosting.setJobTitle("Senior Java Developer");
        jobPosting.setJobDescription("We are looking for an experienced Java Developer to join our backend team.");
        jobPosting.setSalary(120000);
        jobPosting.setJobType(JobType.FULL_TIME);

        ArrayList<String> stringArrayListRequiredSkills = new ArrayList<>();
        stringArrayListRequiredSkills.add("Java");
        stringArrayListRequiredSkills.add("Kotlin");

        jobPosting.setRequiredSkillsList(stringArrayListRequiredSkills);
        jobPosting.setDepartment("Software Development");

        Location location = new Location();
        location.setCity("Istanbul");
        location.setCountry("Türkiye");
        jobPosting.setLocation(location);

        jobPosting.setJobStatus(JobStatus.LIVE);
        jobPosting.setJobPostingDeadline(new Date());

        when(jobPostingClient.getJobPostingById(50L)).thenReturn(new StandardResponse(true,"success",jobPosting,null,200));

        Candidate firstCandidate = new Candidate();

        firstCandidate.setId(112L);
        firstCandidate.setFirstName("mockFirstnameBasedOnJobI");
        firstCandidate.setLastName("mockLastnameBasedOnJobI");
        firstCandidate.setAddress(new Address("ankara","Türkiye","Balgat Oğuzlar Mahallesi"));
        firstCandidate.setEmail("mockEmailBasedOnJobI@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneBasedOnJobINumber")
                .linkedinUrl("mocklinkedinBasedOnJobI")
                .instagramUrl("mockİnstagramBasedOnJobI")
                .facebookUrl("mockFacebookBasedOnJobI")
                .build());
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Java");
        stringArrayList.add("Kotlin");
        firstCandidate.setSkills(stringArrayList);
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");


        firstCandidate.setCreatedAt("22.10.2025");

        ArrayList<Candidate> candidates = new ArrayList<>();
        candidates.add(firstCandidate);

        when(candidateRepository.findAll()).thenReturn(candidates);
;
        when(candidateMapper.fromCandidate(any(Candidate.class))).thenAnswer(invocation -> {
            Candidate candidate = invocation.getArgument(0);
            return new CandidateResponseDTO(
                    candidate.getId(),
                    candidate.getFirstName(),
                    candidate.getLastName(),
                    candidate.getAddress(),
                    candidate.getEmail(),
                    candidate.getConnections().getLinkedinUrl(),
                    candidate.getSkills(),
                    candidate.getConnections().getInstagramUrl(),
                    candidate.getConnections().getFacebookUrl(),
                    candidate.getConnections().getPhoneNumber(),
                    candidate.getCvUrl(),
                    candidate.getCreatedAt()
            );
        });
        ResponseEntity<ApiResponse> response = applicationService.getTheProperCandidates(50L);

        List<CandidateResponseDTO>  responseBody = (List<CandidateResponseDTO> ) response.getBody().getData();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(firstCandidate.getEmail()).isEqualTo(responseBody.get(0).getEmail());
        assertThat(firstCandidate.getFirstName()).isEqualTo(responseBody.get(0).getFirstName());
        assertThat(firstCandidate.getLastName()).isEqualTo(responseBody.get(0).getLastName());
    }


    @Test
    void getTheProperCandidates_shouldHandleJobPostingNotFound() throws  Exception{
        when(jobPostingClient.getJobPostingById(50L)).thenReturn( new StandardResponse(false,"Job Posting Not found",null,null,404) );


        ResponseEntity<ApiResponse> response = applicationService.getTheProperCandidates(50L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Job Posting Not found");
    }
    @Test
    void getTheProperCandidates_shouldHandleRequiredSkillsAreNull() throws  Exception{
        JobPostingResponseDTO jobPosting = new JobPostingResponseDTO();
        jobPosting.setJobTitle("Senior Java Developer");
        jobPosting.setJobDescription("We are looking for an experienced Java Developer to join our backend team.");
        jobPosting.setSalary(120000);
        jobPosting.setJobType(JobType.FULL_TIME);

        ArrayList<String> stringArrayListRequiredSkills = new ArrayList<>();
        jobPosting.setRequiredSkillsList(stringArrayListRequiredSkills);
        jobPosting.setDepartment("Software Development");

        Location location = new Location();
        location.setCity("Istanbul");
        location.setCountry("Türkiye");
        jobPosting.setLocation(location);

        jobPosting.setJobStatus(JobStatus.LIVE);
        jobPosting.setJobPostingDeadline(new Date());

        when(jobPostingClient.getJobPostingById(50L)).thenReturn( new StandardResponse(false,"Required Skills Are Empty",null,null,404) );


        ResponseEntity<ApiResponse> response = applicationService.getTheProperCandidates(50L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Required Skills Are Empty");

    }


    @Test
    void getTheProperCandidates_shouldHandleCandidateListEmpty() throws  Exception{
        JobPostingResponseDTO jobPosting = new JobPostingResponseDTO();
        jobPosting.setJobTitle("Senior Java Developer");
        jobPosting.setJobDescription("We are looking for an experienced Java Developer to join our backend team.");
        jobPosting.setSalary(120000);
        jobPosting.setJobType(JobType.FULL_TIME);

        ArrayList<String> stringArrayListRequiredSkills = new ArrayList<>();
        stringArrayListRequiredSkills.add("Java");
        stringArrayListRequiredSkills.add("Kotlin");

        jobPosting.setRequiredSkillsList(stringArrayListRequiredSkills);
        jobPosting.setDepartment("Software Development");

        Location location = new Location();
        location.setCity("Istanbul");
        location.setCountry("Türkiye");
        jobPosting.setLocation(location);

        jobPosting.setJobStatus(JobStatus.LIVE);
        jobPosting.setJobPostingDeadline(new Date());

        when(jobPostingClient.getJobPostingById(50L)).thenReturn(

                new StandardResponse(true,"success",jobPosting,null,200));

        when(candidateRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<ApiResponse> response = applicationService.getTheProperCandidates(50L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getMessage()).isEqualTo("Candidate List is empty");

    }

    @Test
    void getTheProperCandidates_whenValidRequest_shouldHandleCandidateSkillsAreEmpty() throws  Exception{
        JobPostingResponseDTO jobPosting = new JobPostingResponseDTO();
        jobPosting.setJobTitle("Senior Java Developer");
        jobPosting.setJobDescription("We are looking for an experienced Java Developer to join our backend team.");
        jobPosting.setSalary(120000);
        jobPosting.setJobType(JobType.FULL_TIME);

        ArrayList<String> stringArrayListRequiredSkills = new ArrayList<>();
        stringArrayListRequiredSkills.add("Java");
        stringArrayListRequiredSkills.add("Kotlin");

        jobPosting.setRequiredSkillsList(stringArrayListRequiredSkills);
        jobPosting.setDepartment("Software Development");

        Location location = new Location();
        location.setCity("Istanbul");
        location.setCountry("Türkiye");
        jobPosting.setLocation(location);

        jobPosting.setJobStatus(JobStatus.LIVE);
        jobPosting.setJobPostingDeadline(new Date());

        when(jobPostingClient.getJobPostingById(50L)).thenReturn(new StandardResponse(true,"success",jobPosting,null,200));

        Candidate firstCandidate = new Candidate();

        firstCandidate.setId(112L);
        firstCandidate.setFirstName("mockFirstnameBasedOnJobI");
        firstCandidate.setLastName("mockLastnameBasedOnJobI");
        firstCandidate.setAddress(new Address("ankara","Türkiye","Balgat Oğuzlar Mahallesi"));
        firstCandidate.setEmail("mockEmailBasedOnJobI@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneBasedOnJobINumber")
                .linkedinUrl("mocklinkedinBasedOnJobI")
                .instagramUrl("mockİnstagramBasedOnJobI")
                .facebookUrl("mockFacebookBasedOnJobI")
                .build());
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Java");
        stringArrayList.add("Kotlin");
        firstCandidate.setSkills(stringArrayList);
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");


        firstCandidate.setCreatedAt("22.10.2025");
        Candidate secondCandidate = new Candidate();

        secondCandidate.setId(113L);
        secondCandidate.setFirstName("mockSecondCandidateFirstname");
        secondCandidate.setLastName("mockSecondCandidateLastname");
        secondCandidate.setAddress(new Address("adana","Türkiye","Kenan Evren Mahallesi"));
        secondCandidate.setEmail("mockSecondCandidate@gmail.com");
        secondCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneNumberSecondCandidate")
                .linkedinUrl("mockLinkedinUrlSecondCandidate")
                .instagramUrl("mockİnstagramSecondCandidate")
                .facebookUrl("mockFacebookSecondCnadidate")
                .build());

        secondCandidate.setSkills(Collections.emptyList());
        secondCandidate.setCvUrl("https://example.com/cv2.pdf");
        secondCandidate.setSkills(Collections.emptyList());
        secondCandidate.setCreatedAt("02.10.2025");


        ArrayList<Candidate> candidates = new ArrayList<>();
        candidates.add(firstCandidate);
        candidates.add(secondCandidate);

        when(candidateRepository.findAll()).thenReturn(candidates);
        List<CandidateResponseDTO> candidateResponseDTO = candidates.stream()
                .map(candidateMapper::fromCandidate).toList();
        when(candidateMapper.fromCandidate(any(Candidate.class))).thenAnswer(invocation -> {
            Candidate candidate = invocation.getArgument(0);
            return new CandidateResponseDTO(
                    candidate.getId(),
                    candidate.getFirstName(),
                    candidate.getLastName(),
                    candidate.getAddress(),
                    candidate.getEmail(),
                    candidate.getConnections().getLinkedinUrl(),
                    candidate.getSkills(),
                    candidate.getConnections().getInstagramUrl(),
                    candidate.getConnections().getFacebookUrl(),
                    candidate.getConnections().getPhoneNumber(),
                    candidate.getCvUrl(),
                    candidate.getCreatedAt()
            );
        });
        ResponseEntity<ApiResponse> response = applicationService.getTheProperCandidates(50L);

        List<CandidateResponseDTO>  responseBody = (List<CandidateResponseDTO> ) response.getBody().getData();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(responseBody.size(),1);
        assertThat(firstCandidate.getEmail()).isEqualTo(responseBody.get(0).getEmail());
        assertThat(firstCandidate.getFirstName()).isEqualTo(responseBody.get(0).getFirstName());
        assertThat(firstCandidate.getLastName()).isEqualTo(responseBody.get(0).getLastName());

    }

    @Test
    void getTheProperCandidates_whenValidRequest_shouldHandleCandidateSkillsAreNotMatch() throws  Exception{
        JobPostingResponseDTO jobPosting = new JobPostingResponseDTO();
        jobPosting.setJobTitle("Senior Java Developer");
        jobPosting.setJobDescription("We are looking for an experienced Java Developer to join our backend team.");
        jobPosting.setSalary(120000);
        jobPosting.setJobType(JobType.FULL_TIME);

        ArrayList<String> stringArrayListRequiredSkills = new ArrayList<>();
        stringArrayListRequiredSkills.add("React");
        stringArrayListRequiredSkills.add("Angular");

        jobPosting.setRequiredSkillsList(stringArrayListRequiredSkills);
        jobPosting.setDepartment("Software Development");

        Location location = new Location();
        location.setCity("Istanbul");
        location.setCountry("Türkiye");
        jobPosting.setLocation(location);

        jobPosting.setJobStatus(JobStatus.LIVE);
        jobPosting.setJobPostingDeadline(new Date());

        when(jobPostingClient.getJobPostingById(50L)).thenReturn(new StandardResponse(true,"success",jobPosting,null,200));

        Candidate firstCandidate = new Candidate();

        firstCandidate.setId(112L);
        firstCandidate.setFirstName("mockFirstnameBasedOnJobI");
        firstCandidate.setLastName("mockLastnameBasedOnJobI");
        firstCandidate.setAddress(new Address("ankara","Türkiye","Balgat Oğuzlar Mahallesi"));
        firstCandidate.setEmail("mockEmailBasedOnJobI@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneBasedOnJobINumber")
                .linkedinUrl("mocklinkedinBasedOnJobI")
                .instagramUrl("mockİnstagramBasedOnJobI")
                .facebookUrl("mockFacebookBasedOnJobI")
                .build());
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Java");
        stringArrayList.add("Kotlin");
        firstCandidate.setSkills(stringArrayList);
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");


        firstCandidate.setCreatedAt("22.10.2025");

        ArrayList<Candidate> candidates = new ArrayList<>();
        candidates.add(firstCandidate);

        when(candidateRepository.findAll()).thenReturn(candidates);

        ResponseEntity<ApiResponse> response = applicationService.getTheProperCandidates(50L);

        List<CandidateResponseDTO>  responseBody = (List<CandidateResponseDTO> ) response.getBody().getData();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(responseBody.size(),0);

    }

    @Test
    void updateTheCandidateApplicationStatus_whenValidRequest_shouldReturns200() throws  Exception{
        Candidate firstCandidate = new Candidate();

        firstCandidate.setId(112L);
        firstCandidate.setFirstName("mockFirstnameBasedOnJobI");
        firstCandidate.setLastName("mockLastnameBasedOnJobI");
        firstCandidate.setAddress(new Address("ankara","Türkiye","Balgat Oğuzlar Mahallesi"));
        firstCandidate.setEmail("mockEmailBasedOnJobI@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneBasedOnJobINumber")
                .linkedinUrl("mocklinkedinBasedOnJobI")
                .instagramUrl("mockİnstagramBasedOnJobI")
                .facebookUrl("mockFacebookBasedOnJobI")
                .build());
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Java");
        stringArrayList.add("Kotlin");
        firstCandidate.setSkills(stringArrayList);
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");


        firstCandidate.setCreatedAt("22.10.2025");
        Applications application = new Applications();
        application.setId(105L);
        application.setApplicationDate(LocalDate.now());
        application.setAppliedPosition("Java Core Developer");
        application.setCoverLetter("cover letter mock");
        application.setApplicationStatus(ApplicationStatus.APPLIED);
        application.setCandidate(firstCandidate);
        application.setJobPostingId(4502L);

        when(applicationRepository.findByCandidateId(application.getCandidate().getId())).thenReturn(Optional.of(application));

        ApplicationStatus applicationStatus = ApplicationStatus.INTERVIEW_SCHEDULED;

        ResponseEntity<ApiResponse> response = applicationService.updateTheCandidateApplicationStatus(112L,ApplicationStatus.INTERVIEW_SCHEDULED);

        ApplicationStatus  responseBody = (ApplicationStatus) response.getBody().getData();

        assertThat(response.getBody().getData()).isEqualTo(applicationStatus);

    }

    @Test
    void updateTheCandidateApplicationStatus_shouldHandleEmptyApplications() throws  Exception{

        when(applicationRepository.findByCandidateId(anyLong())).thenReturn(Optional.empty());

        ApplicationStatus applicationStatus = ApplicationStatus.INTERVIEW_SCHEDULED;

        ResponseEntity<ApiResponse> response = applicationService.updateTheCandidateApplicationStatus(112L,ApplicationStatus.INTERVIEW_SCHEDULED);

        assertThat(response.getBody().getMessage()).isEqualTo("There is no application spesified on candidate");

    }


    @Test
    void updateTheCandidateApplicationStatus_shouldHandleNegativeCandidateId() throws  Exception{


        ResponseEntity<ApiResponse> response = applicationService.updateTheCandidateApplicationStatus(-1L,ApplicationStatus.INTERVIEW_SCHEDULED);



        assertThat(response.getBody().getStatus()).isEqualTo(409);

    }


}
