package com.hrerp.candidatems;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrerp.candidatems.controller.ApplicationController;
import com.hrerp.candidatems.dto.*;
import com.hrerp.candidatems.jobPosting.JobPostingClient;
import com.hrerp.candidatems.model.*;
import com.hrerp.candidatems.repository.ApplicationRepository;
import com.hrerp.candidatems.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ApplicationController.class)
public class ApplicationControllerTest {
    private static final String BASE_URL = "/api/v1/applications";
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ApplicationService applicationService;
    @MockitoBean
    private JobPostingClient jobPostingClient;

    @MockitoBean
    private ApplicationRepository applicationRepository;

    @Autowired
    private  ObjectMapper objectMapper;


    @Test
    void createApplications_whenValidRequest_shouldReturns201() throws Exception {
        ApplicationRequestDTO dto = new ApplicationRequestDTO(1L, LocalDate.now(), "Mock Cover letter", ApplicationStatus.APPLIED, 1L, 1L);

        Applications mockApplication = new Applications();
        mockApplication.setId(1L);
        mockApplication.setApplicationDate(LocalDate.now());
        mockApplication.setCoverLetter("Mock Cover letter");
        mockApplication.setApplicationStatus(ApplicationStatus.APPLIED);
        mockApplication.setJobPostingId(1L);


        when(applicationService.createApplication(dto, 1L))
                .thenReturn(ResponseEntity.ok(ApiResponse.success(mockApplication)));

        when(applicationRepository.save(mockApplication)).thenReturn(mockApplication);
        mockMvc.perform(post("/api/v1/applications/createApplication/{jobPostingId}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.coverLetter").value("Mock Cover letter"))
                .andExpect(jsonPath("$.data.applicationStatus").value("APPLIED"))
                .andExpect(jsonPath("$.data.jobPostingId").value(mockApplication.getJobPostingId()))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void createApplications_shouldHandleNoUser() throws Exception {
        ApplicationRequestDTO dto = new ApplicationRequestDTO(
                1L, LocalDate.now(), "Mock Cover letter", ApplicationStatus.APPLIED, 1L, 1L
        );

        Applications mockApplication = new Applications();
        when(applicationService.createApplication(dto, 1L))
                .thenReturn(ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("Candidate Not Found", List.of("error"),HttpStatus.CONFLICT)));

        mockMvc.perform(post("/api/v1/applications/createApplication/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.errors[0]").value("error"))
                .andExpect(jsonPath("$.message").value("Candidate Not Found"));
    }

    @Test
    void createApplications_shouldHandleNullCandidateId() throws Exception {
        ApplicationRequestDTO dto = new ApplicationRequestDTO(
                1L, LocalDate.now(), "Cover letter", ApplicationStatus.APPLIED, null, 1L
        );

        Applications mockApplication = new Applications();
        when(applicationService.createApplication(dto, 1L))
                .thenReturn(ResponseEntity.internalServerError().body(ApiResponse.error("Candidate ID must not be null",List.of("error"),HttpStatus.CONFLICT)));

        mockMvc.perform(post("/api/v1/applications/createApplication/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.candidateId").value("Candidate ID must not be null"));
    }

    @Test
    void createApplications_shouldHandleServerError() throws Exception {
        ApplicationRequestDTO dto = new ApplicationRequestDTO(
                1L, LocalDate.now(), "Mock Cover letter", ApplicationStatus.APPLIED, 132L, 1L
        );

        Applications mockApplication = new Applications();
        when(applicationService.createApplication(dto, 1L))
                .thenReturn(ResponseEntity.internalServerError().body(ApiResponse.error("Error",List.of("error"),HttpStatus.INTERNAL_SERVER_ERROR)));

        mockMvc.perform(post("/api/v1/applications/createApplication/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors[0]").value("error"))
                .andExpect(jsonPath("$.message").value("Error"));
    }

    @Test
    void createApplications_whenValidRequest_shouldHandleEmptyCoverLetter() throws Exception {
        ApplicationRequestDTO dto = new ApplicationRequestDTO(
                1L, LocalDate.now(), " ", ApplicationStatus.APPLIED, 1L, 1L
        );


        when(applicationService.createApplication(dto, 1L))
                .thenReturn(ResponseEntity.ok(ApiResponse.success(dto)));

        mockMvc.perform(post("/api/v1/applications/createApplication/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.coverLetter").value(" "))
                .andExpect(jsonPath("$.success").value(true));
    }


    @Test
    void getApplicationsBasedOnJobId_whenValidRequest_shouldReturns201() throws Exception {
        ApplicationsOnSpesificJobPostingDTO applicationsOnSpesificJobPostingDTO = new ApplicationsOnSpesificJobPostingDTO();
        applicationsOnSpesificJobPostingDTO.setApplicationId(150L);
        applicationsOnSpesificJobPostingDTO.setApplicationDate(LocalDate.now());
        applicationsOnSpesificJobPostingDTO.setCandidateId(123L);
        applicationsOnSpesificJobPostingDTO.setCandidateFullName("Controller unit test for mocking");
        applicationsOnSpesificJobPostingDTO.setCandidateEmail("mockEmailForControllerUnitTest@email.com");


        ArrayList<ApplicationsOnSpesificJobPostingDTO> applicationsOnSpesificJobPostingDTOS  = new ArrayList<>();
        applicationsOnSpesificJobPostingDTOS.add(applicationsOnSpesificJobPostingDTO);

        when(applicationService.getApplicationsBasedOnJobId(123L))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                        applicationsOnSpesificJobPostingDTOS
                )));

        mockMvc.perform(get("/api/v1/applications/{jobId}/getApplications" , 123L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void getApplicationsBasedOnJobId_whenValidRequest_shouldReturnEmptyList() throws Exception {

        ArrayList<ApplicationsOnSpesificJobPostingDTO> applicationsOnSpesificJobPostingDTOS  = new ArrayList<>();

        when(applicationService.getApplicationsBasedOnJobId(123L))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                        applicationsOnSpesificJobPostingDTOS
                )));

        mockMvc.perform(get("/api/v1/applications/{jobId}/getApplications" , 123L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void getApplicationsBasedOnJobId_whenValidRequest_shouldHandleNullFields() throws Exception {
        ApplicationsOnSpesificJobPostingDTO applicationsOnSpesificJobPostingDTO = new ApplicationsOnSpesificJobPostingDTO();
        applicationsOnSpesificJobPostingDTO.setApplicationDate(LocalDate.now());
        applicationsOnSpesificJobPostingDTO.setCandidateId(123L);
        applicationsOnSpesificJobPostingDTO.setCandidateFullName(null);
        applicationsOnSpesificJobPostingDTO.setCandidateEmail(null);
        ArrayList<ApplicationsOnSpesificJobPostingDTO> applicationsOnSpesificJobPostingDTOS  = new ArrayList<>();
        applicationsOnSpesificJobPostingDTOS.add(applicationsOnSpesificJobPostingDTO);

        when(applicationService.getApplicationsBasedOnJobId(123L))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                        applicationsOnSpesificJobPostingDTOS
                )));

        mockMvc.perform(get("/api/v1/applications/{jobId}/getApplications" , 123L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.candidateFullName").doesNotExist())
                .andExpect(jsonPath("$.data.candidateEmail").doesNotExist())
                .andExpect(jsonPath("$.data.applicationId").doesNotExist());

    }

    @Test
    void getApplicationBasedOnJobAndCandidateId_whenValidRequest_shouldReturns201() throws Exception {
        ApplicationDetailJobPostingDTO dto = new ApplicationDetailJobPostingDTO();

        dto.setApplicationId(101L);
        dto.setApplicationDate(LocalDate.of(2024, 8, 10));
        dto.setCandidateId(55L);
        dto.setCandidateFullName("Fahri Mert");
        dto.setCandidateEmail("fahri.mert@example.com");
        dto.setCoverLetter("I am very interested in this position and believe I am a strong candidate.");
        dto.setCandidateCity("Ankara");
        dto.setCandidateCountry("Turkey");
        dto.setCandidateAddress("Barış manço street, No:312, Çukurambar");
        dto.setLinkedinUrl("https://linkedin.com/in/fahrimert");
        dto.setInstagramUrl("https://instagram.com/fahrimert");
        dto.setFacebookUrl("https://facebook.com/fahrimert");
        dto.setPhoneNumber("3123312312412");


        when(applicationService.getApplicationBasedOnJobId(123L,123L))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                        dto
                )));

        mockMvc.perform(get("/api/v1/applications/{jobPostingId}/getApplication/{candidateId}" , 123L,123L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.candidateFullName").value(dto.getCandidateFullName()))
                .andExpect(jsonPath("$.data.candidateEmail").value(dto.getCandidateEmail()))
                .andExpect(jsonPath("$.data.coverLetter").value(dto.getCoverLetter()));
    }

    @Test
    void getApplicationBasedOnJobAndCandidateId_whenValidRequest_shouldHandleCandidateOrApplicationNotFound() throws Exception {
        ApplicationDetailJobPostingDTO dto = new ApplicationDetailJobPostingDTO();

        dto.setApplicationId(123124124L);
        dto.setApplicationDate(LocalDate.of(2024, 8, 10));
        dto.setCandidateId(3123124L);
        dto.setCandidateFullName("Fahri Mert");
        dto.setCandidateEmail("fahri.mert@example.com");
        dto.setCoverLetter("I am very interested in this position and believe I am a strong candidate.");
        dto.setCandidateCity("Ankara");
        dto.setCandidateCountry("Turkey");
        dto.setCandidateAddress("Barış manço street, No:312, Çukurambar");
        dto.setLinkedinUrl("https://linkedin.com/in/fahrimert");
        dto.setInstagramUrl("https://instagram.com/fahrimert");
        dto.setFacebookUrl("https://facebook.com/fahrimert");
        dto.setPhoneNumber("3123312312412");


        when(applicationService.getApplicationBasedOnJobId(1231241243L,3123124L))
                .thenReturn(ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Candidate or Application not found",
                                null,
                                HttpStatus.CONFLICT
                        )));

        mockMvc.perform(get("/api/v1/applications/{jobPostingId}/getApplication/{candidateId}" , 1231241243L,3123124L))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Candidate or Application not found"));
    }

    @Test
    void whenInvalidPathVariables_thenReturns400() throws Exception {
        mockMvc.perform(get("/api/v1/applications/invalid/getApplication/one"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTheProperCandidates_whenValidRequest_shouldReturns200() throws Exception {
        CandidateResponseDTO dto = new CandidateResponseDTO();

        dto.setId(1234L);
        dto.setFirstName("Fahri");
        dto.setLastName("Mert");

        Address address = new Address();
        address.setAddress("Ataturk Street, No:12, Kadikoy");
        address.setCity("Istanbul");
        address.setCountry("Turkey");

        dto.setAddress(address);

        dto.setEmail("fahri.mert@example.com");
        dto.setLinkedin_url("https://linkedin.com/in/fahrimert");
        dto.setInstagram_url("https://instagram.com/fahrimert");
        dto.setFacebook_url("https://facebook.com/fahrimert");
        dto.setSkills(List.of("Java","Cotlin"));
        dto.setPhoneNumber("+90 555 123 4567");
        dto.setCvUrl("https://example.com/cv/fahri_mert.pdf");
        dto.setCreatedAt("2024-08-10 10:30:00");


        ArrayList<CandidateResponseDTO> candidateResponseDTOS  = new ArrayList<>();
        candidateResponseDTOS.add(dto);

        when(applicationService.getTheProperCandidates(123L))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                        candidateResponseDTOS
                )));

        mockMvc.perform(get("/api/v1/applications/{jobPostingId}/getTheProperCandidates" , 123L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data",hasSize(1)))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void getTheProperCandidates_whenValidRequest_shouldHandleJobPostingNotFound() throws Exception {
        when(applicationService.getTheProperCandidates(123L))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(ApiResponse.error("error",List.of("Job Posting Not Found"),HttpStatus.NOT_FOUND)
                ));

        mockMvc.perform(get("/api/v1/applications/{jobPostingId}/getTheProperCandidates" , 123L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("error"))
                .andExpect(jsonPath("$.errors[0]").value("Job Posting Not Found"));
    }

    @Test
    void getTheProperCandidates_whenValidRequest_shouldHandleNoProperCandidates() throws Exception {
        when(applicationService.getTheProperCandidates(123L))

                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                        Collections.emptyList()
                )));

        mockMvc.perform(get("/api/v1/applications/{jobPostingId}/getTheProperCandidates" , 123L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data",hasSize(0)));
    }

    @Test
    void getTheProperCandidates_whenValidRequest_shouldHandleInvalidId() throws Exception {
        mockMvc.perform(get("/api/v1/applications/{jobPostingId}/getTheProperCandidates" , "abc"))
                .andExpect(status().isBadRequest());
    }




}


