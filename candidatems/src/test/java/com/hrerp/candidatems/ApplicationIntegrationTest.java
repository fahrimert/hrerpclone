package com.hrerp.candidatems;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrerp.candidatems.dto.ApiResponse;
import com.hrerp.candidatems.dto.ApplicationRequestDTO;
import com.hrerp.candidatems.dto.ApplicationStatusUpdateDTO;
import com.hrerp.candidatems.jobPosting.JobPostingClient;
import com.hrerp.candidatems.mapper.ApplicationMapper;
import com.hrerp.candidatems.mapper.CandidateMapper;
import com.hrerp.candidatems.model.*;
import com.hrerp.candidatems.repository.ApplicationRepository;
import com.hrerp.candidatems.repository.CandidateRepository;
import com.hrerp.candidatems.service.ApplicationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT ,
        properties = {
                "eureka.client.enabled=false",
                "spring.cloud.discovery.enabled=false"
        })
@Transactional
@AutoConfigureMockMvc


public class ApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JobPostingClient jobPostingClient;

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private CandidateMapper candidateMapper;


    @Test
    @Transactional
    void shouldReturn200_whenCreateApplication() throws Exception{
        Candidate firstCandidate = new Candidate();
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
        Candidate savedCandidate = candidateRepository.save(firstCandidate);

        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        applicationRequestDTO.setId(15L);
        applicationRequestDTO.setCoverLetter("mock cover letter");
        applicationRequestDTO.setApplicationStatus(ApplicationStatus.APPLIED);
        applicationRequestDTO.setCandidateId(savedCandidate.getId());
        applicationRequestDTO.setJobPostingId(56L);



        ResponseEntity<ApiResponse> candidateList = applicationService.createApplication(applicationRequestDTO,56L);


        String requestBody = new ObjectMapper().writeValueAsString(applicationRequestDTO);

        mockMvc.perform(post("/api/v1/applications/createApplication/{jobPostingId}" ,56L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(jsonPath("$.data.coverLetter").value(applicationRequestDTO.getCoverLetter()))
                .andExpect(jsonPath("$.data.applicationStatus").value("APPLIED"))
                .andExpect(jsonPath("$.data.candidate.id").value(savedCandidate.getId()))
                .andExpect(status().isOk());

}

    @Test
    @Transactional
    void shouldHandleNullCandidate_whenCreateApplication() throws Exception{
        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        applicationRequestDTO.setId(15L);
        applicationRequestDTO.setCoverLetter("mock cover letter");
        applicationRequestDTO.setApplicationStatus(ApplicationStatus.APPLIED);
        applicationRequestDTO.setCandidateId(ArgumentMatchers.anyLong());
        applicationRequestDTO.setJobPostingId(56L);

        ResponseEntity<ApiResponse> candidateList = applicationService.createApplication(applicationRequestDTO,56L);


        String requestBody = new ObjectMapper().writeValueAsString(applicationRequestDTO);

        mockMvc.perform(post("/api/v1/applications/createApplication/{jobPostingId}" ,56L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Candidate Not Found"))
                .andExpect(status().isConflict());

    }

    @Test
    @Transactional
    void shouldHandleAppliedPositionNameNotFound_whenCreateApplication() throws Exception{
        Candidate firstCandidate = new Candidate();
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
        Candidate savedCandidate = candidateRepository.save(firstCandidate);

        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        applicationRequestDTO.setId(15L);
        applicationRequestDTO.setCoverLetter("mock cover letter");
        applicationRequestDTO.setApplicationStatus(ApplicationStatus.APPLIED);
        applicationRequestDTO.setCandidateId(savedCandidate.getId());
        applicationRequestDTO.setJobPostingId(3123123L);

        ResponseEntity<ApiResponse> candidateList = applicationService.createApplication(applicationRequestDTO,3123123L);


        String requestBody = new ObjectMapper().writeValueAsString(applicationRequestDTO);

        mockMvc.perform(post("/api/v1/applications/createApplication/{jobPostingId}" ,3123123L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(status().isConflict());

    }


    @Test
    @Transactional
    void shouldHandleAlreadyAppliedToPosition_whenCreateApplication() throws Exception{
        Candidate firstCandidate = new Candidate();
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
        applications.setAppliedPosition("Java Core Developer");
        applications.setApplicationDate(LocalDate.now());
        applications.setCandidate(firstCandidate);
        applications.setCoverLetter("mock Cover Letter");
        applications.setApplicationStatus(ApplicationStatus.APPLIED);
        applications.setJobPostingId(56L);

        ArrayList<Applications> applicationsArrayList = new ArrayList<>();
        applicationsArrayList.add(applications);
        firstCandidate.setApplications(applicationsArrayList);

        Candidate savedCandidate = candidateRepository.save(firstCandidate);

        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        applicationRequestDTO.setId(15L);
        applicationRequestDTO.setCoverLetter("mock cover letter");
        applicationRequestDTO.setApplicationStatus(ApplicationStatus.APPLIED);
        applicationRequestDTO.setCandidateId(savedCandidate.getId());
        applicationRequestDTO.setJobPostingId(56L);

        ResponseEntity<ApiResponse> candidateList = applicationService.createApplication(applicationRequestDTO,56L);


        String requestBody = new ObjectMapper().writeValueAsString(applicationRequestDTO);

        mockMvc.perform(post("/api/v1/applications/createApplication/{jobPostingId}" ,56L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Halihazırdaki şirkete başvurunuz değerlendirilmektedir"))
                .andExpect(status().isConflict());

    }


    @Test
    @Transactional
    void shouldReturn200_whenGetApplicationsBasedOnJobId() throws Exception{
        Candidate firstCandidate = new Candidate();
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
        firstCandidate.setCreatedAt("21.07.2025");

        Candidate secondCandidate = new Candidate();

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
        secondCandidate.setCreatedAt("02.10.2025");
        Candidate savedFirstCandidate = candidateRepository.save(firstCandidate);
        Candidate savedSecondCandidate = candidateRepository.save(secondCandidate);
        Applications application1 = new Applications();
        application1.setAppliedPosition("Java Core Developer");
        application1.setApplicationDate(LocalDate.now());
        application1.setCandidate(firstCandidate);
        application1.setCoverLetter("mock Cover Letter");
        application1.setApplicationStatus(ApplicationStatus.APPLIED);
        application1.setJobPostingId(56L);

        Applications application2 = new Applications();
        application2.setAppliedPosition("React Developer");
        application2.setApplicationDate(LocalDate.now());
        application2.setCandidate(secondCandidate);
        application2.setCoverLetter("mock Cover Letter");
        application2.setApplicationStatus(ApplicationStatus.APPLIED);
        application2.setJobPostingId(56L);


        ArrayList<Applications> applicationsArrayList = new ArrayList<>();
        applicationRepository.save(application1);
        applicationRepository.save(application2);


        ResponseEntity<ApiResponse> applicationList = applicationService.getApplicationsBasedOnJobId(56L);


        mockMvc.perform(get("/api/v1/applications/{jobId}/getApplications" ,56L)
                )
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void shouldHandleNoApplications_whenGetApplicationsBasedOnJobId() throws Exception{
        ArrayList<Applications> applicationsArrayList = new ArrayList<>();
        ResponseEntity<ApiResponse> applicationList = applicationService.getApplicationsBasedOnJobId(56L);
        mockMvc.perform(get("/api/v1/applications/{jobId}/getApplications" ,56L)
                )
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(status().isOk());

    }

    @Test
    @Transactional
    void shouldReturn200_whenGetApplicationBasedOnJobId() throws Exception{
        Candidate firstCandidate = new Candidate();
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
        firstCandidate.setCreatedAt("21.07.2025");

        Applications application1 = new Applications();
        application1.setAppliedPosition("Java Core Developer");
        application1.setApplicationDate(LocalDate.now());
        application1.setCandidate(firstCandidate);
        application1.setCoverLetter("mock Cover Letter");
        application1.setApplicationStatus(ApplicationStatus.APPLIED);
        application1.setJobPostingId(56L);
        ArrayList<Applications> applicationsArrayList = new ArrayList<>();
        applicationsArrayList.add(application1);
        firstCandidate.setApplications(applicationsArrayList);
        Candidate savedCandidate = candidateRepository.save(firstCandidate);

        ResponseEntity<ApiResponse<?>> applicationSingle = applicationService.getApplicationBasedOnJobId(56L,savedCandidate.getId());

                mockMvc.perform(get("/api/v1/applications/{jobPostingId}/getApplication/{candidateId}" ,56L,savedCandidate.getId())
                )
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.applicationId").exists())
                .andExpect(jsonPath("$.data.candidateId").exists())
                .andExpect(status().isOk());

    }

    @Test
    @Transactional
    void shouldHandleNoCandidate_whenGetApplicationBasedOnJobId() throws Exception{
        Applications application1 = new Applications();
        application1.setAppliedPosition("Java Core Developer");
        application1.setApplicationDate(LocalDate.now());
        application1.setCandidate(null);
        application1.setCoverLetter("mock Cover Letter");
        application1.setApplicationStatus(ApplicationStatus.APPLIED);
        application1.setJobPostingId(56L);
        ArrayList<Applications> applicationsArrayList = new ArrayList<>();
        applicationsArrayList.add(application1);
        ResponseEntity<ApiResponse<?>> applicationSingle = applicationService.getApplicationBasedOnJobId(56L,ArgumentMatchers.anyLong());

        mockMvc.perform(get("/api/v1/applications/{jobPostingId}/getApplication/{candidateId}" ,56L,ArgumentMatchers.anyLong())
                )
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Candidate or Application not found"))
                .andExpect(jsonPath("$.data").doesNotExist());

    }

    @Test
    @Transactional
    void shouldHandleNoApplication_whenGetApplicationBasedOnJobId() throws Exception{

        ResponseEntity<ApiResponse<?>> applicationSingle = applicationService.getApplicationBasedOnJobId(ArgumentMatchers.anyLong(),ArgumentMatchers.anyLong());


        mockMvc.perform(get("/api/v1/applications/{jobPostingId}/getApplication/{candidateId}" ,ArgumentMatchers.anyLong(),ArgumentMatchers.anyLong())
                )
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Candidate or Application not found"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @Transactional
    void shouldHandleNullFields_whenGetApplicationBasedOnJobId() throws Exception{
        Candidate firstCandidate = new Candidate();
        firstCandidate.setEmail("mockEmailBasedOnJobI@gmail.com");
        firstCandidate.setConnections(null);
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName(null);
        firstCandidate.setFirstName(null);
        firstCandidate.setCreatedAt("21.07.2025");

        Applications application1 = new Applications();
        application1.setAppliedPosition("Java Core Developer");
        application1.setApplicationDate(LocalDate.now());
        application1.setCandidate(firstCandidate);
        application1.setCoverLetter("mock Cover Letter");
        application1.setApplicationStatus(ApplicationStatus.APPLIED);
        application1.setJobPostingId(56L);
        ArrayList<Applications> applicationsArrayList = new ArrayList<>();
        applicationsArrayList.add(application1);
        firstCandidate.setApplications(applicationsArrayList);
        Candidate savedCandidate = candidateRepository.save(firstCandidate);

        ResponseEntity<ApiResponse<?>> applicationSingle = applicationService.getApplicationBasedOnJobId(56L,savedCandidate.getId());

        mockMvc.perform(get("/api/v1/applications/{jobPostingId}/getApplication/{candidateId}" ,56L,savedCandidate.getId())
                )
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.candidateCity").doesNotExist())
                .andExpect(jsonPath("$.data.linkedinUrl").doesNotExist())
                .andExpect(jsonPath("$.data.instagramUrl").doesNotExist())
                .andExpect(jsonPath("$.data.candidateCountry").doesNotExist())
                .andExpect(status().isOk());
    }

    //controller servisde falan da öyle yapmışım yani nullsa null kaydediyor böyle mi olması gerekiyor emin ddeğilim yaparken böyle yapmışım

    @Test
    @Transactional
    void shouldReturn200_whenupdateTheCandidateApplicationStatus() throws Exception{
        Candidate firstCandidate = new Candidate();
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
        firstCandidate.setCreatedAt("21.07.2025");

        Candidate savedFirstCandidate = candidateRepository.save(firstCandidate);
        Applications application1 = new Applications();
        application1.setAppliedPosition("Java Core Developer");
        application1.setApplicationDate(LocalDate.now());
        application1.setCandidate(firstCandidate);
        application1.setCoverLetter("mock Cover Letter");
        application1.setApplicationStatus(ApplicationStatus.APPLIED);
        application1.setJobPostingId(56L);

        applicationRepository.save(application1);
        ResponseEntity<ApiResponse> applicationList = applicationService.updateTheCandidateApplicationStatus(savedFirstCandidate.getId(),ApplicationStatus.APPLIED);

        ApplicationStatusUpdateDTO applicationStatusUpdateDTO = new ApplicationStatusUpdateDTO();
        applicationStatusUpdateDTO.setApplicationStatus(ApplicationStatus.APPLIED);
        String requestBody = new ObjectMapper().writeValueAsString(applicationStatusUpdateDTO);

        mockMvc.perform(put("/api/v1/applications/{candidateId}/updateTheCandidateApplicationStatus" ,savedFirstCandidate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value("APPLIED"))
                .andExpect(status().isOk());

    }

    @Test
    @Transactional
    void shouldHandleNoApplication_whenupdateTheCandidateApplicationStatus() throws Exception{
        Candidate firstCandidate = new Candidate();
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
        firstCandidate.setCreatedAt("21.08.2025");
        ArrayList<Applications> applicationsEmptyArrayList = new ArrayList<>();
        firstCandidate.setApplications(applicationsEmptyArrayList);

        Candidate savedCandidate = candidateRepository.save(firstCandidate);
        ApplicationStatusUpdateDTO applicationStatusUpdateDTO = new ApplicationStatusUpdateDTO();
        applicationStatusUpdateDTO.setApplicationStatus(ApplicationStatus.APPLIED);
        String requestBody = new ObjectMapper().writeValueAsString(applicationStatusUpdateDTO);

        mockMvc.perform(put("/api/v1/applications/{candidateId}/updateTheCandidateApplicationStatus",savedCandidate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("There is no application spesified on candidate"))
                .andExpect(status().isConflict());

    }



}
