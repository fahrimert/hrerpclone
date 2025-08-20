package com.hrerp.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyInterviewRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.EvaluateCaseStudyDTO.EvaluationCaseStudyJsonDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.EvaluateCaseStudyDTO.EvaluationCaseStudyResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.InitiateCaseStudyDTO.InitCaseStudyJsonDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.InitiateCaseStudyDTO.InitiateCaseStudyResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.SolutionCaseStudyDTO.SolutionCaseStudyJsonDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.SolutionCaseStudyDTO.SolutionCaseStudyResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos.InitialInterviewDataDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos.InterviewInitiateRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos.InterviewInitiateResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.InterviewTechicalResponseDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.TechnicalInterviewDataDTO;
import com.hrerp.model.Interview;
import com.hrerp.model.RecruitmentProcess;
import com.hrerp.model.enums.InterviewProcesses;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;


@Service
public class InterviewMapper {
    private static ObjectMapper mapper ;
    public InterviewMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Interview toInitiateInterview(@Valid InterviewInitiateRequestDTO interviewInitiateRequestDTO) throws ParseException {
        String jsonData = toJsonInitial(
                new InitialInterviewDataDTO(

                        interviewInitiateRequestDTO.getGeneralImpression(),
                        interviewInitiateRequestDTO.getCandidateTeamCompabilityNote(),
                        interviewInitiateRequestDTO.getLocatedInTheSameCity(),
                        interviewInitiateRequestDTO.getCandidateCanWorkInTheOffice(),
                        interviewInitiateRequestDTO.getCandidateCareerGoals(),
                        interviewInitiateRequestDTO.getSalaryExpectation(),
                        interviewInitiateRequestDTO.getAvailabilityToStart()
                ));


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date schedulerDate = formatter.parse(interviewInitiateRequestDTO.getInterviewScheduleTime());
        return  Interview.builder()
                .candidateId(interviewInitiateRequestDTO.getCandidateId())
                .interviewRatingQuote(interviewInitiateRequestDTO.getInterviewRatingQuote())
                .interviewScore(interviewInitiateRequestDTO.getInterviewScore().getScore())
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .interviewQuestions(interviewInitiateRequestDTO.getInterviewQuestions())
                .interviewerName(interviewInitiateRequestDTO.getInterviewerName())
                .interviewScheduleTime(schedulerDate)
                .processSpecificData(jsonData)
                .interviewScheduleTime(schedulerDate)
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

    }
    public InterviewInitiateResponseDTO fromInitiateInterview(Interview interview){
        return  new InterviewInitiateResponseDTO(
                interview.getCandidateId(),
                interview.getInterviewRatingQuote(),
                interview.getInterviewQuestions(),
                interview.getInterviewerName(),
                interview.getInterviewScore(),
                interview.getInterviewScheduleTime(),
                interview.getCreatedAt(),
                interview.getLastUpdated()

        );
    }

    public    String toJsonInitial(InitialInterviewDataDTO dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("DTO to JSON conversion failed");
        }

    }

    public    String toJsonTechnical(TechnicalInterviewDataDTO dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("DTO to JSON conversion failed");
        }

    }


    public InterviewTechicalResponseDTO fromForwardToTechnical(Interview interview){
        return  new InterviewTechicalResponseDTO(
                interview.getCandidateId(),
                interview.getInterviewRatingQuote(),
                interview.getInterviewQuestions(),
                interview.getInterviewerName(),
                interview.getInterviewScore(),
                interview.getProcess().getInterviewProcesses(),
                interview.getInterviewScheduleTime(),
                interview.getCreatedAt(),
                interview.getLastUpdated()

        );
    }

    public Interview toforwardToTheCaseStudyInterviewProcess(@Valid CaseStudyInterviewRequestDTO caseStudyInterviewRequestDTO, RecruitmentProcess process) throws Exception {
        //schedule timeyi böyle yapınca datayı kaydetmiyor dbye
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date schedulerDate = formatter.parse(caseStudyInterviewRequestDTO.getInterviewScheduleTime());
        return  Interview.builder()
                .candidateId(caseStudyInterviewRequestDTO.getCandidateId())
                .interviewRatingQuote(null)
                .interviewQuestions(null)
                .interviewerName(caseStudyInterviewRequestDTO.getInterviewerName())
                .processSpecificData(null)
                .process(process)
                .interviewProcesses(InterviewProcesses.CASE_PROJECT)
                .interviewScore(caseStudyInterviewRequestDTO.getInterviewScore().getScore())
//                .processSpecificData(jsonDataCaseStudy)
                .interviewScheduleTime(schedulerDate)
                .createdAt(new Date())
                .lastUpdated(LocalDateTime.now())
                .build();

    }
    public CaseStudyResponseDTO fromForwardToCaseStudy(Interview interview){
        return  new CaseStudyResponseDTO(
                interview.getCandidateId(),
                interview.getInterviewerName(),
                interview.getInterviewScheduleTime(),
                interview.getCreatedAt(),
                interview.getLastUpdated()
        );
    }

    public InitiateCaseStudyResponseDTO fromInitateTheCaseStudyInterviewProcess(Interview interview) throws JsonProcessingException {
        System.out.println(interview.getProcessSpecificData());
        InitCaseStudyJsonDTO initCaseStudyData = null;
        try{

            if (interview.getProcessSpecificData() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                //burada parse etmemiz lazım jsonumuzu o yüzden object mapperi çağırdık

                JsonNode root = objectMapper.readTree(interview.getProcessSpecificData());
                //process spesifik datanın hepsi bir string olduğu için bunu jsona parse ediyoru
                //elimizde artık json yapısı var

                JsonNode initNode = root.get("initCaseStudy");
                //initcasestudyi aldık json tarafından

                if (initNode != null && !initNode.isNull()) {
                    initCaseStudyData = objectMapper.treeToValue(initNode, InitCaseStudyJsonDTO.class);
                    //initnode boş değilse ve varsa onu InitiateCaseStudyResponseDTO sınıfına çeviridk
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            initCaseStudyData = null;
        }
        return  new InitiateCaseStudyResponseDTO(
                interview.getCandidateId(),
                interview.getInterviewerName(),
                interview.getCaseStudyProcesses(),
                initCaseStudyData.getGivenCaseTitle(),
                initCaseStudyData.getCaseStudyScore().getScore(),
                initCaseStudyData.getGivenCaseContent(),
                initCaseStudyData.getGivenCaseDeadline(),
                interview.getInterviewScheduleTime(),
                interview.getCreatedAt(),
                interview.getLastUpdated()


        );
    }

    public SolutionCaseStudyResponseDTO fromSolveTheCaseStudyInterviewProcess(Interview interview) throws JsonProcessingException {
        SolutionCaseStudyJsonDTO solutionCaseStudyResponseDTO = null;

        ObjectMapper objectMapper = new ObjectMapper();
        //burada parse etmemiz lazım jsonumuzu o yüzden object mapperi çağırdık

        JsonNode root = objectMapper.readTree(interview.getProcessSpecificData());
        //process spesifik datanın hepsi bir string olduğu için bunu jsona parse ediyoru
        //elimizde artık json yapısı var

        JsonNode initNode = root.get("solutionCaseStudy");
        //initcasestudyi aldık json tarafından
        try{

            if (initNode != null && !initNode.isNull()) {
                solutionCaseStudyResponseDTO = objectMapper.treeToValue(initNode, SolutionCaseStudyJsonDTO.class);
                //initnode boş değilse ve varsa onu InitiateCaseStudyResponseDTO sınıfına çeviridk
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            solutionCaseStudyResponseDTO = null;
        }

        return  new SolutionCaseStudyResponseDTO(
                interview.getCandidateId(),
                interview.getInterviewerName(),
                solutionCaseStudyResponseDTO.getCaseStudySolutionTitle(),
                solutionCaseStudyResponseDTO.getCaseStudySolutionDescriptino(),
                Double.parseDouble(solutionCaseStudyResponseDTO.getCaseStudyScore()),
                interview.getInterviewScheduleTime(),
                interview.getCreatedAt(),
                interview.getLastUpdated()


        );
    }


    public EvaluationCaseStudyResponseDTO fromEvaluateTheCaseStudyInterviewProcess(Interview interview) throws JsonProcessingException {
        EvaluationCaseStudyJsonDTO evaluationCaseStudyResponseDTO = null;

        ObjectMapper objectMapper = new ObjectMapper();
        //burada parse etmemiz lazım jsonumuzu o yüzden object mapperi çağırdık

        JsonNode root = objectMapper.readTree(interview.getProcessSpecificData());
        //process spesifik datanın hepsi bir string olduğu için bunu jsona parse ediyoru
        //elimizde artık json yapısı var

        JsonNode initNode = root.get("evaluationCaseStudy");
        //initcasestudyi aldık json tarafından
        try{

            if (initNode != null && !initNode.isNull()) {
                evaluationCaseStudyResponseDTO = objectMapper.treeToValue(initNode, EvaluationCaseStudyJsonDTO.class);
                //initnode boş değilse ve varsa onu InitiateCaseStudyResponseDTO sınıfına çeviridk
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            evaluationCaseStudyResponseDTO = null;
        }

        return  new EvaluationCaseStudyResponseDTO(
                interview.getCandidateId(),
                interview.getInterviewerName(),
                interview.getProcess(),
                evaluationCaseStudyResponseDTO.getGivenCaseSolutionEvaluation(),
                evaluationCaseStudyResponseDTO.getGivenCasePresentationEvaluation(),
                evaluationCaseStudyResponseDTO.getRisksIdentified(),
                evaluationCaseStudyResponseDTO.getCandidateAnalyticThinkingNote(),
                evaluationCaseStudyResponseDTO.getCaseStudyScore().getScore(),

                interview.getInterviewScheduleTime(),
                interview.getCreatedAt(),
                interview.getLastUpdated()


        );
    }


}
