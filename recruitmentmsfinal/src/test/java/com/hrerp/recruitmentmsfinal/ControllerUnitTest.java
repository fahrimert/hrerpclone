package com.hrerp.recruitmentmsfinal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hrerp.Client.CandidateClient;
import com.hrerp.Client.JobPostingClient;
import com.hrerp.controller.RecruitmentController;
import com.hrerp.dto.*;
import com.hrerp.dto.JobPostingDTOs.JobPostingRequestRecruiterSpesificDTO;
import com.hrerp.dto.JobPostingDTOs.JobPostingResponseRecruiterSpesificDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.*;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.EvaluateCaseStudyDTO.EvaluateCaseStudyInterviewDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.EvaluateCaseStudyDTO.EvaluationCaseStudyResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.InitiateCaseStudyDTO.InitialCaseStudyInterviewDataDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.InitiateCaseStudyDTO.InitiateCaseStudyResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.SolutionCaseStudyDTO.SolutionCaseStudyInterviewDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.SolutionCaseStudyDTO.SolutionCaseStudyResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos.InterviewInitiateRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos.InterviewInitiateResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos.RecruitmentProcessInitiateRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos.RecruitmentProcessInitiateResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InterviewScoreDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.InterviewTechicalResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.InterviewTechnicalInterviewRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.TechnicalKnowledgeScore;
import com.hrerp.dto.enums.Address;
import com.hrerp.dto.enums.ApplicationStatus;
import com.hrerp.model.Interview;
import com.hrerp.model.RecruitmentProcess;
import com.hrerp.model.enums.InterviewProcesses;
import com.hrerp.model.mapper.RecruitmentProcessMapper;
import com.hrerp.repository.RecruitmentProcessRepository;
import com.hrerp.service.OfferService;
import com.hrerp.service.RecruitmentProcessService;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecruitmentController.class)
@Import({SecurityConfig.class ,FeignAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = true)

@ActiveProfiles("test")

public class ControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private RecruitmentController controller;


    @MockitoBean
    private OfferService offerService;

    @MockitoBean
    private RecruitmentProcessService recruitmentProcessService;

    @MockitoBean
    private RecruitmentProcessRepository recruitmentProcessRepository;

    @MockitoBean
    private RecruitmentProcessMapper recruitmentProcessMapper;

    @MockitoBean
    private JobPostingClient jobPostingClient;


    @MockitoBean
    private CandidateClient candidateClient;


    private static final String BASE_URL = "/api/v1/recruitment/";


    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    //requestte null gönderemiyoruz buraya bi tane de null kontrollü yazalım
    @Test
    void shouldReturn200_whenUpdateRecruiterSpesificSectionsOnJobPosting() throws Exception{
        JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO = new JobPostingRequestRecruiterSpesificDTO();
        jobPostingRequestRecruiterSpesificDTO.setInternalJobId(12L);
        jobPostingRequestRecruiterSpesificDTO.setHiringManagerName("Mock HR manager name");
        jobPostingRequestRecruiterSpesificDTO.setReplacementFor(null);
        jobPostingRequestRecruiterSpesificDTO.setInternalHrNote("Internal Mock HR note");

        jobPostingRequestRecruiterSpesificDTO.setInternalPostingDate(LocalDateTime.now());
        jobPostingRequestRecruiterSpesificDTO.setReplacement(false);


        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDTO = new JobPostingResponseRecruiterSpesificDTO();
        jobPostingResponseRecruiterSpesificDTO.setInternalJobId(jobPostingRequestRecruiterSpesificDTO.getInternalJobId());
        jobPostingResponseRecruiterSpesificDTO.setHiringManagerName(jobPostingRequestRecruiterSpesificDTO.getHiringManagerName());
        jobPostingResponseRecruiterSpesificDTO.setReplacementFor(jobPostingRequestRecruiterSpesificDTO.getReplacementFor());
        jobPostingResponseRecruiterSpesificDTO.setInternalHrNote(jobPostingRequestRecruiterSpesificDTO.getInternalHrNote());
        jobPostingResponseRecruiterSpesificDTO.setInternalPostingDate(jobPostingRequestRecruiterSpesificDTO.getInternalPostingDate());
        jobPostingResponseRecruiterSpesificDTO.setReplacement(jobPostingRequestRecruiterSpesificDTO.isReplacement());


        when(jobPostingClient.recruiterSpesificUpdate(any(),anyLong())).thenReturn(

                ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(jobPostingResponseRecruiterSpesificDTO)

                ));
        ResponseEntity<ApiResponse>  response =        jobPostingClient.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO,1L);


        when(recruitmentProcessService.recruiterSpesificUpdate(any(),anyLong())).thenReturn(

                ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(jobPostingResponseRecruiterSpesificDTO)

                ));
        ResponseEntity<ApiResponse> responsee = recruitmentProcessService.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO,1L) ;

        ResponseEntity<ApiResponse> result = controller.updateRecruiterSpesificSectionsOnJobPosting(jobPostingRequestRecruiterSpesificDTO,1L);
        JobPostingResponseRecruiterSpesificDTO responseOfController = (JobPostingResponseRecruiterSpesificDTO) result.getBody().getData();

        assertEquals(jobPostingRequestRecruiterSpesificDTO.getInternalHrNote(), responseOfController.getInternalHrNote());
        assertEquals(jobPostingRequestRecruiterSpesificDTO.getReplacementFor(), responseOfController.getReplacementFor());
        assertEquals(jobPostingRequestRecruiterSpesificDTO.getHiringManagerName(), responseOfController.getHiringManagerName());

    }

    @Test
    void shouldHandleNoJobPosting_whenUpdateRecruiterSpesificSectionsOnJobPosting() throws Exception{
        JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO = new JobPostingRequestRecruiterSpesificDTO();
        jobPostingRequestRecruiterSpesificDTO.setInternalJobId(12L);
        jobPostingRequestRecruiterSpesificDTO.setHiringManagerName("Mock HR manager name");
        jobPostingRequestRecruiterSpesificDTO.setReplacementFor(null);
        jobPostingRequestRecruiterSpesificDTO.setInternalHrNote("Internal Mock HR note");

        jobPostingRequestRecruiterSpesificDTO.setInternalPostingDate(LocalDateTime.now());
        jobPostingRequestRecruiterSpesificDTO.setReplacement(false);

        when(jobPostingClient.recruiterSpesificUpdate(any(),anyLong())).thenReturn(

                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(
                                "Job Posting not found" ,
                                null,
                                HttpStatus.valueOf(404)

                )));
        ResponseEntity<ApiResponse>  response =        jobPostingClient.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO,1L);


        when(recruitmentProcessService.recruiterSpesificUpdate(any(),anyLong())).thenReturn(

                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(
                                "Job Posting not found" ,
                                null,
                                HttpStatus.valueOf(404)

                        )));
        ResponseEntity<ApiResponse> responsee = recruitmentProcessService.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO,1L) ;

        ResponseEntity<ApiResponse> result = controller.updateRecruiterSpesificSectionsOnJobPosting(jobPostingRequestRecruiterSpesificDTO,1L);
        JobPostingResponseRecruiterSpesificDTO responseOfController = (JobPostingResponseRecruiterSpesificDTO) result.getBody().getData();
        System.out.println(result.getBody().getMessage());
        System.out.println(responseOfController);
        //
        assertEquals(result.getBody().getMessage(), "Job Posting not found");

    }
    @Test
    void shouldHandleInvalidId_whenUpdateRecruiterSpesificSectionsOnJobPosting() throws Exception {
        mockMvc.perform(put("/api/v1/recruitment/-1/recruiterSpesificUpdate"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleInvalidStringId_whenUpdateRecruiterSpesificSectionsOnJobPosting() throws Exception {
        mockMvc.perform(put("/api/v1/recruitment/-abc/recruiterSpesificUpdate"))
                .andExpect(status().isBadRequest());
    }

    //null gönderme edge caselerini de servis ve integrsyon taraflarında yaparız

    @Test
    void shouldReturn200_whenFetchRecruiterSpesificFetch() throws Exception{
        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDTO = new JobPostingResponseRecruiterSpesificDTO();
        jobPostingResponseRecruiterSpesificDTO.setInternalJobId(1L);
        jobPostingResponseRecruiterSpesificDTO.setHiringManagerName("MockHiringManagerName");
        jobPostingResponseRecruiterSpesificDTO.setReplacementFor(null);
        jobPostingResponseRecruiterSpesificDTO.setInternalHrNote("MockHrNote");
        jobPostingResponseRecruiterSpesificDTO.setInternalPostingDate(LocalDateTime.now());

        when(recruitmentProcessService.recruiterSpesificFetch(1L)).thenReturn(ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(jobPostingResponseRecruiterSpesificDTO)));

        ResponseEntity<ApiResponse> result = controller.recruiterSpesificFetchJobPosting(1L);
        JobPostingResponseRecruiterSpesificDTO responseOfController = (JobPostingResponseRecruiterSpesificDTO) result.getBody().getData();

        assertEquals(jobPostingResponseRecruiterSpesificDTO.getInternalHrNote(), responseOfController.getInternalHrNote());
        assertEquals(jobPostingResponseRecruiterSpesificDTO.getReplacementFor(), responseOfController.getReplacementFor());
        assertEquals(jobPostingResponseRecruiterSpesificDTO.getHiringManagerName(), responseOfController.getHiringManagerName());



    }

    @Test
    void shouldJobPostingNotFound_whenFetchRecruiterSpesificFetch() throws Exception{

        when(recruitmentProcessService.recruiterSpesificFetch(1L)).thenReturn(

                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(
                                "Job Posting not found" ,
                                null,
                                HttpStatus.valueOf(404)

                        )));

        ResponseEntity<ApiResponse> result = controller.recruiterSpesificFetchJobPosting(1L);
        JobPostingResponseRecruiterSpesificDTO responseOfController = (JobPostingResponseRecruiterSpesificDTO) result.getBody().getData();

        System.out.println(responseOfController);
        System.out.println(result);
        System.out.println(result.getBody().getMessage());
        System.out.println(result);




    }

    @Test
    void shouldHandleInvalidStringId_whenFetchRecruiterSpesificFetchSectionsOnJobPosting() throws Exception {
        mockMvc.perform(get("/api/v1/recruitment/internal/ABC"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldHandleInvalidId_whenFetchRecruiterSpesificFetchSectionsOnJobPosting() throws Exception {
        mockMvc.perform(get("/api/v1/recruitment/internal/-1"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldReturn200_whenGetTheProperCandidates() throws Exception{

        CandidateResponseDTO candidateTestResponse1 =   new CandidateResponseDTO(
                112L,
                "mockFirstCandidateName",
"mockFirstCandidateLastName",
                Address.builder()
                        .city("mockCity")
                        .address("mockAddress")
                                .country("mockCountry")
                .                build(),
                "mockEmail@gmail.com",
"linkedin/mockfirstnaem.com.tr",
                "mockİnstagram/mockfirstnaem.com.tr",
                "mockFacebook/mockfirstnaem.com.tr",
                "mockPhoneNumber",
                    "mockCvUrl",
                " 2025-08-15 03:00:00"
        );

        CandidateResponseDTO candidateTestResponse2 =   new CandidateResponseDTO(
                155L,
                "mockSecondCandidateName",
                "mockSecondCandidateLastName",
                Address.builder()
                        .city("mockSecondCity")
                        .address("mockSecondAddress")
                        .country("mockSecondCountry")
                        .                build(),
                "mockSecondEmail@gmail.com",
                "linkedin/mockSecondname.com.tr",
                "mockİnstagram/mockSecondnaem.com.tr",
                "mockFacebook/mockSecondnaem.com.tr",
                "mockPhoneNumber",
                "mockCvUrl",
                " 2025-10-15 03:00:00"
        );

        ArrayList<CandidateResponseDTO> candidateResponseDTOS= new ArrayList<>();
        candidateResponseDTOS.add(candidateTestResponse1);
        candidateResponseDTOS.add(candidateTestResponse2);


        when(candidateClient.getTheProperCandidates(anyLong())).thenReturn(

                ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(candidateResponseDTOS)

                ));
        ResponseEntity<ApiResponse<?>> result = controller.getTheProperCandidates(10L);
         List<CandidateResponseDTO> candidateResponseDTOList = (List<CandidateResponseDTO>) result.getBody().getData();

        System.out.println(candidateResponseDTOList);
        System.out.println(result);
        assertEquals(candidateResponseDTOS.get(0).getFirstName(), candidateTestResponse1.getFirstName());
        assertEquals(candidateResponseDTOS.get(0).getEmail(), candidateTestResponse1.getEmail());
        assertEquals(candidateResponseDTOS.get(0).getLastName(), candidateTestResponse1.getLastName());

        assertEquals(candidateResponseDTOS.get(1).getFirstName(), candidateTestResponse2.getFirstName());
        assertEquals(candidateResponseDTOS.get(1).getEmail(), candidateTestResponse2.getEmail());
        assertEquals(candidateResponseDTOS.get(1).getLastName(), candidateTestResponse2.getLastName());
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    void shouldHandleNoJobPostings_whenGetTheProperCandidates() throws Exception{

        ArrayList<CandidateResponseDTO> candidateResponseDTOS= new ArrayList<>();
        when(recruitmentProcessService.getTheProperCandidates(10L))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(ApiResponse.error("error",List.of("Job Posting Not Found"),HttpStatus.NOT_FOUND)
                ));

        ResponseEntity<ApiResponse<?>> result = controller.getTheProperCandidates(10L);
        System.out.println(result.getBody().getMessage());
        assertEquals(result.getBody().getMessage(), "error");
        assertEquals("Job Posting Not Found", result.getBody().getErrors().get(0));

        assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    void shouldHandleCandidateListEmpty_whenGetTheProperCandidates() throws Exception{

        ArrayList<CandidateResponseDTO> candidateResponseDTOS= new ArrayList<>();
        when(recruitmentProcessService.getTheProperCandidates(10L))

                .thenReturn( ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("Candidate List is empty", null,HttpStatus.CONFLICT)));

        ResponseEntity<ApiResponse<?>> result = controller.getTheProperCandidates(10L);
        System.out.println(result.getBody().getMessage());
        assertEquals(result.getBody().getMessage(), "Candidate List is empty");

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());

    }
    //requires skills are nullu falan servis kısmıdna yapmışım o kısımda yapıcam burada da
    @Test
    void shouldHandleInvalidId_whenGetTheProperCandidates() throws Exception {
        mockMvc.perform(get("/api/v1/recruitment/internal/getTheProperCandidates/{jobPostingId}" , "abc"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleNegativeId_whenGetTheProperCandidates() throws Exception {
        mockMvc.perform(get("/api/v1/recruitment/internal/getTheProperCandidates/{jobPostingId}" , -1L))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200_whenUpdateTheCandidateApplicationStatus() throws Exception{
        ApplicationStatusUpdateDTO applicationStatusUpdateDTO = new ApplicationStatusUpdateDTO();

        applicationStatusUpdateDTO.setApplicationStatus(ApplicationStatus.APPLIED);


        when(candidateClient.updateTheCandidateApplicationStatus(6L,applicationStatusUpdateDTO)).thenReturn(

                ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(ApplicationStatus.APPLIED)

                ));
        ResponseEntity<ApiResponse> result = controller.updateTheCandidateApplicationStatus(6L,applicationStatusUpdateDTO);
        ApplicationStatus responseBody = (ApplicationStatus) result.getBody().getData();

        assertEquals(ApplicationStatus.APPLIED, responseBody);
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }
    @Test
    void shouldHandleNoCandidate_whenUpdateTheCandidateApplicationStatus() throws Exception{
        ApplicationStatusUpdateDTO applicationStatusUpdateDTO = new ApplicationStatusUpdateDTO();

        applicationStatusUpdateDTO.setApplicationStatus(ApplicationStatus.APPLIED);


        when(recruitmentProcessService.updateTheCandidateApplicationStatus(6L,applicationStatusUpdateDTO)).thenReturn(

                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "There is no application spesified on candidate",
                                null,
                                HttpStatus.CONFLICT
                )));
        ResponseEntity<ApiResponse> result = controller.updateTheCandidateApplicationStatus(6L,applicationStatusUpdateDTO);

        ApplicationStatus responseBody = (ApplicationStatus) result.getBody().getData();

        assertEquals("There is no application spesified on candidate", result.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());

    }

    @Test
    void shouldHandleNegativeId_whenUpdateTheCandidateApplicationStatus() throws Exception {
        ApplicationStatusUpdateDTO applicationStatusUpdateDTO = new ApplicationStatusUpdateDTO();
        applicationStatusUpdateDTO.setApplicationStatus(ApplicationStatus.APPLIED);
        mockMvc.perform(put("/api/v1/recruitment/internal/updateTheCandidateApplicationStatus/{candidateId}" , -1L )
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(applicationStatusUpdateDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleInvalidId_whenUpdateTheCandidateApplicationStatus() throws Exception {
        ApplicationStatusUpdateDTO applicationStatusUpdateDTO = new ApplicationStatusUpdateDTO();
        applicationStatusUpdateDTO.setApplicationStatus(ApplicationStatus.APPLIED);
        mockMvc.perform(put("/api/v1/recruitment/internal/updateTheCandidateApplicationStatus/{candidateId}" , "abc" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(applicationStatusUpdateDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }




    @Test
    void shouldReturn200_getTheRecruitmentProcesses() throws Exception{
            Interview interview = Interview.builder()
                    .id(100L)
                    .candidateId(200L)
                    .interviewRatingQuote("Pozitif bir izlenim bıraktı")
                    .interviewProcesses(InterviewProcesses.HR_SCREENING)
                    .interviewerName("Ahmet Yılmaz")
                    .processSpecificData("{\"caseStudyScore\":85}")
                    .caseStudyProcesses(null)
                    .interviewScore(85.0)
                    .interviewScheduleTime(new Date())
                    .createdAt(new Date())
                    .lastUpdated(LocalDateTime.now())
                    .build();
            InterviewInitiateResponseDTO interviewInitiateResponseDTO = new InterviewInitiateResponseDTO();
            interviewInitiateResponseDTO.setCandidateId(200L);
            interviewInitiateResponseDTO.setInterviewerName("Ahmet Yılmaz");
            interviewInitiateResponseDTO.setInterviewQuestions(null);
            interviewInitiateResponseDTO.setInterviewScore(5.6);
            interviewInitiateResponseDTO.setInterviewRatingQuote("candidate has a bright future");
            interviewInitiateResponseDTO.setInterviewScheduleTime(new Date());
            interviewInitiateResponseDTO.setCreatedAt(new Date());
            interviewInitiateResponseDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
            RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                    .id(10L)
                    .candidateId(200L)
                    .jobPostingId(300L)
                    .interviewProcesses(InterviewProcesses.HR_SCREENING)
                    .interviews(List.of(interview))
                    .createdAt(new Date())
                    .lastUpdated(LocalDateTime.now())
                    .build();
            ArrayList<RecruitmentProcess> recruitmentProcessArrayList = new ArrayList<>();
            recruitmentProcessArrayList.add(recruitmentProcess);


            RecruitmentProcessInitiateResponseDTO recruitmentProcessInitiateResponseDTO = new RecruitmentProcessInitiateResponseDTO();
            recruitmentProcessInitiateResponseDTO.setCandidateId(200L);
            recruitmentProcessInitiateResponseDTO.setInterviews(List.of(interviewInitiateResponseDTO));
            recruitmentProcessInitiateResponseDTO.setJobPostingId(300L);
            recruitmentProcessInitiateResponseDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
            recruitmentProcessInitiateResponseDTO.setCreatedAt(new Date());

            ArrayList<RecruitmentProcessInitiateResponseDTO> responseDTOS = new ArrayList<>();
            responseDTOS.add(recruitmentProcessInitiateResponseDTO);
            when(recruitmentProcessService.getTheProcessesBasedOnCandidate(200L)).thenReturn(

                    ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(responseDTOS)

                    ));

            ResponseEntity<ApiResponse> result = controller.getTheRecruitmentProcesses(200L);
            List<RecruitmentProcessInitiateResponseDTO> responseBody = (List<RecruitmentProcessInitiateResponseDTO>) result.getBody().getData();

            assertEquals(responseDTOS, responseBody);
            assertEquals(HttpStatus.OK, result.getStatusCode());

        }

    @Test
    void shouldHandleNoCandidate_whengetTheRecruitmentProcesses() throws Exception{

        when(recruitmentProcessService.getTheProcessesBasedOnCandidate(200L)).thenReturn(
                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Candidate Does Not Exists",
                                null,
                                HttpStatus.CONFLICT
                        )));

        ResponseEntity<ApiResponse> result = controller.getTheRecruitmentProcesses(200L);
        List<RecruitmentProcessInitiateResponseDTO> responseBody = (List<RecruitmentProcessInitiateResponseDTO>) result.getBody().getData();

        assertEquals("Candidate Does Not Exists", result.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());

    }

    @Test
    void shouldHandleCandidateDoesntHaveAnyProcessesExist_whengetTheRecruitmentProcesses() throws Exception{

        when(recruitmentProcessService.getTheProcessesBasedOnCandidate(200L)).thenReturn(
                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Candidate doesnt have any recruitment processes exist.",
                                null,
                                HttpStatus.CONFLICT
                        )));

        ResponseEntity<ApiResponse> result = controller.getTheRecruitmentProcesses(200L);
        List<RecruitmentProcessInitiateResponseDTO> responseBody = (List<RecruitmentProcessInitiateResponseDTO>) result.getBody().getData();

        assertEquals("Candidate doesnt have any recruitment processes exist.", result.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());

    }

    @Test
    void shouldHandleInternalServerError_whenGetTheRecruitmentProcesses() throws Exception{

        when(recruitmentProcessService.getTheProcessesBasedOnCandidate(200L)).thenReturn(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error(
                                "Server Error.",
                                null,
                                HttpStatus.INTERNAL_SERVER_ERROR
                        )));

        ResponseEntity<ApiResponse> result = controller.getTheRecruitmentProcesses(200L);
        List<RecruitmentProcessInitiateResponseDTO> responseBody = (List<RecruitmentProcessInitiateResponseDTO>) result.getBody().getData();

        assertEquals("Server Error.", result.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());

    }



    @Test
    void shouldReturn200_getTheInduvualRecruitmentProcesses() throws Exception{
        Interview interview = Interview.builder()
                .id(100L)
                .candidateId(200L)
                .interviewRatingQuote("Pozitif bir izlenim bıraktı")
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviewerName("Ahmet Yılmaz")
                .processSpecificData("{\"caseStudyScore\":85}")
                .caseStudyProcesses(null)
                .interviewScore(85.0)
                .interviewScheduleTime(new Date())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();
        InterviewInitiateResponseDTO interviewInitiateResponseDTO = new InterviewInitiateResponseDTO();
        interviewInitiateResponseDTO.setCandidateId(200L);
        interviewInitiateResponseDTO.setInterviewerName("Ahmet Yılmaz");
        interviewInitiateResponseDTO.setInterviewQuestions(null);
        interviewInitiateResponseDTO.setInterviewScore(5.6);
        interviewInitiateResponseDTO.setInterviewRatingQuote("candidate has a bright future");
        interviewInitiateResponseDTO.setInterviewScheduleTime(new Date());
        interviewInitiateResponseDTO.setCreatedAt(new Date());
        interviewInitiateResponseDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .id(10L)
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviews(List.of(interview))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();


        RecruitmentProcessInitiateResponseDTO recruitmentProcessInitiateResponseDTO = new RecruitmentProcessInitiateResponseDTO();
        recruitmentProcessInitiateResponseDTO.setCandidateId(200L);
        recruitmentProcessInitiateResponseDTO.setInterviews(List.of(interviewInitiateResponseDTO));
        recruitmentProcessInitiateResponseDTO.setJobPostingId(300L);
        recruitmentProcessInitiateResponseDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
        recruitmentProcessInitiateResponseDTO.setCreatedAt(new Date());

        when(recruitmentProcessService.getTheInduvualRecruitmentProcesses(200L,122L)).thenReturn(

                ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(recruitmentProcessInitiateResponseDTO)

                ));

        ResponseEntity<ApiResponse> result = controller.getTheInduvualRecruitmentProcesses(200L,122L);
        RecruitmentProcessInitiateResponseDTO responseBody = (RecruitmentProcessInitiateResponseDTO) result.getBody().getData();

        assertEquals(recruitmentProcessInitiateResponseDTO, responseBody);
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    void shouldHandleRecruitmentProcessNotFOund_whenGetTheInduvualRecruitmentProcesses() throws Exception{
        when(recruitmentProcessService.getTheInduvualRecruitmentProcesses(200L,122L)).thenReturn(
                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Process not found ",
                                null,
                                HttpStatus.CONFLICT
                )));

        ResponseEntity<ApiResponse> result = controller.getTheInduvualRecruitmentProcesses(200L,122L);

        assertEquals(result.getBody().getMessage(), "Process not found ");
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());

    }

    @Test
    void shouldHandleCandidateDoesntHaveAnyRecruitmentProcessExists_whenGetTheInduvualRecruitmentProcesses() throws Exception{
        when(recruitmentProcessService.getTheInduvualRecruitmentProcesses(200L,122L)).thenReturn(
                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Candidate doesnt have any recruitment process exist.",
                                null,
                                HttpStatus.CONFLICT
                        )));

        ResponseEntity<ApiResponse> result = controller.getTheInduvualRecruitmentProcesses(200L,122L);

        assertEquals(result.getBody().getMessage(), "Candidate doesnt have any recruitment process exist.");
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());

    }

    @Test
    void shouldHandleCandidateDoesNotExists_whenGetTheInduvualRecruitmentProcesses() throws Exception{
        when(recruitmentProcessService.getTheInduvualRecruitmentProcesses(200L,122L)).thenReturn(
                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Candidate Does Not Exists",
                                null,
                                HttpStatus.CONFLICT
                        )));

        ResponseEntity<ApiResponse> result = controller.getTheInduvualRecruitmentProcesses(200L,122L);

        assertEquals(result.getBody().getMessage(), "Candidate Does Not Exists");
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());

    }

    @Test
    void shouldHandleInternalServerError_whenGetTheInduvualRecruitmentProcesses() throws Exception{
        when(recruitmentProcessService.getTheInduvualRecruitmentProcesses(200L,122L)).thenReturn(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error(
                                "Server Error.",
                                null,
                                HttpStatus.INTERNAL_SERVER_ERROR
                        )));

        ResponseEntity<ApiResponse> result = controller.getTheInduvualRecruitmentProcesses(200L,122L);

        assertEquals(result.getBody().getMessage(), "Server Error.");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());

    }


    @Test
    void shouldReturn200_whenInitiateRecruitmentProcess() throws Exception{
        RecruitmentProcessInitiateRequestDTO recruitmentProcessInitiateRequestDTO = new RecruitmentProcessInitiateRequestDTO();
        recruitmentProcessInitiateRequestDTO.setCandidateId(200L);
        recruitmentProcessInitiateRequestDTO.setCreatedAt(new Date());
        recruitmentProcessInitiateRequestDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
        recruitmentProcessInitiateRequestDTO.setJobPostingId(300L);
        InterviewInitiateRequestDTO interviewInitiateRequestDTO = new InterviewInitiateRequestDTO();

        InterviewInitiateRequestDTO dto = new InterviewInitiateRequestDTO();

        dto.setInterviewRatingQuote("Genel olarak başarılı bir görüşmeydi.");

// Burada scoreDTO’nun içindeki alanları da setlemen gerekir.

        dto.setCandidateId(123L);
        dto.setInterviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW); // Örneğin enum değeri
        dto.setInterviewerName("Ahmet Yılmaz");
        dto.setGeneralImpression("Pozitif");
        dto.setCandidateTeamCompabilityNote("Takım çalışmasına yatkın");

        dto.setInterviewQuestions(null);

        dto.setLocatedInTheSameCity(true);
        dto.setCandidateCanWorkInTheOffice(true);
        dto.setCandidateCareerGoals("Backend alanında uzmanlaşmak istiyor.");
        dto.setSalaryExpectation("35000 TL");
        dto.setAvailabilityToStart("2 hafta içinde başlayabilir.");

        dto.setInterviewScheduleTime("2025-07-25T14:00:00");

        dto.setCreatedAt(new Date());
        dto.setLastUpdated(LocalDateTime.now());
        Interview interview = Interview.builder()
                .id(100L)
                .candidateId(200L)
                .interviewRatingQuote("Pozitif bir izlenim bıraktı")
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviewerName("Ahmet Yılmaz")
                .processSpecificData("{\"caseStudyScore\":85}")
                .caseStudyProcesses(null)
                .interviewScore(85.0)
                .interviewScheduleTime(new Date())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();


        RecruitmentProcessInitiateResponseDTO recruitmentProcessInitiateResponseDTO = new RecruitmentProcessInitiateResponseDTO();
        recruitmentProcessInitiateResponseDTO.setCandidateId(200L);
        InterviewInitiateResponseDTO interviewInitiateResponseDTO = new InterviewInitiateResponseDTO();
        interviewInitiateResponseDTO.setCandidateId(200L);
        interviewInitiateResponseDTO.setInterviewerName("Ahmet Yılmaz");
        interviewInitiateResponseDTO.setInterviewQuestions(null);
        interviewInitiateResponseDTO.setInterviewScore(5.6);
        interviewInitiateResponseDTO.setInterviewRatingQuote("candidate has a bright future");
        interviewInitiateResponseDTO.setInterviewScheduleTime(new Date());
        interviewInitiateResponseDTO.setCreatedAt(new Date());
        interviewInitiateResponseDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
        recruitmentProcessInitiateResponseDTO.setInterviews(List.of(interviewInitiateResponseDTO));
        recruitmentProcessInitiateResponseDTO.setLastUpdated(LocalDateTime.now());
        recruitmentProcessInitiateResponseDTO.setCreatedAt(new Date());

        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .id(10L)
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviews(List.of(interview))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();


        when(recruitmentProcessService.initiateRecruitmentProcess(recruitmentProcessInitiateRequestDTO)).thenReturn(

                ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(recruitmentProcessInitiateResponseDTO)

                ));

        ResponseEntity<ApiResponse> result = controller.initiateRecruitmentProcess(recruitmentProcessInitiateRequestDTO);
        RecruitmentProcessInitiateResponseDTO responseBody = (RecruitmentProcessInitiateResponseDTO) result.getBody().getData();

        System.out.println(responseBody);

        assertNotNull(responseBody);
        assertNotNull(result);
        assertEquals(recruitmentProcessInitiateResponseDTO, responseBody);
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    void shouldReturnCandidateDoesNotExists_whenInitiateRecruitmentProcess() throws Exception{
        when(recruitmentProcessService.initiateRecruitmentProcess(any())).thenReturn(

                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Candidate Does Not Exists",
                                null,
                                HttpStatus.CONFLICT

                )));

        ResponseEntity<ApiResponse> result = controller.initiateRecruitmentProcess(any());


        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Candidate Does Not Exists", result.getBody().getMessage());


    }

    @Test
    void shouldReturnJobPostingDoesNotExists_whenInitiateRecruitmentProcess() throws Exception{
        when(recruitmentProcessService.initiateRecruitmentProcess(any())).thenReturn(

                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Job Posting Does Not Exists",
                                null,
                                HttpStatus.CONFLICT
                        )));

        ResponseEntity<ApiResponse> result = controller.initiateRecruitmentProcess(any());


        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Job Posting Does Not Exists", result.getBody().getMessage());


    }

    @Test
    void shouldReturnProcessAlreadyInitiated_whenInitiateRecruitmentProcess() throws Exception{
        when(recruitmentProcessService.initiateRecruitmentProcess(any())).thenReturn(
                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Process Already Initiated with Candidate on this JobPosting",
                                null,
                                HttpStatus.CONFLICT
                        )));

        ResponseEntity<ApiResponse> result = controller.initiateRecruitmentProcess(any());


        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Process Already Initiated with Candidate on this JobPosting", result.getBody().getMessage());


    }


    @Test
    void shouldReturn200_whenRejectTheProcess() throws Exception{

        Interview mockInterview = new Interview();
        mockInterview.setCandidateId(200L);
        mockInterview.setInterviewerName("Ahmet Yılmaz");
        mockInterview.setInterviewQuestions(null);
        mockInterview.setInterviewScore(5.6);
        mockInterview.setInterviewRatingQuote("candidate has a bright future");
        mockInterview.setInterviewScheduleTime(new Date());
        mockInterview.setCreatedAt(new Date());
        mockInterview.setLastUpdated(LocalDateTime.now().minusDays(3));
        mockInterview.setLastUpdated(LocalDateTime.now());
        mockInterview.setCreatedAt(new Date());
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .id(10L)
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviews(List.of(mockInterview))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();


        when(recruitmentProcessService.rejectTheRecruitmentProcessOnCandidate(10L)).thenReturn(

                ResponseEntity.status(HttpStatus.OK).body(
                        ApiResponse.success(
                                "Process successfully rejected and interviews deleted"
                )));

        ResponseEntity<ApiResponse> result = controller.rejectRecruitmentProcess(10L);
        assertNotNull(result.getBody().getData());
        assertEquals("Process successfully rejected and interviews deleted", result.getBody().getData());

    }

    @Test
    void shouldHandleProcessNotFound_whenRejectTheProcess() throws Exception{
        when(recruitmentProcessService.rejectTheRecruitmentProcessOnCandidate(10L)).thenReturn(

                ResponseEntity.status(HttpStatus.OK).body(
                        ApiResponse.error(
                                "Process not found ",
                                null,
                                HttpStatus.CONFLICT
                        )));

        ResponseEntity<ApiResponse> result = controller.rejectRecruitmentProcess(10L);
        assertNotNull(result.getBody());
        assertEquals("Process not found ", result.getBody().getMessage());

    }

    @Test
    void shouldHandleNoDetectedInterviews_whenRejectTheProcess() throws Exception{
        when(recruitmentProcessService.rejectTheRecruitmentProcessOnCandidate(10L)).thenReturn(

                ResponseEntity.status(HttpStatus.OK).body(
                        ApiResponse.error(
                                "Process status changed to rejected no interviews detecdet to delete ",
                                null,
                                HttpStatus.CONFLICT
                        )));

        ResponseEntity<ApiResponse> result = controller.rejectRecruitmentProcess(10L);
        assertNotNull(result.getBody());
        assertEquals("Process status changed to rejected no interviews detecdet to delete ", result.getBody().getMessage());

    }

    @Test
    void shouldHandleNegativeId_whenRejectTheProcess() throws Exception {
        mockMvc.perform(post("/api/v1/recruitment/internal/rejectRecruitmentProcess/{processId}" , -1L))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200_whenForwardToTheTechnicalInterviewProcess() throws Exception{

        Interview mockInterview = new Interview();
        mockInterview.setCandidateId(200L);
        mockInterview.setInterviewerName("Ahmet Yılmaz");
        mockInterview.setInterviewQuestions(null);
        mockInterview.setInterviewScore(5.6);
        mockInterview.setInterviewRatingQuote("candidate has a bright future");
        mockInterview.setInterviewScheduleTime(new Date());
        mockInterview.setCreatedAt(new Date());
        mockInterview.setLastUpdated(LocalDateTime.now().minusDays(3));
        mockInterview.setLastUpdated(LocalDateTime.now());
        mockInterview.setCreatedAt(new Date());
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .id(10L)
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviews(List.of(mockInterview))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        InterviewTechnicalInterviewRequestDTO interviewRequestDTO = new InterviewTechnicalInterviewRequestDTO();
        InterviewTechnicalInterviewRequestDTO dummyDto = new InterviewTechnicalInterviewRequestDTO();
        dummyDto.setInterviewerName("Ahmet Yılmaz");
        dummyDto.setCandidateId(123L);
        dummyDto.setInterviewRatingQuote("Kendisini iyi ifade etti, genel izlenim olumlu.");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(4.7);
        dummyDto.setInterviewScore(interviewScoreDTO);
        dummyDto.setCodeExerciseUrl("https://github.com/candidate/code-exercise");
        dummyDto.setCodeQualityScore(85);
        dummyDto.setTechnicalNotes("Temiz kod yazıyor, testleri iyi kullanıyor.");
        dummyDto.setCandidateTechnicalBackgroundNote("Spring Boot ve React konusunda deneyimli.");
        dummyDto.setTechnicalKnowledgeScore(TechnicalKnowledgeScore.AVERAGE);

        dummyDto.setInterviewQuestions(null);

        dummyDto.setInterviewScheduleTime("2025-07-19T10:30");
        dummyDto.setCreatedAt(new Date());
        dummyDto.setLastUpdated(LocalDateTime.now());

        interviewRequestDTO.setInterviewerName("");
        InterviewTechicalResponseDTO dummyResponseDto = new InterviewTechicalResponseDTO();

        dummyResponseDto.setCandidateId(123L);
        dummyResponseDto.setInterviewRatingQuote("Kendisini iyi ifade etti, genel izlenim olumlu.");
        dummyResponseDto.setInterviewerName("Ahmet Yılmaz");

        dummyResponseDto.setInterviewQuestions(null);

        dummyResponseDto.setInterviewScore(4.7);
        dummyResponseDto.setProcess(InterviewProcesses.TECHNICAL_INTERVIEW);

        dummyResponseDto.setInterviewScheduleTime(new Date());
        dummyResponseDto.setCreatedAt(new Date());
        dummyResponseDto.setLastUpdated(LocalDateTime.now());


when(recruitmentProcessRepository.findByCandidateIdAndId(10L,200L)).thenReturn(Optional.of(recruitmentProcess));
        when(recruitmentProcessService.forwardToTheTechnicalInterviewProcess(10L,interviewRequestDTO)).thenReturn(

                ResponseEntity.status(HttpStatus.OK).body(
                        ApiResponse.success(
                               dummyResponseDto
                        )));

        ResponseEntity<ApiResponse> result = controller.forwardToTheTechnicalInterviewProcess(10L,interviewRequestDTO);
        InterviewTechicalResponseDTO responseDTO = (InterviewTechicalResponseDTO) result.getBody().getData();

        assertNotNull(result.getBody().getData());
        assertEquals(dummyDto.getCandidateId(), responseDTO.getCandidateId());
        assertEquals(dummyDto.getInterviewerName(), responseDTO.getInterviewerName());
        assertEquals(dummyDto.getInterviewScore().getScore(), responseDTO.getInterviewScore());

    }
    @Test
    void shouldHandleProcessDoesNotExists_whenForwardToTheTechnicalInterviewProcess() throws Exception{
        InterviewTechnicalInterviewRequestDTO dummyDto = new InterviewTechnicalInterviewRequestDTO();
        dummyDto.setInterviewerName("Ahmet Yılmaz");
        dummyDto.setCandidateId(123L);
        dummyDto.setInterviewRatingQuote("Kendisini iyi ifade etti, genel izlenim olumlu.");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(4.7);
        dummyDto.setInterviewScore(interviewScoreDTO);
        dummyDto.setCodeExerciseUrl("https://github.com/candidate/code-exercise");
        dummyDto.setCodeQualityScore(85);
        dummyDto.setTechnicalNotes("Temiz kod yazıyor, testleri iyi kullanıyor.");
        dummyDto.setCandidateTechnicalBackgroundNote("Spring Boot ve React konusunda deneyimli.");
        dummyDto.setTechnicalKnowledgeScore(TechnicalKnowledgeScore.AVERAGE);

        dummyDto.setInterviewQuestions(null);

        dummyDto.setInterviewScheduleTime("2025-07-19T10:30");
        dummyDto.setCreatedAt(new Date());
        dummyDto.setLastUpdated(LocalDateTime.now());

        when(recruitmentProcessService.forwardToTheTechnicalInterviewProcess(10L,dummyDto)).thenReturn(

                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Process does not exists",
                                null,
                                HttpStatus.CONFLICT
                        )));

        ResponseEntity<ApiResponse> result = controller.forwardToTheTechnicalInterviewProcess(10L,dummyDto);
        InterviewTechicalResponseDTO responseDTO = (InterviewTechicalResponseDTO) result.getBody().getData();

        System.out.println(result);
        System.out.println(result.getBody());
        System.out.println(result.getBody().getMessage());

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Process does not exists", result.getBody().getMessage());

    }
    @Test
    void shouldHandleAlreadyInTechnicalProcesss_whenForwardToTheTechnicalInterviewProcess() throws Exception{
        InterviewTechnicalInterviewRequestDTO dummyDto = new InterviewTechnicalInterviewRequestDTO();
        dummyDto.setInterviewerName("Ahmet Yılmaz");
        dummyDto.setCandidateId(123L);
        dummyDto.setInterviewRatingQuote("Kendisini iyi ifade etti, genel izlenim olumlu.");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(4.7);
        dummyDto.setInterviewScore(interviewScoreDTO);
        dummyDto.setCodeExerciseUrl("https://github.com/candidate/code-exercise");
        dummyDto.setCodeQualityScore(85);
        dummyDto.setTechnicalNotes("Temiz kod yazıyor, testleri iyi kullanıyor.");
        dummyDto.setCandidateTechnicalBackgroundNote("Spring Boot ve React konusunda deneyimli.");
        dummyDto.setTechnicalKnowledgeScore(TechnicalKnowledgeScore.AVERAGE);

        dummyDto.setInterviewQuestions(null);

        dummyDto.setInterviewScheduleTime("2025-07-19T10:30");
        dummyDto.setCreatedAt(new Date());
        dummyDto.setLastUpdated(LocalDateTime.now());

        when(recruitmentProcessService.forwardToTheTechnicalInterviewProcess(10L,dummyDto)).thenReturn(

                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Process already on Technıcal Interview step.",
                                null,
                                HttpStatus.CONFLICT
                        )));

        ResponseEntity<ApiResponse> result = controller.forwardToTheTechnicalInterviewProcess(10L,dummyDto);
        InterviewTechicalResponseDTO responseDTO = (InterviewTechicalResponseDTO) result.getBody().getData();

        System.out.println(result);
        System.out.println(result.getBody());
        System.out.println(result.getBody().getMessage());

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Process already on Technıcal Interview step.", result.getBody().getMessage());

    }
    @Test
    void shouldHandleInternalServerError_whenForwardToTheTechnicalInterviewProcess() throws Exception{
        InterviewTechnicalInterviewRequestDTO dummyDto = new InterviewTechnicalInterviewRequestDTO();
        dummyDto.setInterviewerName("Ahmet Yılmaz");
        dummyDto.setCandidateId(123L);
        dummyDto.setInterviewRatingQuote("Kendisini iyi ifade etti, genel izlenim olumlu.");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(4.7);
        dummyDto.setInterviewScore(interviewScoreDTO);
        dummyDto.setCodeExerciseUrl("https://github.com/candidate/code-exercise");
        dummyDto.setCodeQualityScore(85);
        dummyDto.setTechnicalNotes("Temiz kod yazıyor, testleri iyi kullanıyor.");
        dummyDto.setCandidateTechnicalBackgroundNote("Spring Boot ve React konusunda deneyimli.");
        dummyDto.setTechnicalKnowledgeScore(TechnicalKnowledgeScore.AVERAGE);

        dummyDto.setInterviewQuestions(null);

        dummyDto.setInterviewScheduleTime("2025-07-19T10:30");
        dummyDto.setCreatedAt(new Date());
        dummyDto.setLastUpdated(LocalDateTime.now());

        when(recruitmentProcessService.forwardToTheTechnicalInterviewProcess(10L,dummyDto)).thenReturn(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error(
                                "Server Error.",
                                null,
                                HttpStatus.INTERNAL_SERVER_ERROR
                        )));

        ResponseEntity<ApiResponse> result = controller.forwardToTheTechnicalInterviewProcess(10L,dummyDto);
        InterviewTechicalResponseDTO responseDTO = (InterviewTechicalResponseDTO) result.getBody().getData();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Server Error.", result.getBody().getMessage());

    }


    @Test
    void shouldReturn200_forwardToTheCaseStudyInterviewProcess() throws  Exception{

        Interview mockInterview = new Interview();
        mockInterview.setCandidateId(200L);
        mockInterview.setInterviewerName("Mockito Mock");
        mockInterview.setInterviewQuestions(null);
        mockInterview.setInterviewScore(5.6);
        mockInterview.setInterviewRatingQuote("Case Study going well");
        mockInterview.setInterviewScheduleTime(new Date());
        mockInterview.setCreatedAt(new Date());
        mockInterview.setLastUpdated(LocalDateTime.now().minusDays(3));
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .id(660L)
                .candidateId(400L)
                .jobPostingId(500L)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviews(List.of(mockInterview))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        CaseStudyInterviewRequestDTO dummyDto = new CaseStudyInterviewRequestDTO();
        dummyDto.setInterviewerName("Mockito mock mock");
        dummyDto.setCandidateId(123L);

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(4.7);
        dummyDto.setInterviewScore(interviewScoreDTO);
        dummyDto.setInterviewScheduleTime("2025-07-19T10:30");
        dummyDto.setCreatedAt(new Date());
        dummyDto.setLastUpdated(LocalDateTime.now());

        CaseStudyResponseDTO caseStudyResponseDTO = new CaseStudyResponseDTO();

        caseStudyResponseDTO.setCandidateId(dummyDto.getCandidateId());
        caseStudyResponseDTO.setInterviewerName(dummyDto.getInterviewerName());
        caseStudyResponseDTO.setCreatedAt(dummyDto.getCreatedAt());
        caseStudyResponseDTO.setLastUpdated(dummyDto.getLastUpdated());

        when(recruitmentProcessService.forwardToTheCaseStudyInterviewProcess(10L,dummyDto)).thenReturn(

                ResponseEntity.ok(ApiResponse.success(caseStudyResponseDTO)));

        ResponseEntity<ApiResponse> result = controller.forwardToTheCaseStudyInterviewProcess(10L,dummyDto);
//        CaseStudyResponseDTO responseDTO = (CaseStudyResponseDTO) result.getBody().getData();
        CaseStudyResponseDTO responseBody = (CaseStudyResponseDTO) result.getBody().getData();

        assertNotNull(result.getBody().getData());
        assertEquals(dummyDto.getInterviewerName(), responseBody.getInterviewerName());

        //        assertEquals(dummyDto.getInterviewScore().getScore(), responseDTO.getInterviewScore());

    }

    @Test
    void shouldHandleProcessDoesNotExists_whenForwardToTheCaseStudyInterviewProcess() throws Exception{


        CaseStudyInterviewRequestDTO dummyDto = new CaseStudyInterviewRequestDTO();
        dummyDto.setInterviewerName("Mockito mock mock");
        dummyDto.setCandidateId(123L);

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(4.7);
        dummyDto.setInterviewScore(interviewScoreDTO);
        dummyDto.setInterviewScheduleTime("2025-07-19T10:30");
        dummyDto.setCreatedAt(new Date());
        dummyDto.setLastUpdated(LocalDateTime.now());

        when(recruitmentProcessService.forwardToTheCaseStudyInterviewProcess(10L,dummyDto)).thenReturn(

                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Process does not exists",
                                null,
                                HttpStatus.CONFLICT
                        )));

        ResponseEntity<ApiResponse> result = controller.forwardToTheCaseStudyInterviewProcess(10L,dummyDto);



        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Process does not exists", result.getBody().getMessage());

    }

    @Test
    void shouldProcessAlreadyOnCaseStudyInterviewStep_whenForwardToTheCaseStudyInterviewProcess() throws Exception{
        InterviewTechnicalInterviewRequestDTO dummyDto = new InterviewTechnicalInterviewRequestDTO();
        dummyDto.setInterviewerName("Ahmet Yılmaz");
        dummyDto.setCandidateId(123L);
        dummyDto.setInterviewRatingQuote("Kendisini iyi ifade etti, genel izlenim olumlu.");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(4.7);
        dummyDto.setInterviewScore(interviewScoreDTO);
        dummyDto.setCodeExerciseUrl("https://github.com/candidate/code-exercise");
        dummyDto.setCodeQualityScore(85);
        dummyDto.setTechnicalNotes("Temiz kod yazıyor, testleri iyi kullanıyor.");
        dummyDto.setCandidateTechnicalBackgroundNote("Spring Boot ve React konusunda deneyimli.");
        dummyDto.setTechnicalKnowledgeScore(TechnicalKnowledgeScore.AVERAGE);

        dummyDto.setInterviewQuestions(null);

        dummyDto.setInterviewScheduleTime("2025-07-19T10:30");
        dummyDto.setCreatedAt(new Date());
        dummyDto.setLastUpdated(LocalDateTime.now());

        when(recruitmentProcessService.forwardToTheTechnicalInterviewProcess(10L,dummyDto)).thenReturn(

                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Process already on Case Study Interview step.",
                                null,
                                HttpStatus.CONFLICT
                        )));
        ResponseEntity<ApiResponse> result = controller.forwardToTheTechnicalInterviewProcess(10L,dummyDto);
        InterviewTechicalResponseDTO responseDTO = (InterviewTechicalResponseDTO) result.getBody().getData();

        System.out.println(result);
        System.out.println(result.getBody());
        System.out.println(result.getBody().getMessage());

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Process already on Case Study Interview step.", result.getBody().getMessage());

    }

    @Test
    void shouldHandleInternalServerError_whenForwardToTheCaseStudyInterviewProcess() throws Exception{
        CaseStudyInterviewRequestDTO dummyDto = new CaseStudyInterviewRequestDTO();
        dummyDto.setInterviewerName("Mockito mock mock");
        dummyDto.setCandidateId(123L);

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(4.7);
        dummyDto.setInterviewScore(interviewScoreDTO);
        dummyDto.setInterviewScheduleTime("2025-07-19T10:30");
        dummyDto.setCreatedAt(new Date());
        dummyDto.setLastUpdated(LocalDateTime.now());


        when(recruitmentProcessService.forwardToTheCaseStudyInterviewProcess(10L,dummyDto) ).thenReturn(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error(
                                "Server Error.",
                                null,
                                HttpStatus.INTERNAL_SERVER_ERROR
                        )));

        ResponseEntity<ApiResponse> result = controller.forwardToTheCaseStudyInterviewProcess(10L,dummyDto);
        InterviewTechicalResponseDTO responseDTO = (InterviewTechicalResponseDTO) result.getBody().getData();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Server Error.", result.getBody().getMessage());

    }

    @Test
    void shouldHandleNegativeId_whenForwardToTheCaseStudyInterviewProcess() throws Exception {

        CaseStudyInterviewRequestDTO dummyDto = new CaseStudyInterviewRequestDTO();
        dummyDto.setInterviewerName("Mockito mock mock");
        dummyDto.setCandidateId(123L);

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(4.7);
        dummyDto.setInterviewScore(interviewScoreDTO);
        dummyDto.setInterviewScheduleTime("2025-07-19T10:30");
        dummyDto.setCreatedAt(new Date());
        dummyDto.setLastUpdated(LocalDateTime.now());

        mockMvc.perform(post("/api/v1/recruitment/internal/{processId}/forwardToTheCaseStudyInterviewProcess" , -10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private Object[] setupCommonMocksWhenInitiateCaseStudyInterviewProcess(Long interviewId) throws Exception {
        InitialCaseStudyInterviewDataDTO dto = new InitialCaseStudyInterviewDataDTO();
        CaseStudyScoreDTO scoreDTO = new CaseStudyScoreDTO();
        scoreDTO.setScore(6.9);
        dto.setCaseStudyScore(scoreDTO);
        dto.setInterviewerName("mock interviewer name");
        dto.setGivenCaseContent("mock content");
        dto.setGivenCaseUrl("mockurl.com");
        dto.setGivenCaseTitle("mock title");
        dto.setGivenCaseDeadline("2025-07-22 13:45:45");
        dto.setInterviewScheduleTime("2025-07-22 13:40:45");

        // Interview oluştur
        Interview mockInterview = new Interview();
        mockInterview.setId(interviewId);
        mockInterview.setCandidateId(200L);
        mockInterview.setInterviewerName(dto.getInterviewerName());
        mockInterview.setInterviewScore(5.6);
        mockInterview.setInterviewRatingQuote("Case Study going well");
        mockInterview.setCreatedAt(new Date());
        mockInterview.setLastUpdated(LocalDateTime.now().minusDays(3));


        ObjectNode solutionNode = objectMapper.createObjectNode();
        solutionNode.put("givenCaseTitle", dto.getGivenCaseTitle());
        solutionNode.put("givenCaseContent", dto.getGivenCaseContent());
        solutionNode.put("givenCaseDeadline", dto.getGivenCaseDeadline());
        solutionNode.put("caseStudyScore", dto.getCaseStudyScore().getScore());

        ObjectNode root = objectMapper.createObjectNode();
        root.set("initCaseStudy", solutionNode);
        mockInterview.setProcessSpecificData(objectMapper.writeValueAsString(root));
        mockInterview.setCaseStudyProcesses(CaseStudyProcesses.INITIAL);

        // Tarih formatlama
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mockInterview.setInterviewScheduleTime(formatter.parse(dto.getInterviewScheduleTime()));

        return new Object[]{dto, mockInterview};
    }

    @Test
    void shouldHandleInterviewDoesNotExists_initiateTheCaseStudyInterviewProcess() throws  Exception{
        Object[] mocks = setupCommonMocksWhenInitiateCaseStudyInterviewProcess(10L);
        InitialCaseStudyInterviewDataDTO dto = (InitialCaseStudyInterviewDataDTO) mocks[0];
        Interview mockInterview = (Interview) mocks[1];




        when(recruitmentProcessService.initiateTheCaseStudyInterviewProcess(mockInterview.getId(),dto)).thenReturn(

                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Interview does not exists.",
                                null,
                                HttpStatus.CONFLICT
                        )));

        ResponseEntity<ApiResponse> result = controller.initiateTheCaseStudyInterview(10L,dto);
                assertEquals("Interview does not exists.",result.getBody().getMessage());

    }
    @Test
    void shouldHandleAlreadyInCaseStudy_initiateTheCaseStudyInterviewProcess() throws  Exception{

        Object[] mocks = setupCommonMocksWhenInitiateCaseStudyInterviewProcess(10L);
        InitialCaseStudyInterviewDataDTO dto = (InitialCaseStudyInterviewDataDTO) mocks[0];
        Interview mockInterview = (Interview) mocks[1];


        when(recruitmentProcessService.initiateTheCaseStudyInterviewProcess(10L,dto)).thenReturn(

                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Interview already in case study ınitial progress .",
                                null,
                                HttpStatus.CONFLICT
                        )));

        ResponseEntity<ApiResponse> result = controller.initiateTheCaseStudyInterview(10L,dto);
        assertEquals("Interview already in case study ınitial progress .",result.getBody().getMessage());

    }

    @Test
    void shouldHandleInvalidJsonStructure_initiateTheCaseStudyInterviewProcess() throws  Exception{

        Object[] mocks = setupCommonMocksWhenInitiateCaseStudyInterviewProcess(10L);
        InitialCaseStudyInterviewDataDTO dto = (InitialCaseStudyInterviewDataDTO) mocks[0];
        Interview mockInterview = (Interview) mocks[1];


        when(recruitmentProcessService.initiateTheCaseStudyInterviewProcess(10L,dto)).thenReturn(

                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error("Invalid JSON structure in processSpecificData.", null, HttpStatus.INTERNAL_SERVER_ERROR))
    );

        ResponseEntity<ApiResponse> result = controller.initiateTheCaseStudyInterview(10L,dto);
        assertEquals("Invalid JSON structure in processSpecificData.",result.getBody().getMessage());

    }

    @Test
    void shouldHandleServerError_initiateTheCaseStudyInterviewProcess() throws  Exception{

        Object[] mocks = setupCommonMocksWhenInitiateCaseStudyInterviewProcess(10L);
        InitialCaseStudyInterviewDataDTO dto = (InitialCaseStudyInterviewDataDTO) mocks[0];
        Interview mockInterview = (Interview) mocks[1];


        when(recruitmentProcessService.initiateTheCaseStudyInterviewProcess(10L,dto)).thenReturn(

                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error(
                                "Server Error." ,
                                null,
                                HttpStatus.INTERNAL_SERVER_ERROR
                        )));

        ResponseEntity<ApiResponse> result = controller.initiateTheCaseStudyInterview(10L,dto);
        assertEquals("Server Error." ,result.getBody().getMessage());

    }



    //burada kaldım
    private Object[] setupSolveCaseStudyTest(Long interviewId) throws Exception {
        // DTO oluştur
        SolutionCaseStudyInterviewDTO dto = new SolutionCaseStudyInterviewDTO();
        dto.setCaseStudySolutionDescriptino("I made this solution within 3 weeks...");
        dto.setInterviewerName("mock interviewer name");
        dto.setCaseStudySolutionTitle("Mock Case Study solution title");
        dto.setInterviewScheduleTime("2025-07-22 13:45:45");

        // Interview oluştur
        Interview mockInterview = new Interview();
        mockInterview.setId(interviewId);
        mockInterview.setCandidateId(200L);
        mockInterview.setInterviewerName(dto.getInterviewerName());
        mockInterview.setInterviewScore(5.6);
        mockInterview.setInterviewRatingQuote("Case Study going well");
        mockInterview.setCreatedAt(new Date());
        mockInterview.setLastUpdated(LocalDateTime.now().minusDays(3));

        // JSON verisi oluştur
        ObjectNode solutionNode = objectMapper.createObjectNode();
        solutionNode.put("caseStudySolutionTitle", dto.getCaseStudySolutionTitle());
        solutionNode.put("caseStudySolutionDescriptino", dto.getCaseStudySolutionDescriptino());

        ObjectNode root = objectMapper.createObjectNode();
        root.set("solutionCaseStudy", solutionNode);
        mockInterview.setProcessSpecificData(objectMapper.writeValueAsString(root));
        mockInterview.setCaseStudyProcesses(CaseStudyProcesses.SOLUTION);

        // Response DTO oluştur
        SolutionCaseStudyResponseDTO responseDTO = new SolutionCaseStudyResponseDTO();
        responseDTO.setInterviewerName(mockInterview.getInterviewerName());
        responseDTO.setCaseStudySolutionTitle(dto.getCaseStudySolutionTitle());
        responseDTO.setCaseStudySolutionDescriptino(dto.getCaseStudySolutionDescriptino());
        responseDTO.setCaseStudyScore(5.6);
        responseDTO.setInterviewScheduleTime(new Date());
        responseDTO.setCreatedAt(mockInterview.getCreatedAt());
        responseDTO.setLastUpdated(mockInterview.getLastUpdated());

        return new Object[]{dto, mockInterview, responseDTO};
    }
        @Test
        void shouldReturn200_solveTheCaseStudyInterview() throws  Exception{

            Long interviewId = 10L;
            Object[] testData = setupSolveCaseStudyTest(interviewId);
            SolutionCaseStudyInterviewDTO dto = (SolutionCaseStudyInterviewDTO) testData[0];
            SolutionCaseStudyResponseDTO responseDTO = (SolutionCaseStudyResponseDTO) testData[2];

            when(recruitmentProcessService.solveTheCaseStudyInterview(interviewId,dto)).thenReturn(

                    ResponseEntity.ok(ApiResponse.success(responseDTO)));


            ResponseEntity<ApiResponse> result = controller.solveTheCaseStudyInterview(10L,dto);
    //        CaseStudyResponseDTO responseDTO = (CaseStudyResponseDTO) result.getBody().getData();



            SolutionCaseStudyResponseDTO responseBody = (SolutionCaseStudyResponseDTO) result.getBody().getData();
            assertNotNull(responseBody);
    //        assertEquals(solutionCaseStudyInterviewDTO.getInterviewerName(), responseBody.getInterviewerName());
    //        assertEquals(solutionCaseStudyInterviewDTO.getCaseStudySolutionTitle(), responseBody.getCaseStudySolutionTitle());
    //        assertEquals(solutionCaseStudyInterviewDTO.getGivenCaseSolutionUrl(), responseBody.getGivenCaseSolutionUrl());

        }

        @Test
        void shouldHandleInterviewDoesNotExists_solveTheCaseStudyInterview() throws  Exception{

            Long interviewId = 10L;
            Object[] testData = setupSolveCaseStudyTest(interviewId);
            SolutionCaseStudyInterviewDTO dto = (SolutionCaseStudyInterviewDTO) testData[0];
            SolutionCaseStudyResponseDTO responseDTO = (SolutionCaseStudyResponseDTO) testData[2];

            when(recruitmentProcessService.solveTheCaseStudyInterview(interviewId,dto)).thenReturn(
                    ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(ApiResponse.error(
                                    "Interview does not exists.",
                                    null,
                                    HttpStatus.CONFLICT
                            )));

            ResponseEntity<ApiResponse> result = controller.solveTheCaseStudyInterview(10L,dto);
    //        CaseStudyResponseDTO responseDTO = (CaseStudyResponseDTO) result.getBody().getData();


            assertNotNull(result);
            assertEquals(result.getBody().getMessage(),"Interview does not exists.");
            assertEquals(result.getBody().getStatus(),409);



        }

        @Test
        void shouldHandleInterviewAlreadyInCaseSolution_whenSolveTheCaseStudyInterview() throws  Exception{

            Long interviewId = 10L;
            Object[] testData = setupSolveCaseStudyTest(interviewId);
            SolutionCaseStudyInterviewDTO dto = (SolutionCaseStudyInterviewDTO) testData[0];
            SolutionCaseStudyResponseDTO responseDTO = (SolutionCaseStudyResponseDTO) testData[2];

            when(recruitmentProcessService.solveTheCaseStudyInterview(interviewId,dto)).thenReturn(
                    ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(ApiResponse.error(
                                    "Interview already in case study solution progress .",
                                    null,
                                    HttpStatus.CONFLICT
                            )));

            ResponseEntity<ApiResponse> result = controller.solveTheCaseStudyInterview(10L,dto);
            //        CaseStudyResponseDTO responseDTO = (CaseStudyResponseDTO) result.getBody().getData();


            assertNotNull(result);
            assertEquals(result.getBody().getMessage(),"Interview already in case study solution progress .");
            assertEquals(result.getBody().getStatus(),409);



        }

        @Test
        void shouldHandleServerError_whenSolveTheCaseStudyInterview() throws  Exception{

            Long interviewId = 10L;
            Object[] testData = setupSolveCaseStudyTest(interviewId);
            SolutionCaseStudyInterviewDTO dto = (SolutionCaseStudyInterviewDTO) testData[0];
            SolutionCaseStudyResponseDTO responseDTO = (SolutionCaseStudyResponseDTO) testData[2];

            when(recruitmentProcessService.solveTheCaseStudyInterview(interviewId,dto)).thenReturn(
                    ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(ApiResponse.error(
                                    "Server Error." ,
                                    null,
                                    HttpStatus.CONFLICT
                            )));


            ResponseEntity<ApiResponse> result = controller.solveTheCaseStudyInterview(10L,dto);
            //        CaseStudyResponseDTO responseDTO = (CaseStudyResponseDTO) result.getBody().getData();


            assertNotNull(result);
            assertEquals(result.getBody().getMessage(),"Server Error." );
            assertEquals(result.getBody().getStatus(),409);



        }

        private Object[] setupEvaluateCaseStudyTest(Long interviewId) throws Exception {
            EvaluateCaseStudyInterviewDTO dto = new EvaluateCaseStudyInterviewDTO();
            CaseStudyScoreDTO scoreDTO = new CaseStudyScoreDTO();
            scoreDTO.setScore(6.9);

            dto.setGivenCaseSolutionEvaluation("it was a decent evaluation");
            dto.setGivenCasePresentationEvaluation("it was a decent presentation");
            dto.setRisksIdentified("Risk i identify at controller level security");
            dto.setCaseStudyScore(scoreDTO);
            dto.setCandidateAnalyticThinkingNote("candidate analytic thinking is decent");
            dto.setInterviewScheduleTime("2025-07-22 13:45:45");
            dto.setInterviewerName("mock interviewer name");

            Interview mockInterview = new Interview();
            mockInterview.setId(interviewId);
            mockInterview.setCandidateId(200L);
            mockInterview.setInterviewerName(dto.getInterviewerName());
            mockInterview.setInterviewScore(5.6);
            mockInterview.setInterviewRatingQuote("Case Study going well");
            mockInterview.setCreatedAt(new Date());
            mockInterview.setLastUpdated(LocalDateTime.now().minusDays(3));

            ObjectNode solutionNode = objectMapper.createObjectNode();
            solutionNode.put("givenCaseSolutionEvaluation", dto.getGivenCaseSolutionEvaluation());
            solutionNode.put("givenCasePresentationEvaluation", dto.getGivenCasePresentationEvaluation());
            solutionNode.put("risksIdentified", dto.getRisksIdentified());
            solutionNode.put("candidateAnalyticThinkingNote", dto.getCandidateAnalyticThinkingNote());
            solutionNode.put("caseStudyScore", dto.getCaseStudyScore().getScore());

            ObjectNode root = objectMapper.createObjectNode();
            root.set("initCaseStudy", solutionNode);
            mockInterview.setProcessSpecificData(objectMapper.writeValueAsString(root));
            mockInterview.setCaseStudyProcesses(CaseStudyProcesses.EVALUATION);

            EvaluationCaseStudyResponseDTO responseDTO = new EvaluationCaseStudyResponseDTO();
            responseDTO.setInterviewerName(mockInterview.getInterviewerName());
            responseDTO.setGivenCaseSolutionEvaluation(dto.getGivenCaseSolutionEvaluation());
            responseDTO.setGivenCasePresentationEvaluation(dto.getGivenCasePresentationEvaluation());
            responseDTO.setRisksIdentified(dto.getRisksIdentified());
            responseDTO.setCandidateAnalyticThinkingNote(dto.getCandidateAnalyticThinkingNote());
            responseDTO.setCaseStudyScore(dto.getCaseStudyScore().getScore());

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 10);
            responseDTO.setInterviewScheduleTime(calendar.getTime());

            responseDTO.setCreatedAt(new Date());
            responseDTO.setLastUpdated(LocalDateTime.now());

            return new Object[]{dto, mockInterview, responseDTO};
        }

        @Test
        void shouldReturn200_evaluateTheCaseStudyInterview() throws  Exception{

            Long interviewId = 10L;
            Object[] testData = setupEvaluateCaseStudyTest(interviewId);
            EvaluateCaseStudyInterviewDTO dto = (EvaluateCaseStudyInterviewDTO) testData[0];
            EvaluationCaseStudyResponseDTO responseDTO = (EvaluationCaseStudyResponseDTO) testData[2];

            when(recruitmentProcessService.evaluateTheCaseStudyInterview(interviewId,dto)).thenReturn(

                    ResponseEntity.ok(ApiResponse.success(responseDTO)));
            String requestBody = new ObjectMapper().writeValueAsString(dto);

            mockmvc.perform(post(BASE_URL +"/internal/{interviewId}/evaluateTheCaseStudyInterview",10L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andDo(print())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.interviewerName").value(responseDTO.getInterviewerName()))
                    .andExpect(jsonPath("$.data.caseStudyScore").value(responseDTO.getCaseStudyScore()))
                    .andExpect(jsonPath("$.data.candidateAnalyticThinkingNote").value(responseDTO.getCandidateAnalyticThinkingNote()))
                    .andExpect(jsonPath("$.data.risksIdentified").value(responseDTO.getRisksIdentified()))
                    .andExpect(status().isOk());


        }

        @Test
        void shouldHandleInterviewDoesNotExists_whenEvaluateTheCaseStudyInterview() throws  Exception{

            Long interviewId = 10L;
            Object[] testData = setupEvaluateCaseStudyTest(interviewId);
            EvaluateCaseStudyInterviewDTO dto = (EvaluateCaseStudyInterviewDTO) testData[0];
            EvaluationCaseStudyResponseDTO responseDTO = (EvaluationCaseStudyResponseDTO) testData[2];

            when(recruitmentProcessService.evaluateTheCaseStudyInterview(interviewId,dto)).thenReturn(
                    ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(ApiResponse.error(
                                    "Interview does not exists.",
                                    null,
                                    HttpStatus.CONFLICT
                            )));
            String requestBody = new ObjectMapper().writeValueAsString(dto);

            mockmvc.perform(post(BASE_URL +"/internal/{interviewId}/evaluateTheCaseStudyInterview",10L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Interview does not exists."))
                    .andExpect(status().isConflict());


        }

        @Test
        void shouldHandleAlreadyInCaseEvaluation_whenEvaluateTheCaseStudyInterview() throws  Exception{

            Long interviewId = 10L;
            Object[] testData = setupEvaluateCaseStudyTest(interviewId);
            EvaluateCaseStudyInterviewDTO dto = (EvaluateCaseStudyInterviewDTO) testData[0];
            EvaluationCaseStudyResponseDTO responseDTO = (EvaluationCaseStudyResponseDTO) testData[2];

            when(recruitmentProcessService.evaluateTheCaseStudyInterview(interviewId,dto)).thenReturn(
                    ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(ApiResponse.error(
                                    "Interview already in case study evaluatıon progress .",
                                    null,
                                    HttpStatus.CONFLICT
                            )));
            String requestBody = new ObjectMapper().writeValueAsString(dto);

            mockmvc.perform(post(BASE_URL +"/internal/{interviewId}/evaluateTheCaseStudyInterview",10L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(
                            "Interview already in case study evaluatıon progress ."
                            ))
                    .andExpect(status().isConflict());


        }

        @Test
        void shouldHandleServerError_whenEvaluateTheCaseStudyInterview() throws  Exception{

            Long interviewId = 10L;
            Object[] testData = setupEvaluateCaseStudyTest(interviewId);
            EvaluateCaseStudyInterviewDTO dto = (EvaluateCaseStudyInterviewDTO) testData[0];
            EvaluationCaseStudyResponseDTO responseDTO = (EvaluationCaseStudyResponseDTO) testData[2];

            when(recruitmentProcessService.evaluateTheCaseStudyInterview(interviewId,dto)).thenReturn(
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ApiResponse.error(
                                    "Server Error.",
                                    null,
                                    HttpStatus.INTERNAL_SERVER_ERROR
                            )));
            String requestBody = new ObjectMapper().writeValueAsString(dto);

            mockmvc.perform(post(BASE_URL +"/internal/{interviewId}/evaluateTheCaseStudyInterview",10L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.status").value(500))
                    .andExpect(jsonPath("$.message").value(
                            "Server Error."
                    ))
                    .andExpect(status().isInternalServerError());
        }

        private List<FinalOverviewCandidateDTO> setupCandidateAveragesTestData() {
        CandidateResponseDTO candidateResponseDTO = CandidateResponseDTO.builder()
                .id(102L)
                .firstName("mockCandidateFirstname")
                .lastName("mockCandidateLastName")
                .email("mockemail@gmail.com")
                .phoneNumber("5531233132")
                .facebook_url("facebookurl.com")
                .instagram_url("instagram.com")
                .linkedin_url("linkedin.com")
                .cvUrl("cvurl.com")
                .address(new Address("adana", "Türkiye", "addresss"))
                .build();

        FinalOverviewCandidateDTO finalOverviewCandidateDTO = new FinalOverviewCandidateDTO();
        finalOverviewCandidateDTO.setCandidateResponseDTO(candidateResponseDTO);
        finalOverviewCandidateDTO.setAVERAGE_SCORE(5.5);
        finalOverviewCandidateDTO.setTECHNICAL(6.6);
        finalOverviewCandidateDTO.setHR_SCREENING(3.6);
        finalOverviewCandidateDTO.setCASE_STUDY(3.6);
        finalOverviewCandidateDTO.setINIT_CASE_STUDY(9.6);
        finalOverviewCandidateDTO.setEVALUATION_CASE_STUDY(1.6);

        return List.of(finalOverviewCandidateDTO);
    }
        //bunun edge caselerini araştırarak devam edicem alttakinin yani

        @Test
        void shouldReturn200_whenGetCandidateAveragesOnJobPosting () throws  Exception{
            List<FinalOverviewCandidateDTO> finalOverviewCandidateDTOList = setupCandidateAveragesTestData();
            when(recruitmentProcessService.getCandidateAveragesOnJobPosting(50L)).thenReturn(finalOverviewCandidateDTOList);

            mockmvc.perform(get(BASE_URL +"/internal/{jobPostingId}/getCandidateAveragesOnJobPosting",50L))
                    .andDo(print())
                    .andExpect(jsonPath("$[0].candidateResponseDTO.firstName").value(finalOverviewCandidateDTOList.get(0).getCandidateResponseDTO().getFirstName()))
                    .andExpect(jsonPath("$[0].candidateResponseDTO.lastName").value(finalOverviewCandidateDTOList.get(0).getCandidateResponseDTO().getLastName()))
                    .andExpect(jsonPath("$[0].candidateResponseDTO.email").value(finalOverviewCandidateDTOList.get(0).getCandidateResponseDTO().getEmail()))
                    .andExpect(status().isOk());


        }

        @Test
            void shouldHandleRecruitmentProcessEmpty_whenGetCandidateAveragesOnJobPosting () throws  Exception{
                when(recruitmentProcessService.getCandidateAveragesOnJobPosting(50L)).thenReturn(Collections.emptyList());
                mockmvc.perform(get(BASE_URL +"/internal/{jobPostingId}/getCandidateAveragesOnJobPosting",50L))
                        .andDo(print())
                        .andExpect(jsonPath("$", hasSize(0)))
                        .andExpect(status().isOk());
            }

        @Test
        void shouldHandleNegativeIdPathVariable_whenGetCandidateAveragesOnJobPosting () throws  Exception{
            List<FinalOverviewCandidateDTO> finalOverviewCandidateDTOList = setupCandidateAveragesTestData();
            when(recruitmentProcessService.getCandidateAveragesOnJobPosting(-50L)).thenReturn(finalOverviewCandidateDTOList);

            mockmvc.perform(get(BASE_URL +"/internal/{jobPostingId}/getCandidateAveragesOnJobPosting",-50L))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldHandleInvalidId_whenGetCandidateAveragesOnJobPosting () throws  Exception{
                mockmvc.perform(get(BASE_URL +"/internal/{jobPostingId}/getCandidateAveragesOnJobPosting",-50L))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }


        @Test
        //bunun assertequallarında scheduletimeda falan belki sıkıntı çıkabilir tam bilmiyorum  çünkü requestte string gerçekte date gibi falan  sorun çıakrmı diye bak en son kaldığım yerde buradkai tüm
        //buradkai tüm methodlara neredeyse bu notu almışım bu not neyle alakalı tam bilmiyorum
        void shouldReturn200_changeProcessToFinalOverview() throws  Exception{

        CandidateResponseDTO candidateTestResponse1 =   new CandidateResponseDTO(
                112L,
                "mockFirstCandidateName",
                "mockFirstCandidateLastName",
                Address.builder()
                        .city("mockCity")
                        .address("mockAddress")
                        .country("mockCountry")
                        .                build(),
                "mockEmail@gmail.com",
                "linkedin/mockfirstnaem.com.tr",
                "mockİnstagram/mockfirstnaem.com.tr",
                "mockFacebook/mockfirstnaem.com.tr",
                "mockPhoneNumber",
                "mockCvUrl",
                " 2025-08-15 03:00:00"
        );






        when(recruitmentProcessService.updateTheCandidateRecruitmentProcess(10L,12L)).thenReturn(

                ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.successSpesific(
                                candidateTestResponse1,
                                "Candidate process successfully updated to Final Overview "
                        ))
);
        String requestBody = new ObjectMapper().writeValueAsString(candidateTestResponse1);

        mockmvc.perform(post(BASE_URL +"/internal/changeProcessToFınalOverview/{candidateId}/{processId}",12L,10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value(candidateTestResponse1.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(candidateTestResponse1.getLastName()))
                .andExpect(jsonPath("$.data.email").value(candidateTestResponse1.getEmail()))
                .andExpect(status().isOk());


    }

        @Test
        void shouldHandleProcessNotFound_whenChangeProcessToFinalOverview() throws  Exception{

            CandidateResponseDTO candidateTestResponse1 =   new CandidateResponseDTO(
                    112L,
                    "mockFirstCandidateName",
                    "mockFirstCandidateLastName",
                    Address.builder()
                            .city("mockCity")
                            .address("mockAddress")
                            .country("mockCountry")
                            .                build(),
                    "mockEmail@gmail.com",
                    "linkedin/mockfirstnaem.com.tr",
                    "mockİnstagram/mockfirstnaem.com.tr",
                    "mockFacebook/mockfirstnaem.com.tr",
                    "mockPhoneNumber",
                    "mockCvUrl",
                    " 2025-08-15 03:00:00"
            );






            when(recruitmentProcessService.updateTheCandidateRecruitmentProcess(10L,12L)).thenReturn(

                    ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(ApiResponse.error(
                                    "Process not found",
                                    null,
                                    HttpStatus.CONFLICT
                            ))
            );
            String requestBody = new ObjectMapper().writeValueAsString(candidateTestResponse1);

            mockmvc.perform(post(BASE_URL +"/internal/changeProcessToFınalOverview/{candidateId}/{processId}",12L,10L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(jsonPath("$.message").value("Process not found"))
                    .andExpect(status().isConflict());
        }

        @Test
        void shouldHandleServerError_whenChangeProcessToFinalOverview() throws  Exception{

            CandidateResponseDTO candidateTestResponse1 =   new CandidateResponseDTO(
                    112L,
                    "mockFirstCandidateName",
                    "mockFirstCandidateLastName",
                    Address.builder()
                            .city("mockCity")
                            .address("mockAddress")
                            .country("mockCountry")
                            .                build(),
                    "mockEmail@gmail.com",
                    "linkedin/mockfirstnaem.com.tr",
                    "mockİnstagram/mockfirstnaem.com.tr",
                    "mockFacebook/mockfirstnaem.com.tr",
                    "mockPhoneNumber",
                    "mockCvUrl",
                    " 2025-08-15 03:00:00"
            );






            when(recruitmentProcessService.updateTheCandidateRecruitmentProcess(10L,12L)).thenReturn(

                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            ApiResponse.error(
                                    "Server Error",
                                    List.of( "Unexpected server error"),
                                    HttpStatus.INTERNAL_SERVER_ERROR
                            ))
            );
            String requestBody = new ObjectMapper().writeValueAsString(candidateTestResponse1);

            mockmvc.perform(post(BASE_URL +"/internal/changeProcessToFınalOverview/{candidateId}/{processId}",12L,10L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(jsonPath("$.message").value("Server Error"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        void shouldReturn200_whenUpdateTheCandidateRecruitmentProcess() throws  Exception{
            List<FinalOverviewCandidateDTO> finalOverviewCandidateDTOList = setupCandidateAveragesTestData();
            when(recruitmentProcessService.getCandidateAveragesOnJobPosting(50L)).thenReturn(finalOverviewCandidateDTOList);

            mockmvc.perform(get(BASE_URL +"/internal/{jobPostingId}/getCandidateAveragesOnJobPosting",50L))
                    .andDo(print())
                    .andExpect(jsonPath("$[0].candidateResponseDTO.firstName").value(finalOverviewCandidateDTOList.get(0).getCandidateResponseDTO().getFirstName()))
                    .andExpect(jsonPath("$[0].candidateResponseDTO.lastName").value(finalOverviewCandidateDTOList.get(0).getCandidateResponseDTO().getLastName()))
                    .andExpect(jsonPath("$[0].candidateResponseDTO.email").value(finalOverviewCandidateDTOList.get(0).getCandidateResponseDTO().getEmail()))
                    .andExpect(status().isOk());


        }


    //genel olarak testler bunlar şimdilik yeterli herhalde


}
