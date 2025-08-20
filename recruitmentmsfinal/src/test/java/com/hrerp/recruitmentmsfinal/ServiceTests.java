package com.hrerp.recruitmentmsfinal;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hrerp.Client.CandidateClient;
import com.hrerp.Client.JobPostingClient;
import com.hrerp.dto.*;
import com.hrerp.dto.JobPostingDTOs.JobPostingRequestRecruiterSpesificDTO;
import com.hrerp.dto.JobPostingDTOs.JobPostingResponseRecruiterSpesificDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyInterviewRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyProcesses;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyScoreDTO;
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
import com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.TechnicalInterviewDataDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.TechnicalKnowledgeScore;
import com.hrerp.dto.enums.Address;
import com.hrerp.dto.enums.ApplicationStatus;
import com.hrerp.model.Interview;
import com.hrerp.model.RecruitmentProcess;
import com.hrerp.model.enums.InterviewProcesses;
import com.hrerp.model.enums.InterviewQuestions;
import com.hrerp.model.mapper.InterviewMapper;
import com.hrerp.model.mapper.RecruitmentProcessMapper;
import com.hrerp.repository.InterviewRepository;
import com.hrerp.repository.RecruitmentProcessRepository;
import com.hrerp.service.RecruitmentProcessService;
import feign.FeignException;
import feign.Request;
import feign.Response;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@Transactional
public class ServiceTests {

    @Mock
    private JobPostingClient jobPostingClient;


    @Mock
    private CandidateClient candidateClient;

    @Mock
    private RecruitmentProcessMapper recruitmentProcessMapper;
    @Mock
    private InterviewMapper interviewMapper;
    @Mock
    private RecruitmentProcessRepository recruitmentProcessRepository;
    @Mock
    private InterviewRepository interviewRepository;

    @InjectMocks
    private RecruitmentProcessService recruitmentProcessService;


    @Test
    void shouldReturn200_whenUpdateRecrutierSpesificUpdate(){

        JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO = new JobPostingRequestRecruiterSpesificDTO();
        jobPostingRequestRecruiterSpesificDTO.setInternalJobId(12L);
        jobPostingRequestRecruiterSpesificDTO.setReplacement(false);
        jobPostingRequestRecruiterSpesificDTO.setReplacementFor(null);
        jobPostingRequestRecruiterSpesificDTO.setInternalHrNote("Internal Mock HR note");
        jobPostingRequestRecruiterSpesificDTO.setHiringManagerName("Mock HR manager name");

        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDTO = new JobPostingResponseRecruiterSpesificDTO();
        jobPostingResponseRecruiterSpesificDTO.setInternalPostingDate(LocalDateTime.now().minusDays(3));
        jobPostingResponseRecruiterSpesificDTO.setInternalJobId(12L);
        jobPostingResponseRecruiterSpesificDTO.setReplacement(false);
        jobPostingResponseRecruiterSpesificDTO.setReplacementFor(null);
        jobPostingResponseRecruiterSpesificDTO.setHiringManagerName("Mock HR manager name");
        jobPostingResponseRecruiterSpesificDTO.setInternalHrNote("Internal Mock HR note");


        when(jobPostingClient.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO, 10L))
                .thenReturn(ResponseEntity.ok(ApiResponse.success(jobPostingResponseRecruiterSpesificDTO)));


        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO, 10L);

        JobPostingResponseRecruiterSpesificDTO responseBody = (JobPostingResponseRecruiterSpesificDTO) result.getBody().getData();


        assertEquals(jobPostingRequestRecruiterSpesificDTO.getInternalHrNote(), responseBody.getInternalHrNote());
        assertEquals(jobPostingRequestRecruiterSpesificDTO.getInternalJobId(), responseBody.getInternalJobId());
        assertEquals(jobPostingRequestRecruiterSpesificDTO.getHiringManagerName(), responseBody.getHiringManagerName());
        assertEquals(jobPostingRequestRecruiterSpesificDTO.getReplacementFor(), responseBody.getReplacementFor());
        assertNotNull(result.getBody().getData());
    }

    public static JobPostingRequestRecruiterSpesificDTO createJobPostingRequestDTO() {
        JobPostingRequestRecruiterSpesificDTO dto = new JobPostingRequestRecruiterSpesificDTO();
        dto.setInternalJobId(12L);
        dto.setHiringManagerName("Mock HR manager name");
        dto.setInternalHrNote("Internal Mock HR note");
        dto.setReplacement(false);
        dto.setReplacementFor(null);
        return dto;
    }

    public static JobPostingResponseRecruiterSpesificDTO createJobPostingResponseDTO() {
        JobPostingResponseRecruiterSpesificDTO dto = new JobPostingResponseRecruiterSpesificDTO();
        dto.setInternalJobId(12L);
        dto.setHiringManagerName("Mock HR manager name");
        dto.setInternalHrNote("Internal Mock HR note");
        dto.setReplacement(false);
        dto.setReplacementFor(null);
        dto.setInternalPostingDate(LocalDateTime.now().minusDays(3));
        return dto;
    }

    @Test
    void shouldHandleFeignClientError_whenUpdateRecrutierSpesificUpdate(){

        JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO = createJobPostingRequestDTO();
        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDTO = createJobPostingResponseDTO();


        when(jobPostingClient.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO, 10L))
                .thenThrow(FeignException.errorStatus(
                        "recruiterSpesificUpdate",
                        Response.builder()
                                .status(500)
                                .request(Request.create(
                                        Request.HttpMethod.POST,
                                        "/api/v1/jobPostings/10/recruiterSpesificUpdate",
                                        Collections.emptyMap(),
                                        null,
                                        Charset.defaultCharset()))
                                .build()));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO, 10L);

        assertEquals(result.getBody().getMessage(), "Job Posting not found");
        assertEquals(result.getBody().getStatus(), 500);

    }

    @Test
    void shouldHandleNullFields_whenUpdateRecrutierSpesificUpdate(){


        JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO = new JobPostingRequestRecruiterSpesificDTO();
        jobPostingRequestRecruiterSpesificDTO.setInternalJobId(null);
        jobPostingRequestRecruiterSpesificDTO.setReplacement(false);
        jobPostingRequestRecruiterSpesificDTO.setReplacementFor(null);
        jobPostingRequestRecruiterSpesificDTO.setInternalHrNote("Internal Mock HR note");
        jobPostingRequestRecruiterSpesificDTO.setHiringManagerName("Mock HR manager name");

        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDTO = new JobPostingResponseRecruiterSpesificDTO();
        jobPostingResponseRecruiterSpesificDTO.setInternalPostingDate(LocalDateTime.now().minusDays(3));
        jobPostingResponseRecruiterSpesificDTO.setInternalJobId(12L);
        jobPostingResponseRecruiterSpesificDTO.setReplacement(false);
        jobPostingResponseRecruiterSpesificDTO.setReplacementFor("initialvalueofjobposting");
        jobPostingResponseRecruiterSpesificDTO.setHiringManagerName("Mock HR manager name");
        jobPostingResponseRecruiterSpesificDTO.setInternalHrNote("Internal Mock HR note");


        when(jobPostingClient.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO, 10L))
                .thenReturn(ResponseEntity.ok(ApiResponse.success(jobPostingResponseRecruiterSpesificDTO)));


        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO, 10L);

        JobPostingResponseRecruiterSpesificDTO responseBody = (JobPostingResponseRecruiterSpesificDTO) result.getBody().getData();


        assertEquals(jobPostingRequestRecruiterSpesificDTO.getInternalHrNote(), responseBody.getInternalHrNote());
        assertNotEquals(jobPostingRequestRecruiterSpesificDTO.getInternalJobId(), responseBody.getInternalJobId());
        assertEquals(jobPostingRequestRecruiterSpesificDTO.getHiringManagerName(), responseBody.getHiringManagerName());
        assertNotEquals(jobPostingRequestRecruiterSpesificDTO.getReplacementFor(), responseBody.getReplacementFor());
        assertNotNull(result.getBody().getData());
    }

    //başka edge casesi yok herhalde yukardaki basit bir alan updatele sonra dto dön methodu çünkü

    @Test
    void shouldReturn200_whenUpdateRecrutierSpesificFetch(){
        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDTO = new JobPostingResponseRecruiterSpesificDTO();
        jobPostingResponseRecruiterSpesificDTO.setInternalPostingDate(LocalDateTime.now().minusDays(3));
        jobPostingResponseRecruiterSpesificDTO.setInternalJobId(10L);
        jobPostingResponseRecruiterSpesificDTO.setReplacement(false);
        jobPostingResponseRecruiterSpesificDTO.setReplacementFor("MockReplacementFor");
        jobPostingResponseRecruiterSpesificDTO.setHiringManagerName("mockHiringmanager");
        jobPostingResponseRecruiterSpesificDTO.setInternalHrNote("mockhrınternalnote");
        when(jobPostingClient.recruiterSpesificFetch( 10L))
                .thenReturn(ResponseEntity.ok(ApiResponse.success(jobPostingResponseRecruiterSpesificDTO)));


        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.recruiterSpesificFetch( 10L);

        JobPostingResponseRecruiterSpesificDTO responseBody = (JobPostingResponseRecruiterSpesificDTO) result.getBody().getData();
        assertNotNull(responseBody);
        assertEquals("Success", result.getBody().getMessage());
        assertEquals(jobPostingResponseRecruiterSpesificDTO.getInternalHrNote(), responseBody.getInternalHrNote());
        assertEquals(jobPostingResponseRecruiterSpesificDTO.getInternalJobId(), responseBody.getInternalJobId());
        assertEquals(jobPostingResponseRecruiterSpesificDTO.getHiringManagerName(), responseBody.getHiringManagerName());
        assertEquals(jobPostingResponseRecruiterSpesificDTO.getReplacementFor(), responseBody.getReplacementFor());

    }

    @Test
    void shouldHandleFeignClientError_whenRecrutierSpesificFetch(){


        when(jobPostingClient.recruiterSpesificFetch( 10L))
                .thenThrow(FeignException.errorStatus(
                        "recruiterSpesificFetch",
                        Response.builder()
                                .status(500)
                                .request(Request.create(
                                        Request.HttpMethod.GET,
                                        "/api/v1/jobPostings/internal/10",
                                        Collections.emptyMap(),
                                        null,
                                        Charset.defaultCharset()))
                                .build()));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.recruiterSpesificFetch( 10L);
//        System.out.println(result.getBody());
//        System.out.println(result.getBody().getMessage());
//        System.out.println(result.getBody().getErrors());
        assertEquals(result.getBody().getMessage(), "Job Posting not found");
        assertEquals(result.getBody().getStatus(), 500);

    }
    //bence zaten burada edge case yoktur gibime geliyor

    //böyle bir edge case olmaz çünkü null gelemiyor
//    @Test
//    void shouldHandleNullFields_whenRecrutierSpesificFetch(){
//
//
//        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDTO = new JobPostingResponseRecruiterSpesificDTO();
//        jobPostingResponseRecruiterSpesificDTO.setInternalPostingDate(null);
//        jobPostingResponseRecruiterSpesificDTO.setInternalJobId(null);
//        jobPostingResponseRecruiterSpesificDTO.setReplacement(false);
//        jobPostingResponseRecruiterSpesificDTO.setReplacementFor(null);
//        jobPostingResponseRecruiterSpesificDTO.setHiringManagerName("mockHiringmanager");
//        jobPostingResponseRecruiterSpesificDTO.setInternalHrNote("mockhrınternalnote");
//
//
//        when(jobPostingClient.recruiterSpesificFetch( 10L))
//                .thenReturn(ResponseEntity.ok(ApiResponse.success(jobPostingResponseRecruiterSpesificDTO)));
//
//
//        ResponseEntity<ApiResponse> result =
//                recruitmentProcessService.recruiterSpesificFetch( 10L);
//
//        JobPostingResponseRecruiterSpesificDTO responseBody = (JobPostingResponseRecruiterSpesificDTO) result.getBody().getData();
//
//
//        assertNotNull(responseBody.getInternalJobId());
//        assertNotNull(responseBody.getInternalPostingDate());
//        assertNotNull(responseBody.getReplacementFor());
//    }

    private ArrayList<CandidateResponseDTO> setupResponseDataWhenGetProperCandidates() throws Exception {
        CandidateResponseDTO candidateResponseDTO = new CandidateResponseDTO();
        candidateResponseDTO.setId(10L);
        candidateResponseDTO.setFirstName("Mockcandidate1FirstName");
        candidateResponseDTO.setLastName("Mockcandidate1lastname");
        candidateResponseDTO.setAddress(Address.builder()
                .country("mockCoutry")
                .address("mockAddress")
                .city("mockCity").build());
        candidateResponseDTO.setEmail("mcokCandidate1Email");
        candidateResponseDTO.setLinkedin_url("mockLinkedinUrl");
        candidateResponseDTO.setInstagram_url("mockInstagram");
        candidateResponseDTO.setFacebook_url("mockFacebook");
        candidateResponseDTO.setPhoneNumber("mockPhoneNumber");
        candidateResponseDTO.setCvUrl("mockCvUrl");
        candidateResponseDTO.setCreatedAt("2025-07.20 14:00:00");

        CandidateResponseDTO candidateResponseDTO2 = new CandidateResponseDTO();
        candidateResponseDTO2.setId(12L);
        candidateResponseDTO2.setFirstName("Mockcandidate2FirstName");
        candidateResponseDTO2.setLastName("Mockcandidate2lastname");
        candidateResponseDTO2.setAddress(Address.builder()
                .country("mockCoutry2")
                .address("mockAddress2")
                .city("mockCity2").build());
        candidateResponseDTO2.setEmail("mcokCandidate2Email");
        candidateResponseDTO2.setLinkedin_url("mock2LinkedinUrl");
        candidateResponseDTO2.setInstagram_url("mock2Instagram");
        candidateResponseDTO2.setFacebook_url("mock2Facebook");
        candidateResponseDTO2.setPhoneNumber("mock2PhoneNumber");
        candidateResponseDTO2.setCvUrl("mock2CvUrl");
        candidateResponseDTO2.setCreatedAt("2025-07.20 16:00:00");
        ArrayList<CandidateResponseDTO> candidateResponseDTOArrayList = new ArrayList<>();
        candidateResponseDTOArrayList.add(candidateResponseDTO);
        candidateResponseDTOArrayList.add(candidateResponseDTO2);
        return  candidateResponseDTOArrayList;
    }

    @Test
    void shouldReturn200_whenGetTheProperCandidates() throws Exception {

      ArrayList<CandidateResponseDTO> candidateResponseDTOS = setupResponseDataWhenGetProperCandidates();

        when(candidateClient.getTheProperCandidates( 10L))
                .thenReturn(ResponseEntity.ok(ApiResponse.success(candidateResponseDTOS)));


        ResponseEntity<ApiResponse<?>> result =
                recruitmentProcessService.getTheProperCandidates( 10L);

        List<CandidateResponseDTO> responseBody = (List<CandidateResponseDTO>) result.getBody().getData();

        assertNotNull(responseBody);
        assertEquals("Success", result.getBody().getMessage());

        assertEquals(candidateResponseDTOS.get(0).getFirstName(), responseBody.get(0).getFirstName());
        assertEquals(candidateResponseDTOS.get(0).getLastName(), responseBody.get(0).getLastName());
        assertEquals(candidateResponseDTOS.get(0).getEmail(), responseBody.get(0).getEmail());
        assertEquals(candidateResponseDTOS.get(0).getLinkedin_url(), responseBody.get(0).getLinkedin_url());

        assertEquals(candidateResponseDTOS.get(1).getFirstName(), responseBody.get(1).getFirstName());
        assertEquals(candidateResponseDTOS.get(1).getLastName(), responseBody.get(1).getLastName());
        assertEquals(candidateResponseDTOS.get(1).getEmail(), responseBody.get(1).getEmail());
        assertEquals(candidateResponseDTOS.get(1).getLinkedin_url(), responseBody.get(1).getLinkedin_url());


    }

    @Test
    void shouldHandleJobPostingNotFound_whenGetTheProperCandidates(){


        when(candidateClient.getTheProperCandidates( 10L)).thenReturn(
        ResponseEntity.status(HttpStatus.NOT_FOUND) .body(ApiResponse.error(
                "Job Posting not found",
                null,
                HttpStatus.NOT_FOUND
        )));


        ResponseEntity<ApiResponse<?>> result =
                recruitmentProcessService.getTheProperCandidates( 10L);

        System.out.println(result.getBody().getMessage());
        assertNotNull(result);
        assertEquals("Job Posting not found", result.getBody().getMessage());

    }

    @Test
    void shouldHandleCandidateOrApplicationNotFound_whenGetTheProperCandidates(){
        when(candidateClient.getTheProperCandidates( 10L)).thenReturn(
                ResponseEntity.status(HttpStatus.NOT_FOUND) .body(ApiResponse.error(
                        "Candidate or application not found",
                        null,
                        HttpStatus.NOT_FOUND
                )));


        ResponseEntity<ApiResponse<?>> result =
                recruitmentProcessService.getTheProperCandidates( 10L);

        System.out.println(result.getBody().getMessage());
        assertNotNull(result);
        assertEquals("Candidate or application not found", result.getBody().getMessage());

    }


    @Test
    void shouldHandleCandidateListIsEmpty_whenGetTheProperCandidates(){
        when(candidateClient.getTheProperCandidates( 10L)).thenReturn(
                ResponseEntity.status(HttpStatus.NOT_FOUND) .body(ApiResponse.error(
                        "Candidate List is empty",
                        null,
                        HttpStatus.NOT_FOUND
                )));


        ResponseEntity<ApiResponse<?>> result =
                recruitmentProcessService.getTheProperCandidates( 10L);

        System.out.println(result.getBody().getMessage());
        assertNotNull(result);
        assertEquals("Candidate List is empty", result.getBody().getMessage());

    }

    @Test
    void shouldHandleFeignClientError_whenGetTheProperCandidates() throws Exception {


        when(candidateClient.getTheProperCandidates( 10L))
                .thenThrow(FeignException.errorStatus(
                        "getTheProperCandidates",
                        Response.builder()
                                .status(500)
                                .request(Request.create(
                                        Request.HttpMethod.POST,
                                        "/api/v1/applications/{jobPostingId}/getTheProperCandidates",
                                        Collections.emptyMap(),
                                        null,
                                        Charset.defaultCharset()))
                                .build()));




        ResponseEntity<ApiResponse<?>> result =
                recruitmentProcessService.getTheProperCandidates( 10L);

        assertEquals(result.getBody().getMessage(), "Candidate Or Application Not Found");
        assertEquals(result.getBody().getStatus(), 500);



    }


    @Test
    void shouldReturn200_updateTheCandidateApplicationStatus(){
        ApplicationStatusUpdateDTO applicationStatusUpdateDTO = new ApplicationStatusUpdateDTO();
        applicationStatusUpdateDTO.setApplicationStatus(ApplicationStatus.APPLIED);

        when(candidateClient.updateTheCandidateApplicationStatus(12L, applicationStatusUpdateDTO))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                        ApplicationStatus.APPLIED
                )));


        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.updateTheCandidateApplicationStatus( 12L,applicationStatusUpdateDTO);

        ApplicationStatus responseBody = (ApplicationStatus) result.getBody().getData();

        assertNotNull(responseBody);
        assertEquals("Success", result.getBody().getMessage());

        assertEquals(applicationStatusUpdateDTO.getApplicationStatus(), responseBody);


    }

    @Test
    void shouldHandleNoApplicationSpecifiedOnCandidate_updateTheCandidateApplicationStatus(){
        ApplicationStatusUpdateDTO applicationStatusUpdateDTO = new ApplicationStatusUpdateDTO();
        applicationStatusUpdateDTO.setApplicationStatus(ApplicationStatus.APPLIED);


        when(candidateClient.updateTheCandidateApplicationStatus( 10L,applicationStatusUpdateDTO))
                .thenThrow(FeignException.errorStatus(
                        "updateTheCandidateApplicationStatus",
                        Response.builder()
                                .status(500)
                                .request(Request.create(
                                        Request.HttpMethod.POST,
                                        "/api/v1/applications/{candidateId}/updateTheCandidateApplicationStatus",
                                        Collections.emptyMap(),
                                        null,
                                        Charset.defaultCharset()))
                                .build()));





        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.updateTheCandidateApplicationStatus( 10L,applicationStatusUpdateDTO);



        assertEquals("Candidates not found", result.getBody().getMessage());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());


    }

    @Test
    void shouldHandleInternalServerError_updateTheCandidateApplicationStatus(){
        ApplicationStatusUpdateDTO applicationStatusUpdateDTO = new ApplicationStatusUpdateDTO();
        applicationStatusUpdateDTO.setApplicationStatus(ApplicationStatus.APPLIED);


        when(candidateClient.updateTheCandidateApplicationStatus( 10L,applicationStatusUpdateDTO))
                .thenThrow(new RuntimeException(
                       "Runtime Exception"));





        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.updateTheCandidateApplicationStatus( 10L,applicationStatusUpdateDTO);



        assertEquals("Server Error.", result.getBody().getMessage());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());


    }
    //dediğim gibi bunun da dönebilecekleri bu kadar mesela başka dönebileceği birşey yok methoddan.


    //buradan devam edicem testlere bunun edge casesi var mı daha bi bakarım sonra devam ederim 17.08
    @Test
    void shouldReturn200_getRecruitmentProcessList(){
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
        ArrayList<RecruitmentProcess> arrayList = new ArrayList<>();
        arrayList.add(recruitmentProcess);

        when(candidateClient.candidateExistsById(200L)).thenReturn(true);
        when(recruitmentProcessRepository.findByCandidateId(200L)).thenReturn(arrayList);



        ArrayList<RecruitmentProcessInitiateResponseDTO> responseDTO = new ArrayList<>();

        RecruitmentProcessInitiateResponseDTO recruitmentProcessInitiateResponseDTO = new RecruitmentProcessInitiateResponseDTO();


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
        recruitmentProcessInitiateResponseDTO.setJobPostingId(300L);
        recruitmentProcessInitiateResponseDTO.setCandidateId(200L);
        responseDTO.add(recruitmentProcessInitiateResponseDTO);
        when(recruitmentProcessMapper.fromRecruitmentProcess(any())).thenReturn(recruitmentProcessInitiateResponseDTO);

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.getTheProcessesBasedOnCandidate( 200L);
        List<RecruitmentProcessInitiateResponseDTO> responseBody = (List<RecruitmentProcessInitiateResponseDTO>) result.getBody().getData();
        System.out.println(result.getBody().getData());
        assertNotNull(responseBody);
        assertEquals("Success", result.getBody().getMessage());
        assertEquals(recruitmentProcessInitiateResponseDTO.getCandidateId(), responseBody.get(0).getCandidateId());
        assertEquals(recruitmentProcessInitiateResponseDTO.getInterviews().get(0).getInterviewerName(), responseBody.get(0).getInterviews().get(0).getInterviewerName());
        assertEquals(recruitmentProcessInitiateResponseDTO.getInterviews().get(0).getInterviewScheduleTime(), responseBody.get(0).getInterviews().get(0).getInterviewScheduleTime());
        assertEquals(recruitmentProcessInitiateResponseDTO.getInterviews().get(0).getInterviewScore(), responseBody.get(0).getInterviews().get(0).getInterviewScore());



    }

    @Test
    void shouldHandleCandidateDoesNotExists_getRecruitmentProcessList(){


        when(candidateClient.candidateExistsById(200L)).thenReturn(false);

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.getTheProcessesBasedOnCandidate( 200L);
        assertEquals("Candidate Does Not Exists", result.getBody().getMessage());



    }

    @Test
    void shouldHandleCandidateDoesNotAnyRecruitmentProcessExists_getRecruitmentProcessList(){


        when(candidateClient.candidateExistsById(200L)).thenReturn(true);
        when(recruitmentProcessRepository.findByCandidateId(200L)).thenReturn(Collections.emptyList());

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.getTheProcessesBasedOnCandidate( 200L);
        assertEquals("Candidate doesnt have any recruitment processes exist.", result.getBody().getMessage());



    }

    @Test
    void shouldThrowInternalServerError_getRecruitmentProcessList(){


        when(candidateClient.candidateExistsById(200L)).thenReturn(true);
        when(recruitmentProcessRepository.findByCandidateId(200L)).thenThrow(new RuntimeException("Runtime Exception"));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.getTheProcessesBasedOnCandidate( 200L);
        assertEquals("Server Error.", result.getBody().getMessage());



    }

    @Test
    void shouldReturn200_getTheInduvualRecruitmentProcesses(){
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
        when(  recruitmentProcessRepository.findByCandidateIdAndId(10L, 200L)).thenReturn(Optional.ofNullable(recruitmentProcess));



        ArrayList<RecruitmentProcessInitiateResponseDTO> responseDTO = new ArrayList<>();

        RecruitmentProcessInitiateResponseDTO recruitmentProcessInitiateResponseDTO = new RecruitmentProcessInitiateResponseDTO();


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
        recruitmentProcessInitiateResponseDTO.setJobPostingId(300L);
        recruitmentProcessInitiateResponseDTO.setCandidateId(200L);
        responseDTO.add(recruitmentProcessInitiateResponseDTO);
        when(candidateClient.candidateExistsById(200L)).thenReturn(true);

        when(recruitmentProcessMapper.fromRecruitmentProcess(any())).thenReturn(recruitmentProcessInitiateResponseDTO);

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.getTheInduvualRecruitmentProcesses( 200L,10l);
        RecruitmentProcessInitiateResponseDTO responseBody = (RecruitmentProcessInitiateResponseDTO) result.getBody().getData();
        System.out.println(responseBody);
        assertNotNull(responseBody);
        assertEquals("Success", result.getBody().getMessage());
        assertEquals(recruitmentProcessInitiateResponseDTO.getCandidateId(), responseBody.getCandidateId());
        assertEquals(recruitmentProcessInitiateResponseDTO.getInterviews().get(0).getInterviewerName(), responseBody.getInterviews().get(0).getInterviewerName());
        assertEquals(recruitmentProcessInitiateResponseDTO.getInterviews().get(0).getInterviewScheduleTime(), responseBody.getInterviews().get(0).getInterviewScheduleTime());
        assertEquals(recruitmentProcessInitiateResponseDTO.getInterviews().get(0).getInterviewScore(), responseBody.getInterviews().get(0).getInterviewScore());



    }

    @Test
    void shouldHandleProcessNotFound_getTheInduvualRecruitmentProcesses(){


        when(recruitmentProcessRepository.findByCandidateIdAndId(50L,200L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.getTheInduvualRecruitmentProcesses( 200L,50l);

        System.out.println("message" + result.getBody().getMessage());
        System.out.println("body" + result.getBody());
        System.out.println("errors" + result.getBody().getErrors());

        assertEquals("Process not found", result.getBody().getMessage());



    }

    @Test
    void shouldHandleCandidateDoesNotExists_getTheInduvualRecruitmentProcesses(){
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

        when(recruitmentProcessRepository.findByCandidateIdAndId(50L,200L)).thenReturn(Optional.ofNullable(recruitmentProcess));
        when(candidateClient.candidateExistsById(200L)).thenReturn(false);

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.getTheInduvualRecruitmentProcesses( 200L,50l);

        System.out.println("message" + result.getBody().getMessage());
        System.out.println("body" + result.getBody());
        System.out.println("errors" + result.getBody().getErrors());

        assertEquals("Candidate Does Not Exists", result.getBody().getMessage());



    }

    @Test
    void shouldHandleInternalServerError_getTheInduvualRecruitmentProcesses(){

        when(recruitmentProcessRepository.findByCandidateIdAndId(50L,200L)).thenThrow(new RuntimeException("Runtime Exception"));
        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.getTheInduvualRecruitmentProcesses( 200L,50l);
        assertEquals("Server Error.", result.getBody().getMessage());
    }

    @Test
    void shouldHandleCandidateDoesNotAnyRecruitmentProcessExists_getTheInduvualRecruitmentProcesses(){


        when(candidateClient.candidateExistsById(200L)).thenReturn(true);
        when(recruitmentProcessRepository.findByCandidateId(200L)).thenReturn(Collections.emptyList());

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.getTheProcessesBasedOnCandidate( 200L);
        assertEquals("Candidate doesnt have any recruitment processes exist.", result.getBody().getMessage());



    }



    @Test
    void shouldReturn200_rejectTheRecruitmentProcessOnCandidate(){
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
        when(  recruitmentProcessRepository.findById(10L)).thenReturn(Optional.ofNullable(recruitmentProcess));

recruitmentProcess.setInterviewProcesses(InterviewProcesses.REJECTED);
        ArrayList<RecruitmentProcessInitiateResponseDTO> responseDTO = new ArrayList<>();



        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.rejectTheRecruitmentProcessOnCandidate( 10L);
        System.out.println(result.getBody().getData());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Process successfully rejected and interviews deleted", result.getBody().getData());

    }


    @Test
    void shouldHandleProcessNotFound_rejectTheRecruitmentProcessOnCandidate(){
        when(  recruitmentProcessRepository.findById(10L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.rejectTheRecruitmentProcessOnCandidate( 10L);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Process not found", result.getBody().getMessage());

    }


    @Test
    void shouldHandleNoInterviewsDetectedToDelete_rejectTheRecruitmentProcessOnCandidate(){
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .id(10L)
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviews(Collections.emptyList())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();
        when(  recruitmentProcessRepository.findById(10L)).thenReturn(Optional.ofNullable(recruitmentProcess));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.rejectTheRecruitmentProcessOnCandidate( 10L);

        System.out.println(result.getBody());
        System.out.println(result.getBody().getMessage());
        System.out.println(result.getBody().getData());

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(     "Process status changed to rejected no interviews detecdet to delete", result.getBody().getMessage());

    }



    @Test
    void shouldReturn200_whenInitatingRecruitmentProcess(){
        //candidate client exist by ıd true
        when(candidateClient.candidateExistsById(200L)).thenReturn(true);
        when(jobPostingClient.jobPostingExistsById(300L)).thenReturn(true);
        RecruitmentProcess recruitmentProcess = new RecruitmentProcess();
        recruitmentProcess.setId(25L);
        recruitmentProcess.setCandidateId(200L);
        recruitmentProcess.setJobPostingId(300L);
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

        ArrayList<Interview> interviewList = new ArrayList();
        interviewList.add(mockInterview);
        recruitmentProcess.setInterviews(interviewList);
        recruitmentProcess.setInterviewProcesses(InterviewProcesses.HR_SCREENING);
        recruitmentProcess.setCreatedAt(new Date());
        recruitmentProcess.setLastUpdated(LocalDateTime.now().minusDays(3));


        when(recruitmentProcessRepository.findByCandidateIdAndJobPostingId(200L,300L)).thenReturn(Optional.empty());


        RecruitmentProcessInitiateRequestDTO recruitmentProcessInitiateRequestDTO  = new RecruitmentProcessInitiateRequestDTO();
        recruitmentProcessInitiateRequestDTO.setCandidateId(200L);
        recruitmentProcessInitiateRequestDTO.setJobPostingId(300L);
        recruitmentProcessInitiateRequestDTO.setCreatedAt(new Date());
        recruitmentProcessInitiateRequestDTO.setLastUpdated(LocalDateTime.now().minusDays(3));

        when(recruitmentProcessMapper.toInitiateRecruitmentProcess(recruitmentProcessInitiateRequestDTO)).thenReturn(recruitmentProcess);
        when(recruitmentProcessRepository.save(any()))
                .thenReturn(recruitmentProcess);

        InterviewInitiateRequestDTO dto = new InterviewInitiateRequestDTO();
        dto.setInterviewRatingQuote("Candidate has strong communication and analytical skills");
        dto.setInterviewScore(null);
        dto.setCandidateId(200L);
        dto.setInterviewProcesses(InterviewProcesses.HR_SCREENING);

        dto.setInterviewerName("Ahmet Yılmaz");
        dto.setGeneralImpression("Strong candidate with good potential for leadership");
        dto.setCandidateTeamCompabilityNote("Seems to collaborate well, fits with team culture");

        List<InterviewQuestions> questions = new ArrayList<>();
        questions.add(null);
        questions.add(null);
        dto.setInterviewQuestions(questions);

        dto.setLocatedInTheSameCity(true);
        dto.setCandidateCanWorkInTheOffice(true);
        dto.setCandidateCareerGoals("Wants to grow into a senior software engineer role");
        dto.setSalaryExpectation("70,000 TRY per month");
        dto.setAvailabilityToStart("2 weeks notice");

        dto.setInterviewScheduleTime("2025-08-20T10:00:00");
        dto.setCreatedAt(new Date());
        dto.setLastUpdated(LocalDateTime.now());

        recruitmentProcessInitiateRequestDTO.setInterviews(List.of(dto));

        InterviewInitiateResponseDTO interviewInitiateResponseDTO = new InterviewInitiateResponseDTO();
        interviewInitiateResponseDTO.setCandidateId(200L);
        interviewInitiateResponseDTO.setInterviewerName("Ahmet Yılmaz");
        interviewInitiateResponseDTO.setInterviewQuestions(null);
        interviewInitiateResponseDTO.setInterviewScore(5.6);
        interviewInitiateResponseDTO.setInterviewRatingQuote("candidate has a bright future");
        interviewInitiateResponseDTO.setInterviewScheduleTime(new Date());
        interviewInitiateResponseDTO.setCreatedAt(new Date());
        interviewInitiateResponseDTO.setLastUpdated(LocalDateTime.now().minusDays(3));

        RecruitmentProcessInitiateResponseDTO recruitmentProcessInitiateResponseDTO = new RecruitmentProcessInitiateResponseDTO();
        recruitmentProcessInitiateResponseDTO.setCandidateId(200L);
        recruitmentProcessInitiateResponseDTO.setInterviews(List.of(interviewInitiateResponseDTO));
        recruitmentProcessInitiateResponseDTO.setJobPostingId(300L);
        recruitmentProcessInitiateResponseDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
        recruitmentProcessInitiateResponseDTO.setCreatedAt(new Date());


        when(recruitmentProcessMapper.fromRecruitmentProcess(any()))
                .thenReturn(recruitmentProcessInitiateResponseDTO);



        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.initiateRecruitmentProcess( recruitmentProcessInitiateRequestDTO);
        RecruitmentProcessInitiateResponseDTO responseBody =  (RecruitmentProcessInitiateResponseDTO) result.getBody().getData();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(      result.getBody());
        assertEquals(  recruitmentProcessInitiateRequestDTO.getCandidateId() ,  responseBody.getCandidateId());
        assertEquals(  recruitmentProcessInitiateRequestDTO.getJobPostingId() ,  responseBody.getJobPostingId());

        assertNotNull(      result.getBody());



    }


    @Test
    void shouldHandleCandidateDoesNotExists_whenInitatingRecruitmentProcess(){
        RecruitmentProcessInitiateRequestDTO recruitmentProcessInitiateRequestDTO  = new RecruitmentProcessInitiateRequestDTO();
        recruitmentProcessInitiateRequestDTO.setCandidateId(200L);
        recruitmentProcessInitiateRequestDTO.setCreatedAt(new Date());
        recruitmentProcessInitiateRequestDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
        recruitmentProcessInitiateRequestDTO.setJobPostingId(300L);


        when(candidateClient.candidateExistsById(200L)).thenReturn(false);

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.initiateRecruitmentProcess( recruitmentProcessInitiateRequestDTO);

        System.out.println(result.getBody());
        System.out.println(result.getBody().getMessage());
        System.out.println(result.getBody().getData());

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(     "Candidate Does Not Exists", result.getBody().getMessage());


    }

    @Test
    void jobPostingDoesNotExists_whenInitatingRecruitmentProcess(){
        RecruitmentProcessInitiateRequestDTO recruitmentProcessInitiateRequestDTO  = new RecruitmentProcessInitiateRequestDTO();
        recruitmentProcessInitiateRequestDTO.setCandidateId(200L);
        recruitmentProcessInitiateRequestDTO.setCreatedAt(new Date());
        recruitmentProcessInitiateRequestDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
        recruitmentProcessInitiateRequestDTO.setJobPostingId(300L);


        when(candidateClient.candidateExistsById(200L)).thenReturn(true);
        when(jobPostingClient.jobPostingExistsById(recruitmentProcessInitiateRequestDTO.getJobPostingId())).thenReturn(false);

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.initiateRecruitmentProcess( recruitmentProcessInitiateRequestDTO);

        System.out.println(result.getBody());
        System.out.println(result.getBody().getMessage());
        System.out.println(result.getBody().getData());

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(     "Job Posting Does Not Exists", result.getBody().getMessage());


    }


    @Test
    void processAlreadyInitiated_whenInitatingRecruitmentProcess(){
        when(candidateClient.candidateExistsById(200L)).thenReturn(true);
        RecruitmentProcessInitiateRequestDTO recruitmentProcessInitiateRequestDTO  = new RecruitmentProcessInitiateRequestDTO();
        recruitmentProcessInitiateRequestDTO.setCandidateId(200L);
        recruitmentProcessInitiateRequestDTO.setCreatedAt(new Date());
        recruitmentProcessInitiateRequestDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
        recruitmentProcessInitiateRequestDTO.setJobPostingId(300L);

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
                .interviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW)
                .interviews(List.of(mockInterview))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        when(jobPostingClient.jobPostingExistsById(recruitmentProcessInitiateRequestDTO.getJobPostingId())).thenReturn(true);
        when(recruitmentProcessRepository.findByCandidateIdAndJobPostingId(200L,300L)).thenReturn(Optional.ofNullable(recruitmentProcess));


        when(candidateClient.candidateExistsById(200L)).thenReturn(true);
        when(jobPostingClient.jobPostingExistsById(recruitmentProcessInitiateRequestDTO.getJobPostingId())).thenReturn(true);

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.initiateRecruitmentProcess( recruitmentProcessInitiateRequestDTO);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(     "Process Already Initiated with Candidate on this JobPosting", result.getBody().getMessage());


    }


    //buradan alıp devam edelim inşalalh bugün yarın servis integrasyon tamamen tüm testler biter githuba pushlarız
    //***

    @Test
    void shouldReturn200_whenForwardToTheTechnicalInterviewProcess() throws ParseException {

        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .id(10L)
                .candidateId(25L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviews(new ArrayList<>())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        when(recruitmentProcessRepository.findByCandidateIdAndId(25L,10L))
                .thenReturn(Optional.of(recruitmentProcess));

        InterviewTechnicalInterviewRequestDTO interviewTechnicalInterviewRequestDTO = new InterviewTechnicalInterviewRequestDTO();
        interviewTechnicalInterviewRequestDTO.setInterviewerName("John Doe");
        interviewTechnicalInterviewRequestDTO.setCandidateId(25L);
        interviewTechnicalInterviewRequestDTO.setInterviewRatingQuote("Candidate shows strong problem-solving skills");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(8.5);
        interviewTechnicalInterviewRequestDTO.setInterviewScore(interviewScoreDTO);

        interviewTechnicalInterviewRequestDTO.setCodeExerciseUrl("https://github.com/example/code-exercise");
        interviewTechnicalInterviewRequestDTO.setCodeQualityScore(90);
        interviewTechnicalInterviewRequestDTO.setTechnicalNotes("Clean code, good naming conventions");
        interviewTechnicalInterviewRequestDTO.setCandidateTechnicalBackgroundNote("5 years of backend development experience");
        interviewTechnicalInterviewRequestDTO.setTechnicalKnowledgeScore(TechnicalKnowledgeScore.ABOVE_AVERAGE);
        interviewTechnicalInterviewRequestDTO.setInterviewQuestions(new ArrayList<>());
        interviewTechnicalInterviewRequestDTO.setInterviewScheduleTime("2025-08-20 14:30:00");
        interviewTechnicalInterviewRequestDTO.setCreatedAt(new Date());
        interviewTechnicalInterviewRequestDTO.setLastUpdated(LocalDateTime.now());

        String jsonData = interviewMapper.toJsonTechnical(
                new TechnicalInterviewDataDTO(
                        interviewTechnicalInterviewRequestDTO.getCodeExerciseUrl(),
                        interviewTechnicalInterviewRequestDTO.getCodeQualityScore(),
                        interviewTechnicalInterviewRequestDTO.getTechnicalNotes(),
                        interviewTechnicalInterviewRequestDTO.getCandidateTechnicalBackgroundNote(),
                        interviewTechnicalInterviewRequestDTO.getTechnicalKnowledgeScore()
                )
        );


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date schedulerDate = formatter.parse(interviewTechnicalInterviewRequestDTO.getInterviewScheduleTime());
        InterviewTechicalResponseDTO responseDTO = new InterviewTechicalResponseDTO();
        responseDTO.setCandidateId(25L);
        responseDTO.setInterviewRatingQuote(interviewTechnicalInterviewRequestDTO.getInterviewRatingQuote());
        responseDTO.setInterviewerName("John Doe");
        responseDTO.setInterviewScore(interviewTechnicalInterviewRequestDTO.getInterviewScore().getScore());
        responseDTO.setProcess(InterviewProcesses.TECHNICAL_INTERVIEW);
        responseDTO.setInterviewQuestions(new ArrayList<>());
        responseDTO.setInterviewScheduleTime(new Date());
        responseDTO.setCreatedAt(new Date());
        responseDTO.setLastUpdated(LocalDateTime.now());


        when(interviewMapper.fromForwardToTechnical(any(Interview.class))).thenReturn(responseDTO);
        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.forwardToTheTechnicalInterviewProcess(10L, interviewTechnicalInterviewRequestDTO);

        System.out.println(result.getBody().getData());
    }

    @Test
    void processDoesNotExists_whenForwardToTheTechnicalInterviewProcess(){
        InterviewTechnicalInterviewRequestDTO interviewTechnicalInterviewRequestDTO = new InterviewTechnicalInterviewRequestDTO();
        interviewTechnicalInterviewRequestDTO.setInterviewerName("John Doe");
        interviewTechnicalInterviewRequestDTO.setCandidateId(25L);
        interviewTechnicalInterviewRequestDTO.setInterviewRatingQuote("Candidate shows strong problem-solving skills");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(8.5);
        interviewTechnicalInterviewRequestDTO.setInterviewScore(interviewScoreDTO);

        interviewTechnicalInterviewRequestDTO.setCodeExerciseUrl("https://github.com/example/code-exercise");
        interviewTechnicalInterviewRequestDTO.setCodeQualityScore(90);
        interviewTechnicalInterviewRequestDTO.setTechnicalNotes("Clean code, good naming conventions");
        interviewTechnicalInterviewRequestDTO.setCandidateTechnicalBackgroundNote("5 years of backend development experience");
        interviewTechnicalInterviewRequestDTO.setTechnicalKnowledgeScore(TechnicalKnowledgeScore.ABOVE_AVERAGE);
        interviewTechnicalInterviewRequestDTO.setInterviewQuestions(new ArrayList<>());
        interviewTechnicalInterviewRequestDTO.setInterviewScheduleTime("2025-08-20 14:30:00");
        interviewTechnicalInterviewRequestDTO.setCreatedAt(new Date());
        interviewTechnicalInterviewRequestDTO.setLastUpdated(LocalDateTime.now());

        String jsonData = interviewMapper.toJsonTechnical(
                new TechnicalInterviewDataDTO(
                        interviewTechnicalInterviewRequestDTO.getCodeExerciseUrl(),
                        interviewTechnicalInterviewRequestDTO.getCodeQualityScore(),
                        interviewTechnicalInterviewRequestDTO.getTechnicalNotes(),
                        interviewTechnicalInterviewRequestDTO.getCandidateTechnicalBackgroundNote(),
                        interviewTechnicalInterviewRequestDTO.getTechnicalKnowledgeScore()
                )
        );


        when(recruitmentProcessRepository.findByCandidateIdAndId( 25L,35L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.forwardToTheTechnicalInterviewProcess( 35L,interviewTechnicalInterviewRequestDTO);

        System.out.println(result.getBody());
        System.out.println(result.getBody().getMessage());
        System.out.println(result.getBody().getData());

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(     "Process does not exists.", result.getBody().getMessage());


    }

    @Test
    void processAlreadyOnTechnicalInterview_whenForwardToTheTechnicalInterviewProcess(){
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .id(10L)
                .candidateId(25L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW)
                .interviews(new ArrayList<>())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();
        InterviewTechnicalInterviewRequestDTO interviewTechnicalInterviewRequestDTO = new InterviewTechnicalInterviewRequestDTO();
        interviewTechnicalInterviewRequestDTO.setInterviewerName("John Doe");
        interviewTechnicalInterviewRequestDTO.setCandidateId(25L);
        interviewTechnicalInterviewRequestDTO.setInterviewRatingQuote("Candidate shows strong problem-solving skills");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(8.5);
        interviewTechnicalInterviewRequestDTO.setInterviewScore(interviewScoreDTO);

        interviewTechnicalInterviewRequestDTO.setCodeExerciseUrl("https://github.com/example/code-exercise");
        interviewTechnicalInterviewRequestDTO.setCodeQualityScore(90);
        interviewTechnicalInterviewRequestDTO.setTechnicalNotes("Clean code, good naming conventions");
        interviewTechnicalInterviewRequestDTO.setCandidateTechnicalBackgroundNote("5 years of backend development experience");
        interviewTechnicalInterviewRequestDTO.setTechnicalKnowledgeScore(TechnicalKnowledgeScore.ABOVE_AVERAGE);
        interviewTechnicalInterviewRequestDTO.setInterviewQuestions(new ArrayList<>());
        interviewTechnicalInterviewRequestDTO.setInterviewScheduleTime("2025-08-20 14:30:00");
        interviewTechnicalInterviewRequestDTO.setCreatedAt(new Date());
        interviewTechnicalInterviewRequestDTO.setLastUpdated(LocalDateTime.now());

        String jsonData = interviewMapper.toJsonTechnical(
                new TechnicalInterviewDataDTO(
                        interviewTechnicalInterviewRequestDTO.getCodeExerciseUrl(),
                        interviewTechnicalInterviewRequestDTO.getCodeQualityScore(),
                        interviewTechnicalInterviewRequestDTO.getTechnicalNotes(),
                        interviewTechnicalInterviewRequestDTO.getCandidateTechnicalBackgroundNote(),
                        interviewTechnicalInterviewRequestDTO.getTechnicalKnowledgeScore()
                )
        );


        when(recruitmentProcessRepository.findByCandidateIdAndId( 25L,35L)).thenReturn(Optional.ofNullable(recruitmentProcess));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.forwardToTheTechnicalInterviewProcess( 35L,interviewTechnicalInterviewRequestDTO);

        System.out.println(result.getBody());
        System.out.println(result.getBody().getMessage());
        System.out.println(result.getBody().getData());

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(     "Process already on Technıcal Interview step.", result.getBody().getMessage());


    }

    @Test
    void shouldHandleInternalServer_whenForwardToTheTechnicalInterviewProcess(){
        InterviewTechnicalInterviewRequestDTO interviewTechnicalInterviewRequestDTO = new InterviewTechnicalInterviewRequestDTO();
        interviewTechnicalInterviewRequestDTO.setInterviewerName("John Doe");
        interviewTechnicalInterviewRequestDTO.setCandidateId(25L);
        interviewTechnicalInterviewRequestDTO.setInterviewRatingQuote("Candidate shows strong problem-solving skills");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(8.5);
        interviewTechnicalInterviewRequestDTO.setInterviewScore(interviewScoreDTO);

        interviewTechnicalInterviewRequestDTO.setCodeExerciseUrl("https://github.com/example/code-exercise");
        interviewTechnicalInterviewRequestDTO.setCodeQualityScore(90);
        interviewTechnicalInterviewRequestDTO.setTechnicalNotes("Clean code, good naming conventions");
        interviewTechnicalInterviewRequestDTO.setCandidateTechnicalBackgroundNote("5 years of backend development experience");
        interviewTechnicalInterviewRequestDTO.setTechnicalKnowledgeScore(TechnicalKnowledgeScore.ABOVE_AVERAGE);
        interviewTechnicalInterviewRequestDTO.setInterviewQuestions(new ArrayList<>());
        interviewTechnicalInterviewRequestDTO.setInterviewScheduleTime("2025-08-20 14:30:00");
        interviewTechnicalInterviewRequestDTO.setCreatedAt(new Date());
        interviewTechnicalInterviewRequestDTO.setLastUpdated(LocalDateTime.now());

        String jsonData = interviewMapper.toJsonTechnical(
                new TechnicalInterviewDataDTO(
                        interviewTechnicalInterviewRequestDTO.getCodeExerciseUrl(),
                        interviewTechnicalInterviewRequestDTO.getCodeQualityScore(),
                        interviewTechnicalInterviewRequestDTO.getTechnicalNotes(),
                        interviewTechnicalInterviewRequestDTO.getCandidateTechnicalBackgroundNote(),
                        interviewTechnicalInterviewRequestDTO.getTechnicalKnowledgeScore()
                )
        );


        when(recruitmentProcessRepository.findByCandidateIdAndId( 25L,35L)).thenThrow(new RuntimeException("Runtime Error"));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.forwardToTheTechnicalInterviewProcess( 35L,interviewTechnicalInterviewRequestDTO);

        System.out.println(result.getBody());
        System.out.println(result.getBody().getMessage());
        System.out.println(result.getBody().getData());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(     "Server Error.", result.getBody().getMessage());
    }

    @Test
    void shouldReturn200_whenForwardToTheCaseStudyInterviewProcess() throws Exception {

            RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                    .id(10L)
                    .candidateId(25L)
                    .jobPostingId(300L)
                    .interviewProcesses(InterviewProcesses.HR_SCREENING)
                    .interviews(new ArrayList<>())
                    .createdAt(new Date())
                    .lastUpdated(LocalDateTime.now())
                    .build();

            when(recruitmentProcessRepository.findByCandidateIdAndId(25L,10L))
                    .thenReturn(Optional.of(recruitmentProcess));

            CaseStudyInterviewRequestDTO caseStudyInterviewRequestDTO = new CaseStudyInterviewRequestDTO();
            caseStudyInterviewRequestDTO.setInterviewerName("Mock Interviewewr name");
            caseStudyInterviewRequestDTO.setCandidateId(25L);
            InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
            interviewScoreDTO.setScore(5.9);
            caseStudyInterviewRequestDTO.setInterviewScore(interviewScoreDTO);
            caseStudyInterviewRequestDTO.setInterviewScheduleTime("2025-05-21 14:00:00");

            Interview mockInterview = Interview.builder()
                    .candidateId(25L)
                    .interviewerName(caseStudyInterviewRequestDTO.getInterviewerName())
                    .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2025-05-21 14:00:00"))
                    .build();

            CaseStudyResponseDTO caseStudyResponseDTO = new CaseStudyResponseDTO();
            caseStudyResponseDTO.setCandidateId(25L);
            caseStudyResponseDTO.setInterviewerName("Mock Interviewewr name");
            caseStudyResponseDTO.setInterviewScheduleTime( mockInterview.getInterviewScheduleTime());
            caseStudyResponseDTO.setCreatedAt(new Date());
            caseStudyResponseDTO.setLastUpdated(LocalDateTime.now());


            when(interviewMapper.toforwardToTheCaseStudyInterviewProcess(any(), any()))
                    .thenReturn(mockInterview);
            when(interviewMapper.fromForwardToCaseStudy(any()))
                    .thenReturn(caseStudyResponseDTO);
            when(interviewRepository.save(any()))
                    .thenReturn(mockInterview);

            ResponseEntity<ApiResponse> result =
                    recruitmentProcessService.forwardToTheCaseStudyInterviewProcess(10L, caseStudyInterviewRequestDTO);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody().getData());
            assertEquals(caseStudyInterviewRequestDTO.getInterviewerName(),caseStudyResponseDTO.getInterviewerName());


        }


    @Test
    void shouldHandleProcessDoesNotExists_wheninitiateTheCaseStudyInterviewProcess() throws Exception {

        when(recruitmentProcessRepository.findByCandidateIdAndId( 200L,20L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.forwardToTheCaseStudyInterviewProcess(10L, any());

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Process does not exists.",result.getBody().getMessage());

    }

    @Test
    void shouldHandleProcessAlreadyOnCase_whenForwardToTheCaseStudyInterviewProcess() throws Exception {

        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .id(10L)
                .candidateId(25L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviews(new ArrayList<>())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        CaseStudyInterviewRequestDTO caseStudyInterviewRequestDTO = new CaseStudyInterviewRequestDTO();
        caseStudyInterviewRequestDTO.setInterviewerName("Mock Interviewewr name");
        caseStudyInterviewRequestDTO.setCandidateId(25L);
        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(5.9);
        caseStudyInterviewRequestDTO.setInterviewScore(interviewScoreDTO);
        caseStudyInterviewRequestDTO.setInterviewScheduleTime("2025-05-21 14:00:00");

        when(recruitmentProcessRepository.findByCandidateIdAndId(25L,10L))
                .thenReturn(Optional.of(recruitmentProcess));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.forwardToTheCaseStudyInterviewProcess(10L, caseStudyInterviewRequestDTO);


        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Process already on Case Study Interview step.",result.getBody().getMessage());


    }

    @Test
    void shouldHandleInternalServerError_whenForwardToTheCaseStudyInterviewProcess() throws Exception {

        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .id(10L)
                .candidateId(25L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviews(new ArrayList<>())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        CaseStudyInterviewRequestDTO caseStudyInterviewRequestDTO = new CaseStudyInterviewRequestDTO();
        caseStudyInterviewRequestDTO.setInterviewerName("Mock Interviewewr name");
        caseStudyInterviewRequestDTO.setCandidateId(25L);
        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(5.9);
        caseStudyInterviewRequestDTO.setInterviewScore(interviewScoreDTO);
        caseStudyInterviewRequestDTO.setInterviewScheduleTime("2025-05-21 14:00:00");

        when(recruitmentProcessRepository.findByCandidateIdAndId(25L,10L))
                .thenThrow(new RuntimeException("Runtime Error"));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.forwardToTheCaseStudyInterviewProcess(10L, caseStudyInterviewRequestDTO);


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Server Error." ,result.getBody().getMessage());


    }



    public static RecruitmentProcess createCompleteMockProcessForCaseStudySteps() throws Exception {
        RecruitmentProcess process = RecruitmentProcess.builder()
                .id(1L)
                .candidateId(100L)
                .jobPostingId(200L)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2026-01-01"))
                .lastUpdated(LocalDateTime.now())
                .interviews(new ArrayList<>())
                .build();

        Interview hrInterview = Interview.builder()
                .id(10L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviewerName("HR Manager Burak")
                .interviewScore(4.5)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-10"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-05"))
                .processSpecificData("{\"generalImpression\":\"Positif\", \"salaryExpectation\":30k}")
                .process(process)
                .interviewQuestions(createHrQuestions())
                .build();

        Interview techInterview = Interview.builder()
                .id(11L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW)
                .interviewerName("Tech Lead")
                .interviewScore(8.2)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-20"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-15"))
                .processSpecificData(null)
                .process(process)
                .interviewQuestions(createTechQuestions())
                .build();
        Interview caseStudyInterview = Interview.builder()
                .id(12L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviewerName("Product Manager")
                .interviewScore(7.8)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-02-01"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-28"))
                .processSpecificData(null)
                .process(process)
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        process.getInterviews().add(hrInterview);
        process.getInterviews().add(techInterview);
        process.getInterviews().add(caseStudyInterview);

        return process;
    }

    private static List<InterviewQuestions> createHrQuestions() {
        return Arrays.asList(
                new InterviewQuestions(1L, "Why do you want to work here?", "I align with company values", 4.0, null, new Date()),
                new InterviewQuestions(2L, "What's your expected salary?", "Competitive market rate", 3.5, null, new Date())
        );
    }

    private static List<InterviewQuestions> createTechQuestions() {
        return Arrays.asList(
                new InterviewQuestions(3L, "Explain SOLID principles", "Gave complete examples", 9.0, null, new Date()),
                new InterviewQuestions(4L, "Solve this algorithm problem", "Optimal solution provided", 8.5, null, new Date())
        );
    }


    //buradan devam edek


    @Test
    void shouldReturn200_whenInitiateTheCaseStudyInterviewProcess() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        RecruitmentProcess recruitmentProcess = createCompleteMockProcessForCaseStudySteps();
        Interview caseStudyInterview = recruitmentProcess.getInterviews().get(2);
        caseStudyInterview.setProcessSpecificData("{}");
        caseStudyInterview.setCaseStudyProcesses(null);

        when(interviewRepository.findById( 12L))
                .thenReturn(Optional.ofNullable(caseStudyInterview));



        String interviewTime = "2023-12-15 14:00:00";





        InitialCaseStudyInterviewDataDTO initialCaseStudyInterviewDataDTO = new InitialCaseStudyInterviewDataDTO();
        CaseStudyScoreDTO caseStudyScoreDTO = new CaseStudyScoreDTO();
        caseStudyScoreDTO.setScore(6.5);
        initialCaseStudyInterviewDataDTO.setInterviewScheduleTime(interviewTime);

        initialCaseStudyInterviewDataDTO.setCaseStudyScore(caseStudyScoreDTO);
        initialCaseStudyInterviewDataDTO.setGivenCaseTitle("Given Case Title");
        initialCaseStudyInterviewDataDTO.setGivenCaseDeadline("20-07-2025");
        initialCaseStudyInterviewDataDTO.setGivenCaseUrl("mock@gmail.com");
        initialCaseStudyInterviewDataDTO.setGivenCaseContent("Given Case Mock content");



        InitiateCaseStudyResponseDTO mockResponse = new InitiateCaseStudyResponseDTO();
        mockResponse.setCandidateId(caseStudyInterview.getCandidateId());
        mockResponse.setInterviewerName(caseStudyInterview.getInterviewerName());
        mockResponse.setProcess(CaseStudyProcesses.INITIAL);
        mockResponse.setGivenCaseTitle(initialCaseStudyInterviewDataDTO.getGivenCaseTitle());
        mockResponse.setGivenCaseContent(initialCaseStudyInterviewDataDTO.getGivenCaseContent());
        mockResponse.setCaseStudyScore(initialCaseStudyInterviewDataDTO.getCaseStudyScore().getScore());
        mockResponse.setGivenCaseDeadline(initialCaseStudyInterviewDataDTO.getGivenCaseDeadline());
        mockResponse.setInterviewScheduleTime(caseStudyInterview.getInterviewScheduleTime());
        mockResponse.setCreatedAt(caseStudyInterview.getCreatedAt());
        mockResponse.setLastUpdated(caseStudyInterview.getLastUpdated());

        when(interviewMapper.fromInitateTheCaseStudyInterviewProcess(any())).thenReturn(mockResponse);

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.initiateTheCaseStudyInterviewProcess(12L, initialCaseStudyInterviewDataDTO);

        InitiateCaseStudyResponseDTO responseBody = (InitiateCaseStudyResponseDTO) result.getBody().getData();
        System.out.println(responseBody);
        assertEquals(initialCaseStudyInterviewDataDTO.getGivenCaseTitle(), responseBody.getGivenCaseTitle());
        assertEquals(initialCaseStudyInterviewDataDTO.getCaseStudyScore().getScore(), responseBody.getCaseStudyScore());
        assertEquals(caseStudyInterview.getInterviewerName(), responseBody.getInterviewerName());
        assertNotNull(result.getBody().getData());

    }

    @Test
    void shouldHandleInterviewDoesNotExists_whenInitiateTheCaseStudyInterviewProcess() throws Exception {
        when(interviewRepository.findById( 12L))
                .thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.initiateTheCaseStudyInterviewProcess(12L, any());

        assertEquals("Interview does not exists.", result.getBody().getMessage());
        assertEquals(409, result.getBody().getStatus());
    }

    @Test
    void shouldHandleInterviewAlreadyInCaseStudy_whenInitiateTheCaseStudyInterviewProcess() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        RecruitmentProcess process = RecruitmentProcess.builder()
                .id(1L)
                .candidateId(100L)
                .jobPostingId(200L)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2026-01-01"))
                .lastUpdated(LocalDateTime.now())
                .interviews(new ArrayList<>())
                .build();

        Interview caseStudyInterview = Interview.builder()
                .id(12L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviewerName("Product Manager")
                .interviewScore(7.8)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-02-01"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-28"))
                .processSpecificData(null)
                .process(process)
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();
        when(interviewRepository.findById( 12L))
                .thenReturn(Optional.ofNullable(caseStudyInterview));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.initiateTheCaseStudyInterviewProcess(12L, any());
        System.out.println(result.getBody().getMessage());
        assertEquals("Interview already in case study ınitial progress .", result.getBody().getMessage());
        assertEquals(409, result.getBody().getStatus());
    }


    @Test
    void shouldHandleInternalServerError_whenInitiateTheCaseStudyInterviewProcess() throws Exception {

        when(interviewRepository.findById( 12L))
                .thenThrow(new RuntimeException("runtime exception"));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.initiateTheCaseStudyInterviewProcess(12L, any());
        assertEquals("Server Error.", result.getBody().getMessage());
        assertEquals(500, result.getBody().getStatus());
    }


    @Test
    void shouldReturn200_whenSolveTheCaseStudyInterview() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        RecruitmentProcess recruitmentProcess = createCompleteMockProcessForCaseStudySteps();
        Interview caseStudyInterview = recruitmentProcess.getInterviews().get(2);
        caseStudyInterview.setProcessSpecificData("{}");
        caseStudyInterview.setCaseStudyProcesses(null);

        when(interviewRepository.findById(12L))
                .thenReturn(Optional.ofNullable(caseStudyInterview));


        String interviewTime = "2023-12-15T14:00:00";


        SolutionCaseStudyInterviewDTO solutionCaseStudyInterviewDTO = new SolutionCaseStudyInterviewDTO();
        solutionCaseStudyInterviewDTO.setCaseStudySolutionTitle("Case Study Solution Title");
        solutionCaseStudyInterviewDTO.setCaseStudySolutionDescriptino("Case Study Solution Description");
        solutionCaseStudyInterviewDTO.setInterviewerName("Mock Interviewer name");

        solutionCaseStudyInterviewDTO.setInterviewScheduleTime(interviewTime);




        SolutionCaseStudyResponseDTO solutionCaseStudyResponseDTO = new SolutionCaseStudyResponseDTO();
        solutionCaseStudyResponseDTO.setCandidateId(caseStudyInterview.getCandidateId());
        solutionCaseStudyResponseDTO.setInterviewerName(caseStudyInterview.getInterviewerName());


        solutionCaseStudyResponseDTO.setCaseStudySolutionTitle(solutionCaseStudyInterviewDTO.getCaseStudySolutionTitle());
        solutionCaseStudyResponseDTO.setCaseStudyScore(5.5);
        solutionCaseStudyResponseDTO.setInterviewScheduleTime(caseStudyInterview.getInterviewScheduleTime());
        solutionCaseStudyResponseDTO.setCreatedAt(caseStudyInterview.getCreatedAt());
        solutionCaseStudyResponseDTO.setLastUpdated(caseStudyInterview.getLastUpdated());





        when(interviewMapper.fromSolveTheCaseStudyInterviewProcess(any())).thenReturn(solutionCaseStudyResponseDTO);

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.solveTheCaseStudyInterview(12L, solutionCaseStudyInterviewDTO);

        SolutionCaseStudyResponseDTO responseBody = (SolutionCaseStudyResponseDTO) result.getBody().getData();
        assertEquals(solutionCaseStudyInterviewDTO.getCaseStudySolutionTitle(), responseBody.getCaseStudySolutionTitle());
        assertEquals(caseStudyInterview.getInterviewerName(), responseBody.getInterviewerName());
        assertNotNull(result.getBody().getData());
        System.out.println(responseBody);
    }

    @Test
    void shouldHandleInterviewDoesNotExists_whenSolveTheCaseStudyInterview() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();


        when(interviewRepository.findById( 12L))
                .thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.solveTheCaseStudyInterview(12L, any());
        System.out.println(result.getBody().getMessage());
        assertEquals("Interview does not exists.", result.getBody().getMessage());
        assertEquals(409, result.getBody().getStatus());
    }

    @Test
    void shouldHandleInterviewAlreadyInCaseStudySolution_whenSolveTheCaseStudyInterview() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        RecruitmentProcess process = RecruitmentProcess.builder()
                .id(1L)
                .candidateId(100L)
                .jobPostingId(200L)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2026-01-01"))
                .lastUpdated(LocalDateTime.now())
                .interviews(new ArrayList<>())
                .build();

        Interview caseStudyInterview = Interview.builder()
                .id(12L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .caseStudyProcesses(CaseStudyProcesses.SOLUTION)
                .interviewerName("Product Manager")
                .interviewScore(7.8)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-02-01"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-28"))
                .processSpecificData(null)
                .process(process)
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();
        when(interviewRepository.findById( 12L))
                .thenReturn(Optional.ofNullable(caseStudyInterview));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.solveTheCaseStudyInterview(12L, any());
        System.out.println(result.getBody().getMessage());
        assertEquals("Interview already in case study solution progress.", result.getBody().getMessage());
        assertEquals(409, result.getBody().getStatus());
    }

    @Test
    void shouldHandleInternalServerError_whenSolveTheCaseStudyInterview() throws Exception {
        when(interviewRepository.findById( 12L))
                .thenThrow(new RuntimeException("runtime exception"));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.solveTheCaseStudyInterview(12L, any());
        System.out.println(result.getBody().getMessage());
        assertEquals("Server Error.", result.getBody().getMessage());
        assertEquals(500, result.getBody().getStatus());
    }

    @Test
    void shouldReturn200_whenEvaluateTheCaseStudyInterview() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        RecruitmentProcess recruitmentProcess = createCompleteMockProcessForCaseStudySteps();
        Interview caseStudyInterview = recruitmentProcess.getInterviews().get(2);
        caseStudyInterview.setProcessSpecificData("{}");
        caseStudyInterview.setCaseStudyProcesses(null);

        when(interviewRepository.findById(12L))
                .thenReturn(Optional.ofNullable(caseStudyInterview));


        String interviewTime = "2023-12-15 14:00:00";


        EvaluateCaseStudyInterviewDTO evaluateCaseStudyInterviewDTO = new EvaluateCaseStudyInterviewDTO();
        CaseStudyScoreDTO caseStudyScoreDTO = new CaseStudyScoreDTO();
        caseStudyScoreDTO.setScore(5.5);
        evaluateCaseStudyInterviewDTO.setInterviewerName(caseStudyInterview.getInterviewerName());
        evaluateCaseStudyInterviewDTO.setCaseStudyScore(caseStudyScoreDTO);
        evaluateCaseStudyInterviewDTO.setGivenCasePresentationEvaluation("Case Study presentatino evalutaone");
        evaluateCaseStudyInterviewDTO.setRisksIdentified("Mock Risks I identify");
        evaluateCaseStudyInterviewDTO.setCandidateAnalyticThinkingNote("Mock Candidate Analythic thinking note is good");
        evaluateCaseStudyInterviewDTO.setInterviewScheduleTime(interviewTime);



        EvaluationCaseStudyResponseDTO evaluationCaseStudyResponseDTO = new EvaluationCaseStudyResponseDTO();
        evaluationCaseStudyResponseDTO.setCandidateId(caseStudyInterview.getCandidateId());
        evaluationCaseStudyResponseDTO.setInterviewerName(caseStudyInterview.getInterviewerName());
        evaluationCaseStudyResponseDTO.setProcess(recruitmentProcess);
        evaluationCaseStudyResponseDTO.setGivenCaseSolutionEvaluation(evaluateCaseStudyInterviewDTO.getGivenCaseSolutionEvaluation());
        evaluationCaseStudyResponseDTO.setGivenCasePresentationEvaluation(evaluateCaseStudyInterviewDTO.getGivenCasePresentationEvaluation());
        evaluationCaseStudyResponseDTO.setRisksIdentified(evaluateCaseStudyInterviewDTO.getRisksIdentified());
        evaluationCaseStudyResponseDTO.setCandidateAnalyticThinkingNote(evaluateCaseStudyInterviewDTO.getCandidateAnalyticThinkingNote());
        CaseStudyScoreDTO caseStudyScoreDTOResponse = new CaseStudyScoreDTO();
        caseStudyScoreDTO.setScore(5.5);
        evaluationCaseStudyResponseDTO.setCaseStudyScore(caseStudyScoreDTOResponse.getScore());
        evaluationCaseStudyResponseDTO.setInterviewScheduleTime(caseStudyInterview.getInterviewScheduleTime());
        evaluationCaseStudyResponseDTO.setCaseStudyScore(caseStudyScoreDTOResponse.getScore());
        evaluationCaseStudyResponseDTO.setCaseStudyScore(5.5);
        evaluationCaseStudyResponseDTO.setInterviewScheduleTime(caseStudyInterview.getInterviewScheduleTime());
        evaluationCaseStudyResponseDTO.setCreatedAt(caseStudyInterview.getCreatedAt());
        evaluationCaseStudyResponseDTO.setLastUpdated(caseStudyInterview.getLastUpdated());





        when(interviewMapper.fromEvaluateTheCaseStudyInterviewProcess(any())).thenReturn(evaluationCaseStudyResponseDTO);

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.evaluateTheCaseStudyInterview(12L, evaluateCaseStudyInterviewDTO);

        EvaluationCaseStudyResponseDTO responseBody = (EvaluationCaseStudyResponseDTO) result.getBody().getData();
        assertEquals(evaluateCaseStudyInterviewDTO.getGivenCaseSolutionEvaluation(), responseBody.getGivenCaseSolutionEvaluation());
        assertEquals(evaluateCaseStudyInterviewDTO.getInterviewerName(), responseBody.getInterviewerName());
        assertNotNull(result.getBody().getData());
    }

    @Test
    void shouldHandleInterviewDoesNotExists_whenEvaluateTheCaseStudyInterview() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();


        when(interviewRepository.findById( 12L))
                .thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.evaluateTheCaseStudyInterview(12L, any());
        System.out.println(result.getBody().getMessage());
        assertEquals("Interview does not exists.", result.getBody().getMessage());
        assertEquals(409, result.getBody().getStatus());
    }

    @Test
    void shouldHandleInterviewAlreadyInCaseStudyEvaluation_whenEvaluateTheCaseStudyInterview() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String interviewTime = "2023-12-15 14:00:00";

        Interview mockInterview = new Interview();
        mockInterview.setCandidateId(200L);
        mockInterview.setInterviewerName("Ahmet Yılmaz");
        mockInterview.setInterviewQuestions(null);
        mockInterview.setInterviewScore(5.6);
        mockInterview.setInterviewRatingQuote("candidate has a bright future");
        mockInterview.setInterviewScheduleTime(new Date());
        mockInterview.setCreatedAt(new Date());
        mockInterview.setCaseStudyProcesses(CaseStudyProcesses.EVALUATION);
        mockInterview.setLastUpdated(LocalDateTime.now().minusDays(3));
        mockInterview.setLastUpdated(LocalDateTime.now());
        mockInterview.setCreatedAt(new Date());


        EvaluateCaseStudyInterviewDTO evaluateCaseStudyInterviewDTO = new EvaluateCaseStudyInterviewDTO();
        CaseStudyScoreDTO caseStudyScoreDTO = new CaseStudyScoreDTO();
        caseStudyScoreDTO.setScore(5.5);
        evaluateCaseStudyInterviewDTO.setInterviewerName("mockInterviewerName");
        evaluateCaseStudyInterviewDTO.setCaseStudyScore(caseStudyScoreDTO);
        evaluateCaseStudyInterviewDTO.setGivenCasePresentationEvaluation("Case Study presentatino evalutaone");
        evaluateCaseStudyInterviewDTO.setRisksIdentified("Mock Risks I identify");
        evaluateCaseStudyInterviewDTO.setCandidateAnalyticThinkingNote("Mock Candidate Analythic thinking note is good");
        evaluateCaseStudyInterviewDTO.setInterviewScheduleTime(interviewTime);


        when(interviewRepository.findById( 12L))
                .thenReturn(Optional.of(mockInterview));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.evaluateTheCaseStudyInterview(12L, any());
        System.out.println(result.getBody().getMessage());
        assertEquals("Interview already in case study evaluatıon progress.", result.getBody().getMessage());
        assertEquals(409, result.getBody().getStatus());
    }

    @Test
    void shouldHandleInternalServerError_whenEvaluateTheCaseStudyInterview() throws Exception {
        when(interviewRepository.findById( 12L))
                .thenThrow(new RuntimeException("runtime exception"));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.evaluateTheCaseStudyInterview(12L, any());
        System.out.println(result.getBody().getMessage());
        assertEquals("Server Error.", result.getBody().getMessage());
        assertEquals(500, result.getBody().getStatus());
    }

    @Test
    void shouldReturn200_whenGetCandidateAverages() throws Exception {
        RecruitmentProcess recruitmentProcess = new RecruitmentProcess();
        recruitmentProcess.setId(25L);
        recruitmentProcess.setCandidateId(10L);
        recruitmentProcess.setJobPostingId(300L);
        recruitmentProcess.setCandidateId(10L);


        when(recruitmentProcessRepository.findByJobPostingId(300L))
                .thenReturn(List.of(recruitmentProcess));

        CandidateResponseDTO candidateResponseDTO = setupResponseDataWhenGetProperCandidates().get(0);
        when(candidateClient.getTheInduvualCandidate(10L)).thenReturn(ResponseEntity.ok(ApiResponse.success(candidateResponseDTO)));

        Interview hrInterview = Interview.builder()
                .id(10L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviewerName("HR Manager Burak")
                .interviewScore(4.5)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-10"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-05"))
                .processSpecificData("{\"generalImpression\":\"Positif\", \"salaryExpectation\":30k}")
                .process(recruitmentProcess)
                .interviewQuestions(createHrQuestions())
                .build();

        Interview techInterview = Interview.builder()
                .id(11L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW)
                .interviewerName("Tech Lead")
                .interviewScore(8.2)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-20"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-15"))
                .processSpecificData(null)
                .process(recruitmentProcess)
                .interviewQuestions(createTechQuestions())
                .build();
        Interview caseStudyInterview = Interview.builder()
                .id(12L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviewerName("Product Manager")
                .interviewScore(7.8)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-02-01"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-28"))
                .processSpecificData(null)
                .process(recruitmentProcess)
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode root = new ObjectMapper().createObjectNode();

         ObjectNode initNode = objectMapper.createObjectNode();
        ObjectNode solutionNode = objectMapper.createObjectNode();
        ObjectNode evaluationNode = objectMapper.createObjectNode();


    //yeni node oluşturuyoruz bu solution nodesi olacak

        initNode.put("givenCaseTitle","Given Case Title");
        initNode.put("givenCaseContent", "20-07-2025");
        initNode.put("givenCaseDeadline","25-07-2025");
        initNode.put("caseStudyScore",10.8);
            root.set("initCaseStudy", initNode);
    //roota bu nodeyi setliyoruz

        solutionNode.put("givenCaseSolutionUrl", "mockGivenCaseSolutionurl.com");
        solutionNode.put("caseStudySolutionTitle", "caseStudy mock solution title");
        solutionNode.put("caseStudySolutionDescriptino", "case study mock solution description");

            root.set("solutionCaseStudy", solutionNode);


        evaluationNode.put("givenCaseSolutionEvaluation", "given Case Solutino Evalation");
        evaluationNode.put("givenCasePresentationEvaluation", "Given Case Presentation Evaluation");
        evaluationNode.put("risksIdentified", "risk ı identified");
        evaluationNode.put("candidateAnalyticThinkingNote", "mock candidate Analytic ThinkingNote");
        evaluationNode.put("caseStudyScore",5.7);

        root.set("evaluationCaseStudy", evaluationNode);


        caseStudyInterview.setProcessSpecificData(objectMapper.writeValueAsString(root));
        ArrayList<Interview> arrayListInterview = new ArrayList<>();
        arrayListInterview.add(hrInterview);
        arrayListInterview.add(techInterview);
        arrayListInterview.add(caseStudyInterview);

        recruitmentProcess.setInterviews(arrayListInterview);


        List<FinalOverviewCandidateDTO> result =
                recruitmentProcessService.getCandidateAveragesOnJobPosting(300L);
        System.out.println(result);
        assertEquals("Mockcandidate1FirstName", result.get(0).getCandidateResponseDTO().getFirstName());
        assertEquals(5.7,result.get(0).getEVALUATION_CASE_STUDY());
        assertEquals(8.2,result.get(0).getTECHNICAL());
        assertNotNull( result);
    }

    @Test
    void shouldHandleEmptyRecruitmentProcessList_whenGetCandidateAverages() throws Exception {
        when(recruitmentProcessRepository.findByJobPostingId(300L))
                .thenReturn(Collections.emptyList());


        List<FinalOverviewCandidateDTO> result =
                recruitmentProcessService.getCandidateAveragesOnJobPosting(300L);
        System.out.println(result);
        assertEquals(Collections.emptyList(), result);

        assertNotNull( result);
    }

    @Test
    void shouldHandleNoInterviews_whenGetCandidateAverages() {
        RecruitmentProcess recruitmentProcess = new RecruitmentProcess();
        recruitmentProcess.setId(25L);
        recruitmentProcess.setCandidateId(10L);
        recruitmentProcess.setJobPostingId(300L);
        recruitmentProcess.setCandidateId(10L);
        ArrayList<Interview> arrayListInterview = new ArrayList<>();

        recruitmentProcess.setInterviews(arrayListInterview);

        List<FinalOverviewCandidateDTO> result =
                recruitmentProcessService.getCandidateAveragesOnJobPosting(300L);

        assertEquals(Collections.emptyList(), result);

        assertNotNull( result);
    }

    @Test
    void shouldHandleNullFields_whenGetCandidateAverages() throws Exception {
        RecruitmentProcess recruitmentProcess = new RecruitmentProcess();
        recruitmentProcess.setId(25L);
        recruitmentProcess.setCandidateId(10L);
        recruitmentProcess.setJobPostingId(300L);
        recruitmentProcess.setCandidateId(10L);


        when(recruitmentProcessRepository.findByJobPostingId(300L))
                .thenReturn(List.of(recruitmentProcess));

        CandidateResponseDTO candidateResponseDTO = setupResponseDataWhenGetProperCandidates().get(0);
        when(candidateClient.getTheInduvualCandidate(10L)).thenReturn(ResponseEntity.ok(ApiResponse.success(candidateResponseDTO)));

        Interview hrInterview = Interview.builder()
                .id(10L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviewerName("HR Manager Burak")
                .interviewScore(4.5)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-10"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-05"))
                .processSpecificData("{\"generalImpression\":\"Positif\", \"salaryExpectation\":30k}")
                .process(recruitmentProcess)
                .interviewQuestions(createHrQuestions())
                .build();

        Interview techInterview = Interview.builder()
                .id(11L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW)
                .interviewerName("Tech Lead")
                .interviewScore(8.2)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-20"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-15"))
                .processSpecificData(null)
                .process(recruitmentProcess)
                .interviewQuestions(createTechQuestions())
                .build();
        Interview caseStudyInterview = Interview.builder()
                .id(12L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviewerName("Product Manager")
                .interviewScore(7.8)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-02-01"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-28"))
                .processSpecificData(null)
                .process(recruitmentProcess)
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode root = new ObjectMapper().createObjectNode();

        ObjectNode initNode = objectMapper.createObjectNode();


        //yeni node oluşturuyoruz bu solution nodesi olacak

        initNode.put("givenCaseTitle","Given Case Title");
        initNode.put("givenCaseContent", "20-07-2025");
        initNode.put("givenCaseDeadline","25-07-2025");
        root.set("initCaseStudy", initNode);
        //roota bu nodeyi setliyoruz



        caseStudyInterview.setProcessSpecificData(objectMapper.writeValueAsString(root));
        ArrayList<Interview> arrayListInterview = new ArrayList<>();
        arrayListInterview.add(hrInterview);
        arrayListInterview.add(techInterview);
        arrayListInterview.add(caseStudyInterview);

        recruitmentProcess.setInterviews(arrayListInterview);


        List<FinalOverviewCandidateDTO> result =
                recruitmentProcessService.getCandidateAveragesOnJobPosting(300L);
        System.out.println(result);
        assertEquals("Mockcandidate1FirstName", result.get(0).getCandidateResponseDTO().getFirstName());
        assertNull(result.get(0).getEVALUATION_CASE_STUDY());
        assertNull(result.get(0).getINIT_CASE_STUDY());
        assertNotNull( result);
    }

    @Test
    void shouldHandleNullScores_whenGetCandidateAverages() throws Exception {
        RecruitmentProcess recruitmentProcess = new RecruitmentProcess();
        recruitmentProcess.setId(25L);
        recruitmentProcess.setCandidateId(10L);
        recruitmentProcess.setJobPostingId(300L);
        recruitmentProcess.setCandidateId(10L);


        when(recruitmentProcessRepository.findByJobPostingId(300L))
                .thenReturn(List.of(recruitmentProcess));

        CandidateResponseDTO candidateResponseDTO = setupResponseDataWhenGetProperCandidates().get(0);
        when(candidateClient.getTheInduvualCandidate(10L)).thenReturn(ResponseEntity.ok(ApiResponse.success(candidateResponseDTO)));

        Interview hrInterview = Interview.builder()
                .id(10L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviewerName("HR Manager Burak")
                .interviewScore(null)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-10"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-05"))
                .processSpecificData("{\"generalImpression\":\"Positif\", \"salaryExpectation\":30k}")
                .process(recruitmentProcess)
                .interviewQuestions(createHrQuestions())
                .build();

        Interview techInterview = Interview.builder()
                .id(11L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW)
                .interviewerName("Tech Lead")
                .interviewScore(null)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-20"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-15"))
                .processSpecificData(null)
                .process(recruitmentProcess)
                .interviewQuestions(createTechQuestions())
                .build();
        Interview caseStudyInterview = Interview.builder()
                .id(12L)
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviewerName("Product Manager")
                .interviewScore(null)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-02-01"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-28"))
                .processSpecificData(null)
                .process(recruitmentProcess)
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode root = new ObjectMapper().createObjectNode();

        ObjectNode initNode = objectMapper.createObjectNode();


        //yeni node oluşturuyoruz bu solution nodesi olacak

        initNode.put("givenCaseTitle","Given Case Title");
        initNode.put("givenCaseContent", "20-07-2025");
        initNode.put("givenCaseDeadline","25-07-2025");
        root.set("initCaseStudy", initNode);
        //roota bu nodeyi setliyoruz



        caseStudyInterview.setProcessSpecificData(objectMapper.writeValueAsString(root));
        ArrayList<Interview> arrayListInterview = new ArrayList<>();
        arrayListInterview.add(hrInterview);
        arrayListInterview.add(techInterview);
        arrayListInterview.add(caseStudyInterview);

        recruitmentProcess.setInterviews(arrayListInterview);


        List<FinalOverviewCandidateDTO> result =
                recruitmentProcessService.getCandidateAveragesOnJobPosting(300L);
        System.out.println(result);
        assertEquals("Mockcandidate1FirstName", result.get(0).getCandidateResponseDTO().getFirstName());
        assertTrue(Double.isNaN(result.get(0).getAVERAGE_SCORE()));
        assertNull(result.get(0).getEVALUATION_CASE_STUDY());
        assertNull(result.get(0).getINIT_CASE_STUDY());

        assertNotNull( result);
    }


    //buna bakmak lazım 18.08 bunu bitirip integrasyonlara geçmek lazım 
    @Test
    void shouldReturn200_whenUpdateTheCandidateRecruitmentProcess() throws Exception {
        CandidateResponseDTO candidateResponseDTO = setupResponseDataWhenGetProperCandidates().get(0);

        when(candidateClient.getTheInduvualCandidate(10L)).thenReturn(ResponseEntity.ok(ApiResponse.success(candidateResponseDTO)));

        RecruitmentProcess recruitmentProcess = new RecruitmentProcess();
        recruitmentProcess.setId(25L);
        recruitmentProcess.setCandidateId(10L);
        recruitmentProcess.setJobPostingId(300L);
        recruitmentProcess.setCandidateId(10L);

        when(recruitmentProcessRepository.findById(25L)).thenReturn(Optional.ofNullable(recruitmentProcess));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.updateTheCandidateRecruitmentProcess(25L,10L);
        System.out.println(result.getBody());
        System.out.println(result.getBody().getMessage());
        System.out.println(result.getBody().getErrors());
        System.out.println(result.getBody().getData());
        CandidateResponseDTO candidateResponseDTOBody = (CandidateResponseDTO) result.getBody().getData();
        assertEquals("Mockcandidate1FirstName", candidateResponseDTOBody.getFirstName());
        assertEquals(InterviewProcesses.FINAL_OVERVİEW,recruitmentProcess.getInterviewProcesses());
        assertEquals("mcokCandidate1Email", candidateResponseDTOBody.getEmail());
        assertNotNull( result);
    }

    @Test
    void shouldHandleProcessNotFound_whenUpdateTheCandidateRecruitmentProcess() throws Exception {
        CandidateResponseDTO candidateResponseDTO = setupResponseDataWhenGetProperCandidates().get(0);

        when(candidateClient.getTheInduvualCandidate(10L)).thenReturn(ResponseEntity.ok(ApiResponse.success(candidateResponseDTO)));

        RecruitmentProcess recruitmentProcess = new RecruitmentProcess();
        recruitmentProcess.setId(25L);
        recruitmentProcess.setCandidateId(10L);
        recruitmentProcess.setJobPostingId(300L);
        recruitmentProcess.setCandidateId(10L);

        when(recruitmentProcessRepository.findById(25L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.updateTheCandidateRecruitmentProcess(25L,10L);

        assertEquals("Process not found", result.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT,result.getStatusCode());
        assertNotNull( result);
    }

    @Test
    void shouldHandleInternalServerError_whenUpdateTheCandidateRecruitmentProcess() throws Exception {
        when(recruitmentProcessRepository.findById(25L))
                .thenThrow(new RuntimeException("runtime exception"));

        ResponseEntity<ApiResponse> result =
                recruitmentProcessService.updateTheCandidateRecruitmentProcess(25L, any());
        System.out.println(result.getBody().getMessage());
        assertEquals("Server Error", result.getBody().getMessage());
        assertEquals(500, result.getBody().getStatus());

    }


}
