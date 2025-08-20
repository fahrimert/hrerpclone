package com.hrerp.recruitmentmsfinal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hrerp.Client.CandidateClient;
import com.hrerp.Client.JobPostingClient;
import com.hrerp.dto.ApiResponse;
import com.hrerp.dto.CandidateResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyInterviewRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyProcesses;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyScoreDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.EvaluateCaseStudyDTO.EvaluateCaseStudyInterviewDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.InitiateCaseStudyDTO.InitialCaseStudyInterviewDataDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.SolutionCaseStudyDTO.SolutionCaseStudyInterviewDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos.InterviewInitiateRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos.RecruitmentProcessInitiateRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InterviewScoreDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.InterviewTechnicalInterviewRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.TechnicalKnowledgeScore;
import com.hrerp.dto.enums.Address;
import com.hrerp.model.Interview;
import com.hrerp.model.RecruitmentProcess;
import com.hrerp.model.enums.InterviewProcesses;
import com.hrerp.model.mapper.InterviewMapper;
import com.hrerp.model.mapper.RecruitmentProcessMapper;
import com.hrerp.repository.InterviewRepository;
import com.hrerp.repository.RecruitmentProcessRepository;
import com.hrerp.service.RecruitmentProcessService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Transactional
@AutoConfigureMockMvc

public class RecruitmentIntegrationTest {
    @MockitoBean
    private JobPostingClient jobPostingClient;

    @MockitoBean
    private CandidateClient candidateClient;

    @Autowired
    private RecruitmentProcessService service;

    @Autowired
    private RecruitmentProcessRepository recruitmentProcessRepository;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InterviewRepository interviewRepository;
    private ObjectMapper objectMapper;

    @Autowired
    private RecruitmentProcessMapper recruitmentProcessMapper;

    @Autowired
    private InterviewMapper interviewMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    //recruiterspesificupdate
    //recruiterspesificfetch
    //getTheProperCandidates
    //updateTheCandidateStatus

    //bunlar proxy görevinde olduğu için ve feign client üzerinden tam olarak nasıl entegrasyon testleri olur çözemediğim için
    //atlıyorum controller unit testleri yeterli işlevi görür herhalde

    @Test
    public void shouldReturn200_whenGetTheRecruitmentProcesses() throws Exception {

        when(candidateClient.candidateExistsById(200L)).thenReturn(true);
        Interview interview = Interview.builder()
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

        Interview interview2 = Interview.builder()
                .candidateId(200L)
                .interviewRatingQuote("Interview For Process 2 was good")
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviewerName("Murat Yılmaz")
                .processSpecificData("{\"caseStudyScore\":45}")
                .caseStudyProcesses(null)
                .interviewScore(85.0)
                .interviewScheduleTime(new Date())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviews(List.of(interview))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        RecruitmentProcess recruitmentProcess2 = RecruitmentProcess.builder()
                .candidateId(200L)
                .jobPostingId(400L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviews(List.of(interview2))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        ArrayList<RecruitmentProcess> recruitmentProcessArrayList = new ArrayList<>();
        recruitmentProcessArrayList.add(recruitmentProcess);
        recruitmentProcessArrayList.add(recruitmentProcess2);

        RecruitmentProcess savedProcess = recruitmentProcessRepository.save(recruitmentProcess);
        RecruitmentProcess savedProcess2 = recruitmentProcessRepository.save(recruitmentProcess2);

        mockMvc.perform(get("/api/v1/recruitment/public/getTheRecruitmentProcesses/{candidateId}", savedProcess.getCandidateId()))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data[0].candidateId").value(recruitmentProcess.getCandidateId()))
                .andExpect(jsonPath("$.data[0].interviews[0].interviewRatingQuote").value(recruitmentProcess.getInterviews().get(0).getInterviewRatingQuote()))
                .andExpect(jsonPath("$.data", hasSize(2)));

    }

    @Test
    public void shouldHandleCandidateDoesNotExists_whenGetTheRecruitmentProcesses() throws Exception {
        when(candidateClient.candidateExistsById(8L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/recruitment/public/getTheRecruitmentProcesses/{candidateId}", 200L))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Candidate Does Not Exists"));

    }

    @Test
    public void shouldHandleCandidateDoesntHaveAnyProcess_whenGetTheRecruitmentProcesses() throws Exception {

        when(candidateClient.candidateExistsById(8L)).thenReturn(true);
        recruitmentProcessRepository.deleteAllByCandidateId(8L);


        mockMvc.perform(get("/api/v1/recruitment/public/getTheRecruitmentProcesses/{candidateId}", 8L))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Candidate doesnt have any recruitment processes exist."));

    }

    @Test
    public void shouldHandleInternalServerError_whenGetTheRecruitmentProcesses() throws Exception {
        when(candidateClient.candidateExistsById(8L)).thenThrow(new RuntimeException("Runtime Exception occurred"));

        mockMvc.perform(get("/api/v1/recruitment/public/getTheRecruitmentProcesses/{candidateId}", 8L))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Server Error."));
    }

    @Test
    public void shouldReturn200_whenGetTheInduvualRecruitmentProcesse() throws Exception {

        Interview interview = Interview.builder()
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

        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviews(List.of(interview))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        RecruitmentProcess savedProcess = recruitmentProcessRepository.save(recruitmentProcess);
        when(candidateClient.candidateExistsById(savedProcess.getId())).thenReturn(true);

        mockMvc.perform(get("/api/v1/recruitment/public/getTheInduvualRecruitmentProcess/{candidateId}/{processId}", savedProcess.getId(), savedProcess.getCandidateId()))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.candidateId").value(recruitmentProcess.getCandidateId()))
                .andExpect(jsonPath("$.data.interviews[0].interviewRatingQuote").value(interview.getInterviewRatingQuote()))

                .andExpect(jsonPath("$.data.jobPostingId").value(recruitmentProcess.getJobPostingId()));
    }

    @Test
    public void shoulHandleProcessNotFound_whenGetTheInduvualRecruitmentProcesse() throws Exception {

        mockMvc.perform(get("/api/v1/recruitment/public/getTheInduvualRecruitmentProcess/{candidateId}/{processId}", 204L, 412L))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Process not found"));


    }

    @Test
    public void shouldHandleCandidateDoesNotExists_whenGetTheInduvualRecruitmentProcesse() throws Exception {

        Interview interview = Interview.builder()
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

        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviews(List.of(interview))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();
        RecruitmentProcess savedProcess = recruitmentProcessRepository.save(recruitmentProcess);
        when(candidateClient.candidateExistsById(savedProcess.getId())).thenReturn(false);

        mockMvc.perform(get("/api/v1/recruitment/public/getTheInduvualRecruitmentProcess/{candidateId}/{processId}", recruitmentProcess.getId(), recruitmentProcess.getCandidateId()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Candidate Does Not Exists"));

    }


    //method doğru çalışıyor integre testinde sorun var atlayacam bunu şimdilik
    @Test
    public void shouldReturn200AndRejectProcess_whenProcessExistsAndHasInterviews() throws Exception {
        Interview interview = Interview.builder()
                .candidateId(200L)
                .interviewRatingQuote("Positive impression")
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviewerName("John Doe")
                .processSpecificData("{\"caseStudyScore\":80}")
                .caseStudyProcesses(null)
                .interviewScore(85.0)
                .interviewScheduleTime(new Date())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();
        Interview savedInterview = interviewRepository.save(interview);
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviews(new ArrayList<>(List.of(savedInterview)))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();
        interview.setProcess(recruitmentProcess);
        interviewRepository.save(savedInterview);
        RecruitmentProcess savedProcess = recruitmentProcessRepository.save(recruitmentProcess);
        mockMvc.perform(post("/api/v1/recruitment/internal/rejectRecruitmentProcess/{processId}", savedProcess.getId()))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").value("Process successfully rejected and interviews deleted"));
    }

    @Test
    @Transactional

    public void shouldReturn200_whenInitiateTheRecruitmentProcess() throws Exception {
        InterviewInitiateRequestDTO interviewInitiateRequestDTO = new InterviewInitiateRequestDTO();
        interviewInitiateRequestDTO.setCandidateId(200L);
        interviewInitiateRequestDTO.setInterviewerName("Ahmet Yılmaz");
        interviewInitiateRequestDTO.setInterviewQuestions(null);

        interviewInitiateRequestDTO.setGeneralImpression("General Impression İs Candidate above average");
        interviewInitiateRequestDTO.setCandidateTeamCompabilityNote("candidate good on team communication");
        interviewInitiateRequestDTO.setLocatedInTheSameCity(true);
        interviewInitiateRequestDTO.setCandidateCanWorkInTheOffice(true);
        interviewInitiateRequestDTO.setSalaryExpectation("40k");
        interviewInitiateRequestDTO.setCandidateCareerGoals("career goals on ml and fullstack engineering");
        interviewInitiateRequestDTO.setAvailabilityToStart("tomorrow or 3 days later");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(5.6);
        interviewInitiateRequestDTO.setInterviewScore(interviewScoreDTO);
        interviewInitiateRequestDTO.setInterviewRatingQuote("candidate has a bright future");
        interviewInitiateRequestDTO.setInterviewScheduleTime("2025-08-20 14:30:00");
        interviewInitiateRequestDTO.setCreatedAt(new Date());
        interviewInitiateRequestDTO.setLastUpdated(LocalDateTime.now().minusDays(3));


        RecruitmentProcessInitiateRequestDTO recruitmentProcessInitiateRequestDTO = new RecruitmentProcessInitiateRequestDTO();
        recruitmentProcessInitiateRequestDTO.setCandidateId(200L);
        recruitmentProcessInitiateRequestDTO.setCreatedAt(new Date());
        recruitmentProcessInitiateRequestDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
        recruitmentProcessInitiateRequestDTO.setJobPostingId(300L);
        recruitmentProcessInitiateRequestDTO.setInterviews(List.of(interviewInitiateRequestDTO));


        when(candidateClient.candidateExistsById(200L)).thenReturn(true);
        when(jobPostingClient.jobPostingExistsById(recruitmentProcessInitiateRequestDTO.getJobPostingId())).thenReturn(true);

        RecruitmentProcess recruitmentProcess = recruitmentProcessMapper.toInitiateRecruitmentProcess(recruitmentProcessInitiateRequestDTO);


        mockMvc.perform(post("/api/v1/recruitment/internal/initiateRecruitmentProcess")
                        .content(objectMapper.writeValueAsString(recruitmentProcessInitiateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.interviews[0].interviewRatingQuote").value("candidate has a bright future"));

    }

    @Test
    @Transactional

    public void shouldHandleCandidateDoesNotExists_whenInitiateTheRecruitmentProcess() throws Exception {

        InterviewInitiateRequestDTO interviewInitiateRequestDTO = new InterviewInitiateRequestDTO();
        interviewInitiateRequestDTO.setCandidateId(200L);
        interviewInitiateRequestDTO.setInterviewerName("Ahmet Yılmaz");
        interviewInitiateRequestDTO.setInterviewQuestions(null);

        interviewInitiateRequestDTO.setGeneralImpression("General Impression İs Candidate above average");
        interviewInitiateRequestDTO.setCandidateTeamCompabilityNote("candidate good on team communication");
        interviewInitiateRequestDTO.setLocatedInTheSameCity(true);
        interviewInitiateRequestDTO.setCandidateCanWorkInTheOffice(true);
        interviewInitiateRequestDTO.setSalaryExpectation("40k");
        interviewInitiateRequestDTO.setCandidateCareerGoals("career goals on ml and fullstack engineering");
        interviewInitiateRequestDTO.setAvailabilityToStart("tomorrow or 3 days later");


        RecruitmentProcessInitiateRequestDTO recruitmentProcessInitiateRequestDTO = new RecruitmentProcessInitiateRequestDTO();
        recruitmentProcessInitiateRequestDTO.setCandidateId(200L);
        recruitmentProcessInitiateRequestDTO.setCreatedAt(new Date());
        recruitmentProcessInitiateRequestDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
        recruitmentProcessInitiateRequestDTO.setJobPostingId(300L);
        recruitmentProcessInitiateRequestDTO.setInterviews(List.of(interviewInitiateRequestDTO));
        when(candidateClient.candidateExistsById(200L)).thenReturn(false);


        mockMvc.perform(post("/api/v1/recruitment/internal/initiateRecruitmentProcess")
                        .content(objectMapper.writeValueAsString(recruitmentProcessInitiateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Candidate Does Not Exists"));

    }

    @Test
    @Transactional

    public void shouldHandleJobPostingDoesNotExists_whenInitiateTheRecruitmentProcess() throws Exception {

        InterviewInitiateRequestDTO interviewInitiateRequestDTO = new InterviewInitiateRequestDTO();
        interviewInitiateRequestDTO.setCandidateId(200L);
        interviewInitiateRequestDTO.setInterviewerName("Ahmet Yılmaz");
        interviewInitiateRequestDTO.setInterviewQuestions(null);

        interviewInitiateRequestDTO.setGeneralImpression("General Impression İs Candidate above average");
        interviewInitiateRequestDTO.setCandidateTeamCompabilityNote("candidate good on team communication");
        interviewInitiateRequestDTO.setLocatedInTheSameCity(true);
        interviewInitiateRequestDTO.setCandidateCanWorkInTheOffice(true);
        interviewInitiateRequestDTO.setSalaryExpectation("40k");
        interviewInitiateRequestDTO.setCandidateCareerGoals("career goals on ml and fullstack engineering");
        interviewInitiateRequestDTO.setAvailabilityToStart("tomorrow or 3 days later");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(5.6);
        interviewInitiateRequestDTO.setInterviewScore(interviewScoreDTO);
        interviewInitiateRequestDTO.setInterviewRatingQuote("candidate has a bright future");
        interviewInitiateRequestDTO.setInterviewScheduleTime("2025-08-20 14:30:00");
        interviewInitiateRequestDTO.setCreatedAt(new Date());
        interviewInitiateRequestDTO.setLastUpdated(LocalDateTime.now().minusDays(3));


        RecruitmentProcessInitiateRequestDTO recruitmentProcessInitiateRequestDTO = new RecruitmentProcessInitiateRequestDTO();
        recruitmentProcessInitiateRequestDTO.setCandidateId(200L);
        recruitmentProcessInitiateRequestDTO.setCreatedAt(new Date());
        recruitmentProcessInitiateRequestDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
        recruitmentProcessInitiateRequestDTO.setJobPostingId(300L);
        recruitmentProcessInitiateRequestDTO.setInterviews(List.of(interviewInitiateRequestDTO));


        when(candidateClient.candidateExistsById(200L)).thenReturn(true);
        when(jobPostingClient.jobPostingExistsById(recruitmentProcessInitiateRequestDTO.getJobPostingId())).thenReturn(false);


        mockMvc.perform(post("/api/v1/recruitment/internal/initiateRecruitmentProcess")
                        .content(objectMapper.writeValueAsString(recruitmentProcessInitiateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Job Posting Does Not Exists"));

    }

    @Test
    @Transactional
    public void shouldHandleProcessAlreadyInitiated_whenInitiateTheRecruitmentProcess() throws Exception {
        Interview interview = Interview.builder()
                .candidateId(200L)
                .interviewRatingQuote("Positive impression")
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviewerName("John Doe")
                .processSpecificData("{\"caseStudyScore\":80}")
                .caseStudyProcesses(null)
                .interviewScore(85.0)
                .interviewScheduleTime(new Date())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        Interview savedInterview = interviewRepository.save(interview);
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviews(new ArrayList<>(List.of(savedInterview)))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();
        recruitmentProcessRepository.save(recruitmentProcess);

        InterviewInitiateRequestDTO interviewInitiateRequestDTO = new InterviewInitiateRequestDTO();
        interviewInitiateRequestDTO.setCandidateId(200L);
        interviewInitiateRequestDTO.setInterviewerName("Ahmet Yılmaz");
        interviewInitiateRequestDTO.setInterviewQuestions(null);

        interviewInitiateRequestDTO.setGeneralImpression("General Impression İs Candidate above average");
        interviewInitiateRequestDTO.setCandidateTeamCompabilityNote("candidate good on team communication");
        interviewInitiateRequestDTO.setLocatedInTheSameCity(true);
        interviewInitiateRequestDTO.setCandidateCanWorkInTheOffice(true);
        interviewInitiateRequestDTO.setSalaryExpectation("40k");
        interviewInitiateRequestDTO.setCandidateCareerGoals("career goals on ml and fullstack engineering");
        interviewInitiateRequestDTO.setAvailabilityToStart("tomorrow or 3 days later");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(5.6);
        interviewInitiateRequestDTO.setInterviewScore(interviewScoreDTO);
        interviewInitiateRequestDTO.setInterviewRatingQuote("candidate has a bright future");
        interviewInitiateRequestDTO.setInterviewScheduleTime("2025-08-20 14:30:00");
        interviewInitiateRequestDTO.setCreatedAt(new Date());
        interviewInitiateRequestDTO.setLastUpdated(LocalDateTime.now().minusDays(3));


        RecruitmentProcessInitiateRequestDTO recruitmentProcessInitiateRequestDTO = new RecruitmentProcessInitiateRequestDTO();
        recruitmentProcessInitiateRequestDTO.setCandidateId(200L);
        recruitmentProcessInitiateRequestDTO.setCreatedAt(new Date());
        recruitmentProcessInitiateRequestDTO.setLastUpdated(LocalDateTime.now().minusDays(3));
        recruitmentProcessInitiateRequestDTO.setJobPostingId(300L);
        recruitmentProcessInitiateRequestDTO.setInterviews(List.of(interviewInitiateRequestDTO));

        when(candidateClient.candidateExistsById(recruitmentProcessInitiateRequestDTO.getCandidateId())).thenReturn(true);
        when(jobPostingClient.jobPostingExistsById(recruitmentProcessInitiateRequestDTO.getJobPostingId())).thenReturn(true);

        mockMvc.perform(post("/api/v1/recruitment/internal/initiateRecruitmentProcess")
                        .content(objectMapper.writeValueAsString(recruitmentProcessInitiateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Process Already Initiated with Candidate on this JobPosting"));

    }


    @Test
    @Transactional
    public void shouldReturn200_whenForwardToTheTechnicalInterviewProcess() throws Exception {

        InterviewTechnicalInterviewRequestDTO interviewTechnicalInterviewRequestDTO = new InterviewTechnicalInterviewRequestDTO();
        interviewTechnicalInterviewRequestDTO.setInterviewerName("John Doe");
        interviewTechnicalInterviewRequestDTO.setCandidateId(200L);
        interviewTechnicalInterviewRequestDTO.setInterviewRatingQuote("Candidate shows strong problem-solving skills");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(8.5);
        interviewTechnicalInterviewRequestDTO.setInterviewScore(interviewScoreDTO);

        interviewTechnicalInterviewRequestDTO.setCodeExerciseUrl("https://github.com/example/fahrimert");
        interviewTechnicalInterviewRequestDTO.setCodeQualityScore(90);
        interviewTechnicalInterviewRequestDTO.setTechnicalNotes("Clean code, good naming conventions");
        interviewTechnicalInterviewRequestDTO.setCandidateTechnicalBackgroundNote("5 years of backend development experience");
        interviewTechnicalInterviewRequestDTO.setTechnicalKnowledgeScore(TechnicalKnowledgeScore.ABOVE_AVERAGE);
        interviewTechnicalInterviewRequestDTO.setInterviewQuestions(new ArrayList<>());
        interviewTechnicalInterviewRequestDTO.setInterviewScheduleTime("2025-08-20 14:30:00");
        interviewTechnicalInterviewRequestDTO.setCreatedAt(new Date());
        interviewTechnicalInterviewRequestDTO.setLastUpdated(LocalDateTime.now());


        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        RecruitmentProcess savedRecruitmentProcess = recruitmentProcessRepository.save(recruitmentProcess);

        mockMvc.perform(post("/api/v1/recruitment/internal/{processId}/forwardToTheTechnicalInterviewProcess", savedRecruitmentProcess.getId())
                        .content(objectMapper.writeValueAsString(interviewTechnicalInterviewRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    @Transactional
    public void shouldHandleProcessDoesNotExists_whenForwardToTheTechnicalInterviewProcess() throws Exception {
        InterviewTechnicalInterviewRequestDTO interviewTechnicalInterviewRequestDTO = new InterviewTechnicalInterviewRequestDTO();

        mockMvc.perform(post("/api/v1/recruitment/internal/{processId}/forwardToTheTechnicalInterviewProcess", 214L)
                        .content(objectMapper.writeValueAsString(interviewTechnicalInterviewRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Process does not exists."));
    }

    @Test
    @Transactional
    public void shouldHandleProcessAlreadyInitiated_whenForwardToTheTechnicalInterviewProcess() throws Exception {
        Interview interview = Interview.builder()
                .candidateId(200L)
                .interviewRatingQuote("Positive impression")
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviewerName("John Doe")
                .processSpecificData("{\"caseStudyScore\":80}")
                .caseStudyProcesses(null)
                .interviewScore(85.0)
                .interviewScheduleTime(new Date())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        Interview savedInterview = interviewRepository.save(interview);
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW)
                .interviews(new ArrayList<>(List.of(savedInterview)))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();


        InterviewTechnicalInterviewRequestDTO interviewTechnicalInterviewRequestDTO = new InterviewTechnicalInterviewRequestDTO();
        interviewTechnicalInterviewRequestDTO.setInterviewerName("John Doe");
        interviewTechnicalInterviewRequestDTO.setCandidateId(200L);
        interviewTechnicalInterviewRequestDTO.setInterviewRatingQuote("Candidate shows strong problem-solving skills");

        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(8.5);
        interviewTechnicalInterviewRequestDTO.setInterviewScore(interviewScoreDTO);

        interviewTechnicalInterviewRequestDTO.setCodeExerciseUrl("https://github.com/example/fahrimert");
        interviewTechnicalInterviewRequestDTO.setCodeQualityScore(90);
        interviewTechnicalInterviewRequestDTO.setTechnicalNotes("Clean code, good naming conventions");
        interviewTechnicalInterviewRequestDTO.setCandidateTechnicalBackgroundNote("5 years of backend development experience");
        interviewTechnicalInterviewRequestDTO.setTechnicalKnowledgeScore(TechnicalKnowledgeScore.ABOVE_AVERAGE);
        interviewTechnicalInterviewRequestDTO.setInterviewQuestions(new ArrayList<>());
        interviewTechnicalInterviewRequestDTO.setInterviewScheduleTime("2025-08-20 14:30:00");
        interviewTechnicalInterviewRequestDTO.setCreatedAt(new Date());
        interviewTechnicalInterviewRequestDTO.setLastUpdated(LocalDateTime.now());

        RecruitmentProcess recruitmentProcess1 = recruitmentProcessRepository.save(recruitmentProcess);

        mockMvc.perform(post("/api/v1/recruitment/internal/{processId}/forwardToTheTechnicalInterviewProcess", recruitmentProcess1.getId())
                        .content(objectMapper.writeValueAsString(interviewTechnicalInterviewRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Process already on Technıcal Interview step."));
    }

    @Test
    @Transactional
    public void shouldReturn200_whenForwardToTheCaseStudyInterviewProcess() throws Exception {


        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .candidateId(25L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW)
                .interviews(new ArrayList<>())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();


        CaseStudyInterviewRequestDTO caseStudyInterviewRequestDTO = new CaseStudyInterviewRequestDTO();
        caseStudyInterviewRequestDTO.setInterviewerName("mock ınterviewer");
        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(5.6);
        caseStudyInterviewRequestDTO.setInterviewScore(interviewScoreDTO);
        caseStudyInterviewRequestDTO.setCandidateId(25L);
        caseStudyInterviewRequestDTO.setInterviewScheduleTime("2025-08-20 14:30:00");


        RecruitmentProcess savedRecruitmentProcess = recruitmentProcessRepository.save(recruitmentProcess);

        mockMvc.perform(post("/api/v1/recruitment/internal/{processId}/forwardToTheCaseStudyInterviewProcess", savedRecruitmentProcess.getId())
                        .content(objectMapper.writeValueAsString(caseStudyInterviewRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    @Transactional
    public void shouldHandleProcessDoesNotExists_whenForwardToTheCaseStudyInterviewProcess() throws Exception {
        CaseStudyInterviewRequestDTO caseStudyInterviewRequestDTO = new CaseStudyInterviewRequestDTO();

        mockMvc.perform(post("/api/v1/recruitment/internal/{processId}/forwardToTheCaseStudyInterviewProcess", 214L)
                        .content(objectMapper.writeValueAsString(caseStudyInterviewRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Process does not exists."));
    }

    @Test
    @Transactional
    public void shouldHandleProcessAlreadyInitiated_whenForwardToTheCaseStudyInterviewProcess() throws Exception {
        Interview interview = Interview.builder()
                .candidateId(200L)
                .interviewRatingQuote("Positive impression")
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviewerName("John Doe")
                .processSpecificData("{\"caseStudyScore\":80}")
                .caseStudyProcesses(null)
                .interviewScore(85.0)
                .interviewScheduleTime(new Date())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        Interview savedInterview = interviewRepository.save(interview);
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviews(new ArrayList<>(List.of(savedInterview)))
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

        RecruitmentProcess savedProcess = recruitmentProcessRepository.save(recruitmentProcess);

        CaseStudyInterviewRequestDTO caseStudyInterviewRequestDTO = new CaseStudyInterviewRequestDTO();
        caseStudyInterviewRequestDTO.setInterviewerName("mock ınterviewer");
        InterviewScoreDTO interviewScoreDTO = new InterviewScoreDTO();
        interviewScoreDTO.setScore(5.6);
        caseStudyInterviewRequestDTO.setInterviewScore(interviewScoreDTO);
        caseStudyInterviewRequestDTO.setCandidateId(200L);
        caseStudyInterviewRequestDTO.setInterviewScheduleTime("2025-08-20 14:30:00");

        mockMvc.perform(post("/api/v1/recruitment/internal/{processId}/forwardToTheCaseStudyInterviewProcess", savedProcess.getId())
                        .content(objectMapper.writeValueAsString(caseStudyInterviewRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Process already on Case Study Interview step."));
    }

    @Test
    @Transactional
    public void shouldReturn200_whenInitiateTheCaseStudyInterviewProcesses() throws Exception {
        Interview interview = Interview.builder()
                .candidateId(200L)
                .interviewRatingQuote("Positive impression")
                .interviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW)
                .interviewerName("John Doe")
                .processSpecificData("{\"caseStudyScore\":80}")
                .caseStudyProcesses(null)
                .interviewScore(85.0)
                .interviewScheduleTime(new Date())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();


        Interview savedInterview = interviewRepository.save(interview);

        InitialCaseStudyInterviewDataDTO dtoIniticalCaseStudy = new InitialCaseStudyInterviewDataDTO();
        CaseStudyScoreDTO scoreDTO = new CaseStudyScoreDTO();
        scoreDTO.setScore(6.9);
        dtoIniticalCaseStudy.setCaseStudyScore(scoreDTO);
        dtoIniticalCaseStudy.setInterviewerName("mock interviewer name");
        dtoIniticalCaseStudy.setGivenCaseContent("mock content");
        dtoIniticalCaseStudy.setGivenCaseTitle("mock title");
        dtoIniticalCaseStudy.setGivenCaseDeadline("2025-07-22 13:45:45");
        dtoIniticalCaseStudy.setInterviewScheduleTime("2025-07-22 13:40:45");


        mockMvc.perform(post("/api/v1/recruitment/internal/{interviewId}/initiateTheCaseStudyInterview", savedInterview.getId())
                        .content(objectMapper.writeValueAsString(dtoIniticalCaseStudy))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.interviewerName").value("John Doe"));

    }

    @Test
    @Transactional
    public void shouldHandleInterviewDoesNotExists_whenForwardToTheCaseStudyInterviewProcess() throws Exception {
        InitialCaseStudyInterviewDataDTO dtoIniticalCaseStudy = new InitialCaseStudyInterviewDataDTO();
        CaseStudyScoreDTO scoreDTO = new CaseStudyScoreDTO();
        scoreDTO.setScore(6.9);
        dtoIniticalCaseStudy.setCaseStudyScore(scoreDTO);
        dtoIniticalCaseStudy.setInterviewerName("mock interviewer name");
        dtoIniticalCaseStudy.setGivenCaseContent("mock content");
        dtoIniticalCaseStudy.setGivenCaseTitle("mock title");
        dtoIniticalCaseStudy.setGivenCaseDeadline("2025-07-22 13:45:45");
        dtoIniticalCaseStudy.setInterviewScheduleTime("2025-07-22 13:40:45");

        mockMvc.perform(post("/api/v1/recruitment/internal/{interviewId}/initiateTheCaseStudyInterview", 3213L)
                        .content(objectMapper.writeValueAsString(dtoIniticalCaseStudy))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Interview does not exists."))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @Transactional
    public void shouldHandleAlreadyInCaseStudyInitial_whenForwardToTheCaseStudyInterviewProcess() throws Exception {
        Interview interview = Interview.builder()
                .candidateId(200L)
                .interviewRatingQuote("Positive impression")
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviewerName("John Doe")
                .caseStudyProcesses(CaseStudyProcesses.INITIAL)
                .processSpecificData("{\"caseStudyScore\":80}")
                .interviewScore(85.0)
                .interviewScheduleTime(new Date())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();


        Interview savedInterview = interviewRepository.save(interview);

        InitialCaseStudyInterviewDataDTO dtoIniticalCaseStudy = new InitialCaseStudyInterviewDataDTO();
        CaseStudyScoreDTO scoreDTO = new CaseStudyScoreDTO();
        scoreDTO.setScore(6.9);
        dtoIniticalCaseStudy.setCaseStudyScore(scoreDTO);
        dtoIniticalCaseStudy.setInterviewerName("mock interviewer name");
        dtoIniticalCaseStudy.setGivenCaseContent("mock content");
        dtoIniticalCaseStudy.setGivenCaseTitle("mock title");
        dtoIniticalCaseStudy.setGivenCaseDeadline("2025-07-22 13:45:45");
        dtoIniticalCaseStudy.setInterviewScheduleTime("2025-07-22 13:40:45");


        mockMvc.perform(post("/api/v1/recruitment/internal/{interviewId}/initiateTheCaseStudyInterview", savedInterview.getId())
                        .content(objectMapper.writeValueAsString(dtoIniticalCaseStudy))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Interview already in case study ınitial progress ."))
               ;

    }

    @Test
    @Transactional
    public void shouldReturnSuccess_whenSolveCaseStudyWithValidData() throws Exception {


        Interview interview = Interview.builder()
                .candidateId(200L)
                .interviewRatingQuote("Case study assigned")
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviewerName("Interviewer mock for case study")
                .caseStudyProcesses(CaseStudyProcesses.INITIAL)
                .interviewScore(0.0)
                .interviewScheduleTime(new Date(System.currentTimeMillis() + 86400000))
                .build();
        interview = interviewRepository.save(interview);

        SolutionCaseStudyInterviewDTO requestDto = new SolutionCaseStudyInterviewDTO();
        requestDto.setCaseStudySolutionTitle("Çözüm Başlığı");
        requestDto.setCaseStudySolutionDescriptino("Detaylı çözüm açıklaması");
        requestDto.setInterviewerName("Mehmet Demir");
        requestDto.setInterviewScheduleTime("2024-07-22 14:30:00");
        CaseStudyScoreDTO caseStudyScoreDTO = new CaseStudyScoreDTO();
        caseStudyScoreDTO.setScore(6.7);
        requestDto.setCaseStudyScore(caseStudyScoreDTO);

        String requestJson = objectMapper.writeValueAsString(requestDto);
        MockMultipartFile jsonPart = new MockMultipartFile(
                "solutionCaseStudyInterviewDTO",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
        );

        mockMvc.perform(multipart("/api/v1/recruitment/public/{interviewId}/solveTheCaseStudyInterview", interview.getId())
                        .file(jsonPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.interviewerName").value(requestDto.getInterviewerName()))
                .andExpect(jsonPath("$.data.caseStudySolutionTitle").value(requestDto.getCaseStudySolutionTitle()))

                .andExpect(jsonPath("$.status").value(200));


    }
    @Test
    @Transactional
    public void shouldHandleInterviewDoesNotExists_whenSolveCaseStudyWithValidData() throws Exception {
        SolutionCaseStudyInterviewDTO requestDto = new SolutionCaseStudyInterviewDTO();
        requestDto.setCaseStudySolutionTitle("Çözüm Başlığı");
        requestDto.setCaseStudySolutionDescriptino("Detaylı çözüm açıklaması");
        requestDto.setInterviewerName("Mehmet Demir");
        requestDto.setInterviewScheduleTime("2024-07-22 14:30:00");
        CaseStudyScoreDTO caseStudyScoreDTO = new CaseStudyScoreDTO();
        caseStudyScoreDTO.setScore(6.7);
        requestDto.setCaseStudyScore(caseStudyScoreDTO);
        String requestJson = objectMapper.writeValueAsString(requestDto);
        MockMultipartFile jsonPart = new MockMultipartFile(
                "solutionCaseStudyInterviewDTO",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
        );
        mockMvc.perform(multipart("/api/v1/recruitment/public/{interviewId}/solveTheCaseStudyInterview", 3214L)
                        .file(jsonPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Interview does not exists."))
                .andExpect(jsonPath("$.status").value(409));


    }

    @Test
    @Transactional
    public void shouldHandleAlreadyInSolution_whenForwardToTheCaseStudyInterviewProcess() throws Exception {
        Interview interview = Interview.builder()
                .candidateId(200L)
                .interviewRatingQuote("Positive impression")
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviewerName("John Doe")
                .caseStudyProcesses(CaseStudyProcesses.SOLUTION)
                .processSpecificData("{\"caseStudyScore\":80}")
                .interviewScore(85.0)
                .interviewScheduleTime(new Date())
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();


        Interview savedInterview = interviewRepository.save(interview);

        SolutionCaseStudyInterviewDTO requestDto = new SolutionCaseStudyInterviewDTO();
        requestDto.setCaseStudySolutionTitle("Çözüm Başlığı");
        requestDto.setCaseStudySolutionDescriptino("Detaylı çözüm açıklaması");
        requestDto.setInterviewerName("Mehmet Demir");
        requestDto.setInterviewScheduleTime("2024-07-22 14:30:00");
        CaseStudyScoreDTO caseStudyScoreDTO = new CaseStudyScoreDTO();
        caseStudyScoreDTO.setScore(6.7);
        requestDto.setCaseStudyScore(caseStudyScoreDTO);


        String requestJson = objectMapper.writeValueAsString(requestDto);
        MockMultipartFile jsonPart = new MockMultipartFile(
                "solutionCaseStudyInterviewDTO",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
        );

        mockMvc.perform((multipart("/api/v1/recruitment/public/{interviewId}/solveTheCaseStudyInterview", savedInterview.getId())
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Interview already in case study solution progress."))
;

    }

    @Test
    @Transactional
    public void shouldReturnSuccess_whenEvaluateTheCaseStudyInterview() throws Exception {

        Interview interview = Interview.builder()
                .candidateId(200L)
                .interviewRatingQuote("Case study assigned")
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviewerName("Interviewer mock for case study")
                .caseStudyProcesses(CaseStudyProcesses.INITIAL)
                .interviewScore(0.0)
                .interviewScheduleTime(new Date(System.currentTimeMillis() + 86400000))
                .build();
        Interview savedInterview = interviewRepository.save(interview);

        EvaluateCaseStudyInterviewDTO requestDto = new EvaluateCaseStudyInterviewDTO();
        requestDto.setGivenCaseSolutionEvaluation("Mock Given Case Solutino Evaluation");
        requestDto.setGivenCasePresentationEvaluation("Mock Given Case Presentation Evaluation");
        requestDto.setRisksIdentified("Mock Risk I identified");
        requestDto.setInterviewScheduleTime("2024-07-22 14:30:00");
        CaseStudyScoreDTO caseStudyScoreDTO = new CaseStudyScoreDTO();
        caseStudyScoreDTO.setScore(6.7);
        requestDto.setInterviewerName("mock interviewer name for test ");
        requestDto.setCaseStudyScore(caseStudyScoreDTO);
        requestDto.setCandidateAnalyticThinkingNote("mock candidate analythic thinking note");
        mockMvc.perform(post("/api/v1/recruitment/internal/{interviewId}/evaluateTheCaseStudyInterview",savedInterview.getId())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.givenCaseSolutionEvaluation").value(requestDto.getGivenCaseSolutionEvaluation()))
                .andExpect(jsonPath("$.data.givenCasePresentationEvaluation").value(requestDto.getGivenCasePresentationEvaluation()))
;


    }

    @Test
    @Transactional
    public void shouldHandleInterviewDoesNotExists_whenEvaluateTheCaseStudyInterview() throws Exception {
        EvaluateCaseStudyInterviewDTO requestDto = new EvaluateCaseStudyInterviewDTO();
        requestDto.setGivenCaseSolutionEvaluation("Mock Given Case Solutino Evaluation");
        requestDto.setGivenCasePresentationEvaluation("Mock Given Case Presentation Evaluation");
        requestDto.setRisksIdentified("Mock Risk I identified");
        requestDto.setInterviewScheduleTime("2024-07-22 14:30:00");
        CaseStudyScoreDTO caseStudyScoreDTO = new CaseStudyScoreDTO();
        caseStudyScoreDTO.setScore(6.7);
        requestDto.setInterviewerName("mock interviewer name for test ");
        requestDto.setCaseStudyScore(caseStudyScoreDTO);
        requestDto.setCandidateAnalyticThinkingNote("mock candidate analythic thinking note");


        mockMvc.perform(post("/api/v1/recruitment/internal/{interviewId}/evaluateTheCaseStudyInterview",1234L)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Interview does not exists."));


    }



    @Test
    @Transactional
    public void shouldReturnSuccess_whenGetCandidateAveragesOnJobPosting() throws Exception {

        RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                .candidateId(200L)
                .jobPostingId(300L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();
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
        dto.setPhoneNumber("+90 555 123 4567");
        dto.setCvUrl("https://example.com/cv/fahri_mert.pdf");
        dto.setCreatedAt("2024-08-10 10:30:00");



        when(candidateClient.getTheInduvualCandidate(200L)).thenReturn(ResponseEntity.ok(ApiResponse.success(dto)));
        Interview hrInterview = Interview.builder()
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviewerName("HR Manager Burak")
                .interviewScore(4.5)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-10"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-05"))
                .processSpecificData("{\"generalImpression\":\"Positif\", \"salaryExpectation\":\"30k\"}")
                .process(recruitmentProcess)
                .build();

        Interview techInterview = Interview.builder()
                .candidateId(100L)
                .interviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW)
                .interviewerName("Tech Lead")
                .interviewScore(8.2)
                .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-20"))
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-15"))
                .processSpecificData(null)
                .process(recruitmentProcess)
                .build();
        Interview caseStudyInterview = Interview.builder()
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



        initNode.put("givenCaseTitle","Given Case Title");
        initNode.put("givenCaseContent", "20-07-2025");
        initNode.put("givenCaseDeadline","25-07-2025");
        initNode.put("caseStudyScore",5.8);
        root.set("initCaseStudy", initNode);

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
        RecruitmentProcess savedProcess= recruitmentProcessRepository.save(recruitmentProcess);


        mockMvc.perform(get("/api/v1/recruitment/internal/{jobPostingId}/getCandidateAveragesOnJobPosting",recruitmentProcess.getJobPostingId()))
                .andExpect(jsonPath("$[0].candidateResponseDTO.firstName").value(dto.getFirstName()))
                .andExpect(jsonPath("$[0].init_CASE_STUDY").value(5.8))
                .andExpect(jsonPath("$[0].evaluation_CASE_STUDY").value(5.7));
    }

        @Test
        @Transactional
        public void shouldHandleProcessDoesNotExists_whenGetCandidateAveragesOnJobPosting() throws Exception {
            mockMvc.perform(get("/api/v1/recruitment/internal/{jobPostingId}/getCandidateAveragesOnJobPosting",3123L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    //buradan devam 20.08 de bitirecez yarın

        @Test
        @Transactional
        public void shouldHandleNullFields_whenGetCandidateAveragesOnJobPosting() throws Exception {
            RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                    .candidateId(200L)
                    .jobPostingId(300L)
                    .interviewProcesses(InterviewProcesses.HR_SCREENING)
                    .createdAt(new Date())
                    .lastUpdated(LocalDateTime.now())
                    .build();
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
            dto.setPhoneNumber("+90 555 123 4567");
            dto.setCvUrl("https://example.com/cv/fahri_mert.pdf");
            dto.setCreatedAt("2024-08-10 10:30:00");



            when(candidateClient.getTheInduvualCandidate(200L)).thenReturn(ResponseEntity.ok(ApiResponse.success(dto)));
            Interview hrInterview = Interview.builder()
                    .candidateId(100L)
                    .interviewProcesses(InterviewProcesses.HR_SCREENING)
                    .interviewerName("HR Manager Burak")
                    .interviewScore(4.5)
                    .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-10"))
                    .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-05"))
                    .processSpecificData("{\"generalImpression\":\"Positif\", \"salaryExpectation\":\"30k\"}")
                    .process(recruitmentProcess)
                    .build();

            Interview techInterview = Interview.builder()
                    .candidateId(100L)
                    .interviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW)
                    .interviewerName("Tech Lead")
                    .interviewScore(8.2)
                    .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-20"))
                    .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-15"))
                    .processSpecificData(null)
                    .process(recruitmentProcess)
                    .build();
            Interview caseStudyInterview = Interview.builder()
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

            initNode.put("givenCaseTitle","Given Case Title");
            initNode.put("givenCaseContent", "20-07-2025");
            initNode.put("givenCaseDeadline","25-07-2025");
            root.set("initCaseStudy", initNode);

            solutionNode.put("givenCaseSolutionUrl", "mockGivenCaseSolutionurl.com");
            solutionNode.put("caseStudySolutionTitle", "caseStudy mock solution title");
            solutionNode.put("caseStudySolutionDescriptino", "case study mock solution description");

            root.set("solutionCaseStudy", solutionNode);


            evaluationNode.put("givenCaseSolutionEvaluation", "given Case Solutino Evalation");
            evaluationNode.put("givenCasePresentationEvaluation", "Given Case Presentation Evaluation");
            evaluationNode.put("risksIdentified", "risk ı identified");
            evaluationNode.put("candidateAnalyticThinkingNote", "mock candidate Analytic ThinkingNote");

            root.set("evaluationCaseStudy", evaluationNode);


            caseStudyInterview.setProcessSpecificData(objectMapper.writeValueAsString(root));
            ArrayList<Interview> arrayListInterview = new ArrayList<>();
            arrayListInterview.add(hrInterview);
            arrayListInterview.add(techInterview);
            arrayListInterview.add(caseStudyInterview);

            recruitmentProcess.setInterviews(arrayListInterview);
            RecruitmentProcess savedProcess= recruitmentProcessRepository.save(recruitmentProcess);


            mockMvc.perform(get("/api/v1/recruitment/internal/{jobPostingId}/getCandidateAveragesOnJobPosting",recruitmentProcess.getJobPostingId()))
                    .andExpect(jsonPath("$[0].candidateResponseDTO.firstName").value(dto.getFirstName()))
                    .andExpect(jsonPath("$[0].init_CASE_STUDY").doesNotExist())
                    .andExpect(jsonPath("$[0].evaluation_CASE_STUDY").doesNotExist());
        }


        @Test
        @Transactional
        public void shouldReturnSuccess_whenUpdateTheCandidateRecruitmentProcess() throws Exception {

            RecruitmentProcess recruitmentProcess = RecruitmentProcess.builder()
                    .candidateId(200L)
                    .jobPostingId(300L)
                    .createdAt(new Date())
                    .lastUpdated(LocalDateTime.now())
                    .build();
            CandidateResponseDTO dto = new CandidateResponseDTO();
            dto.setId(200L);
            dto.setFirstName("Ahmet");
            dto.setLastName("Yılmaz");

            Address address = new Address();
            address.setAddress("Cumhuriyet Mah. No:45, Besiktas");
            address.setCity("Istanbul");
            address.setCountry("Turkey");

            dto.setAddress(address);

            dto.setEmail("ahmet.yilmaz@example.com");
            dto.setLinkedin_url("https://linkedin.com/in/ahmetyilmaz");
            dto.setInstagram_url("https://instagram.com/ahmetyilmaz");
            dto.setFacebook_url("https://facebook.com/ahmetyilmaz");
            dto.setPhoneNumber("+90 532 987 6543");
            dto.setCvUrl("https://example.com/cv/ahmet_yilmaz.pdf");
            dto.setCreatedAt("2024-09-12 14:45:00");
            when(candidateClient.getTheInduvualCandidate(200L)).thenReturn(ResponseEntity.ok(ApiResponse.success(dto)));

            Interview hrInterview = Interview.builder()
                    .candidateId(200L)
                    .interviewProcesses(InterviewProcesses.HR_SCREENING)
                    .interviewerName("HR Specialist Ayşe")
                    .interviewScore(6.0)
                    .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-02-12"))
                    .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-02-05"))
                    .processSpecificData("{\"generalImpression\":\"Olumlu\", \"salaryExpectation\":\"40k\"}")
                    .process(recruitmentProcess)
                    .build();

            Interview techInterview = Interview.builder()
                    .candidateId(200L)
                    .interviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW)
                    .interviewerName("Senior Developer Mehmet")
                    .interviewScore(7.4)
                    .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-02-22"))
                    .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-02-18"))
                    .processSpecificData(null)
                    .process(recruitmentProcess)
                    .build();

            Interview caseStudyInterview = Interview.builder()
                    .candidateId(200L)
                    .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                    .interviewerName("Project Manager Selin")
                    .interviewScore(8.0)
                    .interviewScheduleTime(new SimpleDateFormat("yyyy-MM-dd").parse("2023-03-05"))
                    .createdAt(new SimpleDateFormat("yyyy-MM-dd").parse("2023-03-01"))
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

            initNode.put("givenCaseTitle","New Business Case Title");
            initNode.put("givenCaseContent", "15-08-2025");
            initNode.put("givenCaseDeadline","20-08-2025");
            initNode.put("caseStudyScore",6.3);
            root.set("initCaseStudy", initNode);

            solutionNode.put("givenCaseSolutionUrl", "mockCaseSolutionUrl.com");
            solutionNode.put("caseStudySolutionTitle", "Updated mock solution title");
            solutionNode.put("caseStudySolutionDescriptino", "updated case study solution description");
            root.set("solutionCaseStudy", solutionNode);

            evaluationNode.put("givenCaseSolutionEvaluation", "Updated Case Solution Evaluation");
            evaluationNode.put("givenCasePresentationEvaluation", "Updated Case Presentation Evaluation");
            evaluationNode.put("risksIdentified", "new risk identified");
            evaluationNode.put("candidateAnalyticThinkingNote", "updated candidate Analytic ThinkingNote");
            evaluationNode.put("caseStudyScore",6.9);

            root.set("evaluationCaseStudy", evaluationNode);



            caseStudyInterview.setProcessSpecificData(objectMapper.writeValueAsString(root));
            ArrayList<Interview> arrayListInterview = new ArrayList<>();
            arrayListInterview.add(hrInterview);
            arrayListInterview.add(techInterview);
            arrayListInterview.add(caseStudyInterview);

            recruitmentProcess.setInterviews(arrayListInterview);


            RecruitmentProcess savedProcess= recruitmentProcessRepository.save(recruitmentProcess);


            mockMvc.perform(post("/api/v1/recruitment/internal/changeProcessToFınalOverview/{candidateId}/{processId}",dto.getId(),savedProcess.getId()))
                    .andExpect(jsonPath("$.message").value("Candidate process successfully updated to Final Overview "))
                    .andExpect(jsonPath("$.status").value("200"));
        }

        @Test
        @Transactional
        public void shouldHandleProcessNotFound_whenUpdateTheCandidateRecruitmentProcess() throws Exception {

            mockMvc.perform(post("/api/v1/recruitment/internal/changeProcessToFınalOverview/{candidateId}/{processId}",3214L,324L))
                        .andExpect(jsonPath("$.success").value(false))
                        .andExpect(jsonPath("$.message").value("Process not found"))
                        .andExpect(jsonPath("$.status").value(409));
        }


}
