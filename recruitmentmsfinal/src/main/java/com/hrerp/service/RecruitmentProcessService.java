package com.hrerp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hrerp.Client.CandidateClient;
import com.hrerp.Client.JobPostingClient;
import com.hrerp.dto.*;
import com.hrerp.dto.JobPostingDTOs.JobPostingRequestRecruiterSpesificDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyInterviewRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyProcesses;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyScore;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.EvaluateCaseStudyDTO.EvaluateCaseStudyInterviewDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.InitiateCaseStudyDTO.InitialCaseStudyInterviewDataDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.SolutionCaseStudyDTO.SolutionCaseStudyInterviewDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos.RecruitmentProcessInitiateRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos.RecruitmentProcessInitiateResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.InterviewTechicalResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.InterviewTechnicalInterviewRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.TechnicalInterviewDataDTO;
import com.hrerp.model.Interview;
import com.hrerp.model.RecruitmentProcess;
import com.hrerp.model.enums.InterviewProcesses;
import com.hrerp.model.mapper.InterviewMapper;
import com.hrerp.model.mapper.RecruitmentProcessMapper;
import com.hrerp.repository.InterviewRepository;
import com.hrerp.repository.RecruitmentProcessRepository;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
public class RecruitmentProcessService {
    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final RecruitmentProcessMapper recruitmentProcessMapper;
    private final CandidateClient candidateClient;
    private final JobPostingClient jobPostingClient;
    private final InterviewRepository interviewRepository;
    private  final  InterviewMapper interviewMapper;

    public RecruitmentProcessService(RecruitmentProcessRepository recruitmentProcessRepository, InterviewMapper interviewMapper ,RecruitmentProcessMapper recruitmentProcessMapper, CandidateClient client, CandidateClient candidateClient, JobPostingClient jobPostingClient, InterviewRepository interviewRepository) {
        this.recruitmentProcessRepository = recruitmentProcessRepository;
        this.interviewMapper = interviewMapper;

        this.recruitmentProcessMapper = recruitmentProcessMapper;
        this.candidateClient = candidateClient;
        this.jobPostingClient = jobPostingClient;
        this.interviewRepository = interviewRepository;
    }

    public  ResponseEntity<ApiResponse> recruiterSpesificUpdate (JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO, Long jobPostingId){
        try{
            return jobPostingClient.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO,jobPostingId);
        }
        catch (FeignException e) {
            return ResponseEntity.status(e.status())
                    .body(ApiResponse.error(
                            "Job Posting not found" ,
                            null,
                            HttpStatus.valueOf(e.status())
                    ));
        }
    }

    public  ResponseEntity<ApiResponse> recruiterSpesificFetch ( Long jobPostingId){
        try{
            return jobPostingClient.recruiterSpesificFetch(jobPostingId);
        }
        catch (FeignException e) {
            return ResponseEntity.status(e.status())
                    .body(ApiResponse.error(
                            "Job Posting not found" ,
                            null,
                            HttpStatus.valueOf(e.status())
                    ));
        }
    }


    public  ResponseEntity<ApiResponse<?>> getTheProperCandidates ( Long jobPostingId){
        try{
            return candidateClient.getTheProperCandidates(jobPostingId);
        }
        catch (FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(
                            "Candidate Or Application Not Found",
                            null,
                            HttpStatus.NOT_FOUND
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Internal server error: " + e.getMessage(),
                            null,
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }

    public  ResponseEntity<ApiResponse> updateTheCandidateApplicationStatus (Long jobPostingId, ApplicationStatusUpdateDTO applicationStatusUpdateDTO){
        try{
            return  candidateClient.updateTheCandidateApplicationStatus(jobPostingId,applicationStatusUpdateDTO);
        }
        catch (FeignException e) {
            return ResponseEntity.status(e.status())
                    .body(ApiResponse.error(
                            "Candidates not found" ,
                            null,
                            HttpStatus.valueOf(e.status())
                    ));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Server Error.",
                            null,
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }

    public ResponseEntity<ApiResponse> getTheProcessesBasedOnCandidate(Long candidateId) {
        try {
            if (candidateClient.candidateExistsById(candidateId) == false) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Candidate Does Not Exists",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }
            List<RecruitmentProcess> recruitmentProcessList = recruitmentProcessRepository.findByCandidateId(candidateId);
            if (recruitmentProcessList.isEmpty()  && recruitmentProcessList.size() == 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Candidate doesnt have any recruitment processes exist.",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }
            List<RecruitmentProcessInitiateResponseDTO> responseDTOS = recruitmentProcessList.stream().map(a -> recruitmentProcessMapper.fromRecruitmentProcess(a)).toList();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(
                            responseDTOS
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Server Error.",
                            null,
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }

    public ResponseEntity<ApiResponse> getTheInduvualRecruitmentProcesses(Long candidateId, Long processId) {
        try {
            Optional<RecruitmentProcess> recruitmentProcess = recruitmentProcessRepository.findByCandidateIdAndId(processId, candidateId);

            if (recruitmentProcess.isEmpty() && recruitmentProcess.isPresent() == false){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Process not found",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }


            if (candidateClient.candidateExistsById(candidateId) == false) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Candidate Does Not Exists",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }
            RecruitmentProcess presentProcess = recruitmentProcess.get();

            RecruitmentProcessInitiateResponseDTO responseDTO = recruitmentProcessMapper.fromRecruitmentProcess(presentProcess);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(
                            responseDTO
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Server Error.",
                            null,
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }

    }

    public  ResponseEntity<ApiResponse> rejectTheRecruitmentProcessOnCandidate(Long processId  ){
                Optional<RecruitmentProcess> recruitmentProcess = recruitmentProcessRepository.findById(processId);
                if (recruitmentProcess.isEmpty()  ){
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(ApiResponse.error(
                                    "Process not found",
                                    null,
                                    HttpStatus.CONFLICT
                            ));
                }

                    RecruitmentProcess existingRecruitmentProcess = recruitmentProcess.get();

                    if (existingRecruitmentProcess.getInterviews().isEmpty()   ){
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(ApiResponse.error(
                                        "Process status changed to rejected no interviews detecdet to delete",
                        null,
                                        HttpStatus.CONFLICT
                                ));

                    }
                    recruitmentProcessRepository.save(existingRecruitmentProcess);

                    interviewRepository.deleteAllByProcessId(processId);

                    existingRecruitmentProcess.setInterviewProcesses(InterviewProcesses.REJECTED);
                    existingRecruitmentProcess.getInterviews().clear();


                    return ResponseEntity.ok(ApiResponse.success("Process successfully rejected and interviews deleted"));
    }


    public ResponseEntity<ApiResponse> initiateRecruitmentProcess(RecruitmentProcessInitiateRequestDTO recruitmentProcessInitiateRequestDTO) {

        if (candidateClient.candidateExistsById(recruitmentProcessInitiateRequestDTO.getCandidateId()) == false) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            "Candidate Does Not Exists",
                            null,
                            HttpStatus.CONFLICT
                    ));
        }
        if (jobPostingClient.jobPostingExistsById(recruitmentProcessInitiateRequestDTO.getJobPostingId()) == false) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            "Job Posting Does Not Exists",
                            null,
                            HttpStatus.CONFLICT
                    ));
        }
        Optional<RecruitmentProcess> recruitmentProcess1 = recruitmentProcessRepository.findByCandidateIdAndJobPostingId(recruitmentProcessInitiateRequestDTO.getCandidateId(),recruitmentProcessInitiateRequestDTO.getJobPostingId());
        if (recruitmentProcess1.isPresent()) {
            RecruitmentProcess process = recruitmentProcess1.get();

            if (process.getInterviewProcesses() != InterviewProcesses.REJECTED) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Process Already Initiated with Candidate on this JobPosting",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }

            recruitmentProcessRepository.delete(process);
        }
        RecruitmentProcess recruitmentProcess = recruitmentProcessMapper.toInitiateRecruitmentProcess(recruitmentProcessInitiateRequestDTO);


        Interview interview = recruitmentProcess.getInterviews().get(0);

        recruitmentProcessRepository.save(recruitmentProcess);


        RecruitmentProcessInitiateResponseDTO recruitmentProcessCreateResponseDTO = recruitmentProcessMapper.fromRecruitmentProcess(recruitmentProcess);

        return ResponseEntity.ok(ApiResponse.success(recruitmentProcessCreateResponseDTO));


    }


    public ResponseEntity<ApiResponse> forwardToTheTechnicalInterviewProcess(Long processId, @Valid InterviewTechnicalInterviewRequestDTO interviewTechnicalInterviewRequestDTO) {

        try {

            Optional<RecruitmentProcess> recruitmentProcess = recruitmentProcessRepository.findByCandidateIdAndId( interviewTechnicalInterviewRequestDTO.getCandidateId(),processId);

            if (recruitmentProcess.isEmpty()){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Process does not exists.",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }



            RecruitmentProcess presentProcess = recruitmentProcess.get();

            if (presentProcess.getInterviewProcesses() == InterviewProcesses.TECHNICAL_INTERVIEW){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Process already on Technıcal Interview step.",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }



            presentProcess.setInterviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW);


            recruitmentProcessRepository.save(presentProcess);

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
            Interview interview = Interview.builder()
                    .candidateId(interviewTechnicalInterviewRequestDTO.getCandidateId())
                    .interviewRatingQuote(interviewTechnicalInterviewRequestDTO.getInterviewRatingQuote())
                    .processSpecificData(jsonData)
                    .process(presentProcess)
                    .interviewProcesses(InterviewProcesses.TECHNICAL_INTERVIEW)
                    .interviewScore(interviewTechnicalInterviewRequestDTO.getInterviewScore().getScore()        )
                    .interviewQuestions(interviewTechnicalInterviewRequestDTO.getInterviewQuestions())
                    .interviewScheduleTime(schedulerDate)
                    .interviewerName(interviewTechnicalInterviewRequestDTO.getInterviewerName())
                    .createdAt(new Date())
                    .lastUpdated(LocalDateTime.now()).build();

            interviewRepository.save(interview);

            InterviewTechicalResponseDTO interviewTechicalResponseDTO = interviewMapper.fromForwardToTechnical(interview);


            return ResponseEntity.ok(ApiResponse.success(interviewTechicalResponseDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Server Error.",
                            null,
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }


    public ResponseEntity<ApiResponse> forwardToTheCaseStudyInterviewProcess(Long processId, @Valid CaseStudyInterviewRequestDTO caseStudyInterviewRequestDTO) {

        try {
            Optional<RecruitmentProcess> recruitmentProcess = recruitmentProcessRepository.findByCandidateIdAndId( caseStudyInterviewRequestDTO.getCandidateId(),processId);

            if (recruitmentProcess.isEmpty() ){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Process does not exists.",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }

            RecruitmentProcess existingRecruitmentProcess = recruitmentProcess.get();

            if (existingRecruitmentProcess.getInterviewProcesses() == InterviewProcesses.CASE_PROJECT){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Process already on Case Study Interview step.",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }

            existingRecruitmentProcess.setInterviewProcesses(InterviewProcesses.CASE_PROJECT);

            recruitmentProcessRepository.save(existingRecruitmentProcess);


            Interview interview = interviewMapper.toforwardToTheCaseStudyInterviewProcess(caseStudyInterviewRequestDTO,existingRecruitmentProcess);


            CaseStudyResponseDTO caseStudyResponseDTO = interviewMapper.fromForwardToCaseStudy(interview);


           Interview  savedInterview =  interviewRepository.save(interview);


            return ResponseEntity.ok(ApiResponse.success(caseStudyResponseDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Server Error." ,
                            null,
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }

    public ResponseEntity<ApiResponse> initiateTheCaseStudyInterviewProcess(Long interviewId, @Valid InitialCaseStudyInterviewDataDTO initDto) {

        try {

            ObjectMapper objectMapper = new ObjectMapper();

            Optional<Interview> interview = interviewRepository.findById( interviewId);

            if (interview.isEmpty() ){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Interview does not exists.",
                                   null,
                                HttpStatus.CONFLICT
                        ));
            }

            Interview existingInterview = interview.get();
            ObjectNode root = null;



            if (existingInterview.getCaseStudyProcesses() == CaseStudyProcesses.INITIAL){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Interview already in case study ınitial progress .",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }


            if (existingInterview.getProcessSpecificData() != null && !existingInterview.getProcessSpecificData().isEmpty()){
                JsonNode parsedNode = objectMapper.readTree(existingInterview.getProcessSpecificData());
                if (!parsedNode.isObject()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ApiResponse.error("Invalid JSON structure in processSpecificData.", null, HttpStatus.INTERNAL_SERVER_ERROR));
                }
                root = (ObjectNode) parsedNode;             }
            else {
                root = new ObjectMapper().createObjectNode();
            }
            //read tree ile json verisini object nodeye çeviriyorz
            //eğer boşsa root değişkeni zaten boş kalır ve yeni json oluşur
            //eğer ağaçta veri yoksa olan veriyi al

            ObjectNode solutionNode = objectMapper.createObjectNode();
            //yeni node oluşturuyoruz bu solution nodesi olacak
            solutionNode.put("givenCaseTitle", initDto.getGivenCaseTitle());
            solutionNode.put("givenCaseContent", initDto.getGivenCaseContent());
            solutionNode.put("givenCaseDeadline", initDto.getGivenCaseDeadline());
            solutionNode.put("caseStudyScore",initDto.getCaseStudyScore().getScore());
            root.set("initCaseStudy", solutionNode);
            //roota bu nodeyi setliyoruz

            existingInterview.setProcessSpecificData(objectMapper.writeValueAsString(root));
            //artık üstüne eklenmiş oldu halihazırdaki json datasının
            existingInterview.setCaseStudyProcesses(CaseStudyProcesses.INITIAL);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date schedulerDate = formatter.parse(initDto.getInterviewScheduleTime());
            existingInterview.setInterviewScheduleTime(schedulerDate);


            interviewRepository.save(existingInterview);

            return ResponseEntity.ok(ApiResponse.success(interviewMapper.fromInitateTheCaseStudyInterviewProcess(existingInterview)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Server Error." ,
                            null,
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }


    public ResponseEntity<ApiResponse> solveTheCaseStudyInterview(Long interviewId,
                                                                  @Valid SolutionCaseStudyInterviewDTO solutionCaseStudyInterviewDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //json verilerini  java objelerine çevirmek için jackson kütüphane sınıfı
            //burada processSpecificData alanındaki json stringini çevirmek için kllanıyoruz
            Optional<Interview> interview = interviewRepository.findById( interviewId);

            if (interview.isEmpty()  ){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Interview does not exists.",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }

            Interview existingInterview = interview.get();

            if (existingInterview.getCaseStudyProcesses() == CaseStudyProcesses.SOLUTION){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Interview already in case study solution progress.",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }
            ObjectNode root = null;


            if (existingInterview.getProcessSpecificData() != null && !existingInterview.getProcessSpecificData().isEmpty()){
                JsonNode parsedNode = objectMapper.readTree(existingInterview.getProcessSpecificData());
                if (!parsedNode.isObject()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ApiResponse.error("Invalid JSON structure in processSpecificData.", null, HttpStatus.INTERNAL_SERVER_ERROR));
                }
                root = (ObjectNode ) objectMapper.readTree(existingInterview.getProcessSpecificData());          }
            else {
                root = new ObjectMapper().createObjectNode();
            }
            //başlangıç olarak boş bi node bu json ağacımız olacak


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date schedulerDate = formatter.parse(solutionCaseStudyInterviewDTO.getInterviewScheduleTime());
            //read tree ile json verisini object nodeye çeviriyorz
            //eğer boşsa root değişkeni zaten boş kalır ve yeni json oluşur
            //eğer ağaçta veri yoksa olan veriyi al

            ObjectNode solutionNode = objectMapper.createObjectNode();
            //yeni node oluşturuyoruz bu solution nodesi olacak
            solutionNode.put("caseStudySolutionTitle", solutionCaseStudyInterviewDTO.getCaseStudySolutionTitle());
            solutionNode.put("caseStudySolutionDescriptino", solutionCaseStudyInterviewDTO.getCaseStudySolutionDescriptino());
            solutionNode.put("caseStudyScore", solutionCaseStudyInterviewDTO.getCaseStudyScore().getScore());


            root.set("solutionCaseStudy", solutionNode);
            //roota bu nodeyi setliyoruz
            existingInterview.setInterviewerName(solutionCaseStudyInterviewDTO.getInterviewerName());
            existingInterview.setProcessSpecificData(objectMapper.writeValueAsString(root));
            existingInterview.setInterviewScheduleTime(schedulerDate);
            //artık üstüne eklenmiş oldu halihazırdaki json datasının
            existingInterview.setCaseStudyProcesses(CaseStudyProcesses.SOLUTION);



            interviewRepository.save(existingInterview);

            return ResponseEntity.ok(ApiResponse.success(interviewMapper.fromSolveTheCaseStudyInterviewProcess(existingInterview)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Server Error." + e.getMessage() ,
                            null,
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }


    public ResponseEntity<ApiResponse> evaluateTheCaseStudyInterview(Long interviewId,
                                                                  @Valid EvaluateCaseStudyInterviewDTO evaluateCaseStudyInterviewDTO) {
        try {



            ObjectMapper objectMapper = new ObjectMapper();
            //json verilerini  java objelerine çevirmek için jackson kütüphane sınıfı
            //burada processSpecificData alanındaki json stringini çevirmek için kllanıyoruz


            Optional<Interview> interview = interviewRepository.findById( interviewId);

            if (interview.isEmpty()  && interview.isPresent() == false){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Interview does not exists.",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }

            Interview existingInterview = interview.get();

            if (existingInterview.getCaseStudyProcesses() == CaseStudyProcesses.EVALUATION){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Interview already in case study evaluatıon progress.",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }
            ObjectNode root = null;
            //başlangıç olarak boş bi node bu json ağacımız olacak
            if (existingInterview.getProcessSpecificData() != null && !existingInterview.getProcessSpecificData().isEmpty()){
                JsonNode parsedNode = objectMapper.readTree(existingInterview.getProcessSpecificData());
                if (!parsedNode.isObject()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ApiResponse.error("Invalid JSON structure in processSpecificData.", null, HttpStatus.INTERNAL_SERVER_ERROR));
                }
                root = (ObjectNode ) objectMapper.readTree(existingInterview.getProcessSpecificData());          }
            else {
                root = new ObjectMapper().createObjectNode();
            }
            //read tree ile json verisini object nodeye çeviriyorz
            //eğer boşsa root değişkeni zaten boş kalır ve yeni json oluşur
            //eğer ağaçta veri yoksa olan veriyi al

            ObjectNode solutionNode = objectMapper.createObjectNode();
            //yeni node oluşturuyoruz bu solution nodesi olacak
            String givenCaseSolutionEvaluation;
            String givenCasePresentationEvaluation;
            String risksIdentified;
            String candidateAnalyticThinkingNote;
            CaseStudyScore caseStudyScore;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date schedulerDate = formatter.parse(evaluateCaseStudyInterviewDTO.getInterviewScheduleTime());
            solutionNode.put("givenCaseSolutionEvaluation", evaluateCaseStudyInterviewDTO.getGivenCaseSolutionEvaluation());
            solutionNode.put("givenCasePresentationEvaluation", evaluateCaseStudyInterviewDTO.getGivenCasePresentationEvaluation());
            solutionNode.put("risksIdentified", evaluateCaseStudyInterviewDTO.getRisksIdentified());
            solutionNode.put("candidateAnalyticThinkingNote", evaluateCaseStudyInterviewDTO.getCandidateAnalyticThinkingNote());
            solutionNode.put("caseStudyScore",evaluateCaseStudyInterviewDTO.getCaseStudyScore().getScore());

            root.set("evaluationCaseStudy", solutionNode);
            //roota bu nodeyi setliyoruz

            existingInterview.setProcessSpecificData(objectMapper.writeValueAsString(root));
            existingInterview.setInterviewScheduleTime(schedulerDate);
            existingInterview.setInterviewerName(evaluateCaseStudyInterviewDTO.getInterviewerName());
            //artık üstüne eklenmiş oldu halihazırdaki json datasının
            existingInterview.setCaseStudyProcesses(CaseStudyProcesses.EVALUATION);
            interviewRepository.save(existingInterview);

            return ResponseEntity.ok(ApiResponse.success(interviewMapper.fromEvaluateTheCaseStudyInterviewProcess(existingInterview)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Server Error." + e.getMessage(),
                            null,
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }


    public  Double extractEvaluationCaseStudyScoreFromProcessData(String jsonData){
        try {
            if (jsonData == null) return null;
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonData);
            JsonNode evaluationNode = root.get("evaluationCaseStudy");
            if (evaluationNode != null && !evaluationNode.isNull()) {
                JsonNode scoreNode = evaluationNode.get("caseStudyScore");
                if (scoreNode != null && !scoreNode.isNull()) {
                    return scoreNode.asDouble();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public  Double extractInitCaseStudyScoreFromProcessData(String jsonData){
        try {
            if (jsonData == null) return null;
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonData);
            JsonNode evaluationNode = root.get("initCaseStudy");
            if (evaluationNode != null && !evaluationNode.isNull()) {
                JsonNode scoreNode = evaluationNode.get("caseStudyScore");
                if (scoreNode != null && !scoreNode.isNull()) {
                    return scoreNode.asDouble();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<FinalOverviewCandidateDTO> getCandidateAveragesOnJobPosting(Long jobPostingId) {
        List<RecruitmentProcess> recruitmentProcessList = recruitmentProcessRepository.findByJobPostingId(jobPostingId);
        if (recruitmentProcessList.isEmpty()){
            return  Collections.emptyList();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        List<FinalOverviewCandidateDTO> finalOverviewCandidateDTOS = recruitmentProcessList.stream().map( a -> {
            FinalOverviewCandidateDTO finalOverviewCandidateDTO = new FinalOverviewCandidateDTO();
            ResponseEntity<ApiResponse> candidateResponseDTO =   candidateClient.getTheInduvualCandidate(a.getCandidateId());
            CandidateResponseDTO candidateData = null;

            Object data = candidateResponseDTO.getBody().getData();

            candidateData = new ObjectMapper().convertValue(data, CandidateResponseDTO.class);

            finalOverviewCandidateDTO.setCandidateResponseDTO(candidateData);
            finalOverviewCandidateDTO.setHR_SCREENING(a.getInterviews().stream().filter( b -> b.getInterviewProcesses().equals(InterviewProcesses.HR_SCREENING)).findFirst().get().getInterviewScore()) ;
           finalOverviewCandidateDTO.setCASE_STUDY( a.getInterviews().stream().filter( b -> b.getInterviewProcesses().equals(InterviewProcesses.CASE_PROJECT)).findFirst().get().getInterviewScore());
            finalOverviewCandidateDTO.setTECHNICAL( a.getInterviews().stream().filter( b -> b.getInterviewProcesses().equals(InterviewProcesses.TECHNICAL_INTERVIEW)).findFirst().get().getInterviewScore());
            finalOverviewCandidateDTO.setINIT_CASE_STUDY(

                    a.getInterviews().stream().map(Interview::getProcessSpecificData)
                            .map(this::extractInitCaseStudyScoreFromProcessData)
                            .filter(Objects::nonNull)
                            .findFirst()
                            .orElse(null));
                finalOverviewCandidateDTO.setEVALUATION_CASE_STUDY(
                        a.getInterviews().stream().map(Interview::getProcessSpecificData)
                                .map(this::extractEvaluationCaseStudyScoreFromProcessData)
                                .filter(Objects::nonNull)
                                .findFirst()
                                .orElse(null));
            finalOverviewCandidateDTO.setAVERAGE_SCORE(
                   (Stream.of(
                           finalOverviewCandidateDTO.getHR_SCREENING(),
                           finalOverviewCandidateDTO.getTECHNICAL(),
                           finalOverviewCandidateDTO.getCASE_STUDY(),
                           finalOverviewCandidateDTO.getEVALUATION_CASE_STUDY(),
                           finalOverviewCandidateDTO.getINIT_CASE_STUDY()

                   )
                           .filter(Objects::nonNull)
                           .mapToDouble(Double::doubleValue)
                           .average()
                           .orElse(Double.NaN)
            ));





return  finalOverviewCandidateDTO;


        }).toList();





        return finalOverviewCandidateDTOS;
    }
    public ResponseEntity<ApiResponse>   updateTheCandidateRecruitmentProcess(Long processId, Long candidateId){

        try{
            ResponseEntity<ApiResponse> candidateResponseDTO = candidateClient.getTheInduvualCandidate(candidateId);
            Optional<RecruitmentProcess> recruitmentProcess = recruitmentProcessRepository.findById(processId);

            if (recruitmentProcess.isEmpty() ){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Process not found",
                                null,
                                HttpStatus.CONFLICT
                        ));
            }

            RecruitmentProcess existingRecruitmentProcess = recruitmentProcess.get();

            existingRecruitmentProcess.setInterviewProcesses(InterviewProcesses.FINAL_OVERVİEW);

            recruitmentProcessRepository.save(existingRecruitmentProcess);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.successSpesific(
                            candidateResponseDTO.getBody().getData(),
                            "Candidate process successfully updated to Final Overview "
                    ));

        } catch (Exception e) {
            return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error(
                            "Server Error",
                            List.of( "Unexpected server error"),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
            );
        }

    }
}