package com.hrerp.model.mapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos.*;
import com.hrerp.model.Interview;
import com.hrerp.model.RecruitmentProcess;
import com.hrerp.model.enums.InterviewProcesses;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class RecruitmentProcessMapper {

    private static ObjectMapper mapper ;
    private  InterviewMapper interviewMapper;
    public RecruitmentProcessMapper(ObjectMapper mapper, InterviewMapper interviewMapper) {
        this.mapper = mapper;
        this.interviewMapper = interviewMapper;
    }


    public RecruitmentProcess toInitiateRecruitmentProcess(@Valid RecruitmentProcessInitiateRequestDTO recruitmentProcessInitiateRequestDTO) {
        RecruitmentProcess recruitmentProcess =  RecruitmentProcess.builder()
                .candidateId(recruitmentProcessInitiateRequestDTO.getCandidateId())
                .jobPostingId(recruitmentProcessInitiateRequestDTO.getJobPostingId())
                .interviewProcesses(InterviewProcesses.HR_SCREENING)
                .lastUpdated(LocalDateTime.now())
                .createdAt(new Date())
                .build();
        List<Interview> interviewList = recruitmentProcessInitiateRequestDTO.getInterviews().stream().map(
                a -> {
                    Interview interview = null;
                    try {
                        interview = interviewMapper.toInitiateInterview(a);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    interview.setProcess(recruitmentProcess);
                    return interview;
                }).toList();
        recruitmentProcess.setInterviews(interviewList);
        return  recruitmentProcess;

    }
    public RecruitmentProcessInitiateResponseDTO fromRecruitmentProcess(RecruitmentProcess recruitmentProcess){
        return  new RecruitmentProcessInitiateResponseDTO(
                recruitmentProcess.getCandidateId(),
                recruitmentProcess.getJobPostingId(),
                recruitmentProcess.getInterviews().stream().map(a -> interviewMapper.fromInitiateInterview(a)).toList(),
                recruitmentProcess.getCreatedAt(),
                recruitmentProcess.getLastUpdated()
        );



    }


}
