package com.hrerp.controller;

import com.hrerp.Client.CandidateClient;
import com.hrerp.Client.JobPostingClient;
import com.hrerp.dto.ApiResponse;
import com.hrerp.dto.ApplicationStatusUpdateDTO;
import com.hrerp.dto.CandidateResponseDTO;
import com.hrerp.dto.FinalOverviewCandidateDTO;
import com.hrerp.dto.JobPostingDTOs.JobPostingRequestRecruiterSpesificDTO;
import com.hrerp.dto.OfferDTOs.CounterOfferDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyInterviewRequestDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.EvaluateCaseStudyDTO.EvaluateCaseStudyInterviewDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.InitiateCaseStudyDTO.InitialCaseStudyInterviewDataDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.SolutionCaseStudyDTO.SolutionCaseStudyInterviewDTO;
import com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos.RecruitmentProcessInitiateRequestDTO;
import com.hrerp.model.mapper.Dto.OfferRequestDTO;
import com.hrerp.service.OfferService;
import com.hrerp.service.RecruitmentProcessService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitment")
public class RecruitmentController {
    private final  JobPostingClient jobPostingClient;
    private final  CandidateClient candidateClient;
    private final  RecruitmentProcessService recruitmentProcessService;
    private final  OfferService offerService;

    public RecruitmentController(
            CandidateClient candidateClient,
            JobPostingClient jobPostingClient,OfferService offerService,  RecruitmentProcessService recruitmentProcessService) {
                this.jobPostingClient = jobPostingClient;
                this.candidateClient = candidateClient;
                this.recruitmentProcessService = recruitmentProcessService;
                this.offerService = offerService;
    }


    @PutMapping("/{jobPostingId}/recruiterSpesificUpdate")
    public     ResponseEntity<ApiResponse>  updateRecruiterSpesificSectionsOnJobPosting(@RequestBody @Valid JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO, @PathVariable @Positive Long jobPostingId){
        return  recruitmentProcessService.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO,jobPostingId);
    }

    @GetMapping("/internal/{jobPostingId}")
    public   ResponseEntity<ApiResponse>  recruiterSpesificFetchJobPosting( @PathVariable @Positive Long jobPostingId){
        return  recruitmentProcessService.recruiterSpesificFetch(jobPostingId);
    }

    @GetMapping("/internal/getTheProperCandidates/{jobPostingId}")
    public   ResponseEntity<ApiResponse<?>> getTheProperCandidates( @PathVariable @Positive Long jobPostingId){
            return recruitmentProcessService.getTheProperCandidates(jobPostingId);

    }
    @PutMapping("/internal/updateTheCandidateApplicationStatus/{candidateId}")
    public   ResponseEntity<ApiResponse> updateTheCandidateApplicationStatus( @PathVariable @Positive Long candidateId,@RequestBody ApplicationStatusUpdateDTO applicationStatus ){
    return  recruitmentProcessService.updateTheCandidateApplicationStatus(candidateId,applicationStatus);
    }




    @GetMapping("/public/getTheRecruitmentProcesses/{candidateId}")
    public    ResponseEntity<ApiResponse> getTheRecruitmentProcesses(@PathVariable Long candidateId ){
        return recruitmentProcessService.getTheProcessesBasedOnCandidate(candidateId);
    }

        @GetMapping("/public/getTheInduvualRecruitmentProcess/{candidateId}/{processId}")
    public    ResponseEntity<ApiResponse> getTheInduvualRecruitmentProcesses(@PathVariable @Positive Long candidateId, @PathVariable @Positive Long processId){
        return recruitmentProcessService.getTheInduvualRecruitmentProcesses(candidateId,processId);
    }

    @PostMapping("/internal/initiateRecruitmentProcess")
    public    ResponseEntity<ApiResponse> initiateRecruitmentProcess( @Valid @RequestBody RecruitmentProcessInitiateRequestDTO recruitmentProcessInitiateRequestDTO ){
        return recruitmentProcessService.initiateRecruitmentProcess(recruitmentProcessInitiateRequestDTO);
    }

    @PostMapping("/internal/rejectRecruitmentProcess/{processId}")
    public    ResponseEntity<ApiResponse> rejectRecruitmentProcess( @PathVariable @Positive Long processId ){
        return recruitmentProcessService.rejectTheRecruitmentProcessOnCandidate(processId);
    }



    @PostMapping("/internal/{processId}/forwardToTheTechnicalInterviewProcess")
    public    ResponseEntity<ApiResponse> forwardToTheTechnicalInterviewProcess(
            @PathVariable Long processId ,
            @Valid @RequestBody com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs.InterviewTechnicalInterviewRequestDTO interviewTechnicalInterviewRequestDTO){
        return recruitmentProcessService.forwardToTheTechnicalInterviewProcess(processId,interviewTechnicalInterviewRequestDTO);
    }


    @PostMapping("/internal/{processId}/forwardToTheCaseStudyInterviewProcess")
    public    ResponseEntity<ApiResponse> forwardToTheCaseStudyInterviewProcess(
            @PathVariable @Positive Long processId ,
            @Valid @RequestBody CaseStudyInterviewRequestDTO caseStudyInterviewRequestDTO){
        return recruitmentProcessService.forwardToTheCaseStudyInterviewProcess(processId,caseStudyInterviewRequestDTO);
    }



    @PostMapping("/internal/{interviewId}/initiateTheCaseStudyInterview")
    public    ResponseEntity<ApiResponse> initiateTheCaseStudyInterview(
            @PathVariable Long interviewId ,
            @Valid @RequestBody InitialCaseStudyInterviewDataDTO caseStudyInterviewRequestDTO){
        return recruitmentProcessService.initiateTheCaseStudyInterviewProcess(interviewId,caseStudyInterviewRequestDTO);
    }

        @PostMapping("/public/{interviewId}/solveTheCaseStudyInterview")
        public    ResponseEntity<ApiResponse> solveTheCaseStudyInterview(
                @PathVariable Long interviewId ,
                @Valid @RequestPart("solutionCaseStudyInterviewDTO")  SolutionCaseStudyInterviewDTO solutionCaseStudyInterviewDTO){
            return recruitmentProcessService.solveTheCaseStudyInterview(interviewId,solutionCaseStudyInterviewDTO);
        }

        //bunu da responsesi ve herşeyiyle yapacaz
    @PostMapping("/internal/{interviewId}/evaluateTheCaseStudyInterview")
    public    ResponseEntity<ApiResponse> evaluateTheCaseStudyInterview(
            @PathVariable Long interviewId ,
            @Valid @RequestBody EvaluateCaseStudyInterviewDTO evaluateCaseStudyInterviewDTO){
        return recruitmentProcessService.evaluateTheCaseStudyInterview(interviewId,evaluateCaseStudyInterviewDTO);
    }



    //burada sıkıntı var buradan alıp devam edicem burya istek atınca sorun geldiği için offerlamak için almam gereken verileri alamıyorum o da final overview mezuları yani
    @GetMapping("/internal/{jobPostingId}/getCandidateAveragesOnJobPosting")
    public    List<FinalOverviewCandidateDTO> getTheFinalOverviewCandidatesOnJobPosting(@PathVariable @Positive Long jobPostingId ){
        return recruitmentProcessService.getCandidateAveragesOnJobPosting(jobPostingId);
    }


    @PostMapping("/internal/changeProcessToFınalOverview/{candidateId}/{processId}")
    public    ResponseEntity<ApiResponse> changeProcessToFinalOverview(@PathVariable Long candidateId, @PathVariable Long processId ){
        return recruitmentProcessService.updateTheCandidateRecruitmentProcess(processId,candidateId);
    }


    @PostMapping("/internal/createOfferOnSpesificCandidate")
    public    ResponseEntity<ApiResponse> createOfferOnCandidate(@RequestBody OfferRequestDTO offerRequestDTO){
        return offerService.createOfferToCandidate(offerRequestDTO);
    }

    @GetMapping("/getOffer/{offerId}")
    public    ResponseEntity<ApiResponse> getInduvualOffer(@PathVariable  Long offerId){
        return offerService.getInduvualOfferCandidate(offerId);
    }
    @GetMapping("/getOffers/{candidateId}")
    public    ResponseEntity<ApiResponse> getOffers(@PathVariable  Long candidateId){
        return offerService.getMyOffersCandidate(candidateId);
    }

    @GetMapping("/getInduvualOfferForInternal/{internalJobId}")
    public    ResponseEntity<ApiResponse> getInduvualOfferForInternal(@PathVariable  Long internalJobId){
        return offerService.getInduvualOfferForInternal(internalJobId);
    }

    @PutMapping("/candidateMakeCounterOffer/{offerId}")
    public    ResponseEntity<ApiResponse> candidateMakeCounterOffer(@RequestBody CounterOfferDTO counterOfferDTO, @PathVariable  Long offerId){
        return offerService.candidateMakeCounterOffer(counterOfferDTO,offerId);
    }

    @PutMapping("/internalMakeCounterOffer/{offerId}")
    public    ResponseEntity<ApiResponse> getInduvualOfferForInternal(@RequestBody CounterOfferDTO counterOfferDTO, @PathVariable  Long offerId){
        return offerService.internalMakeCounterOffer(counterOfferDTO,offerId);
    }





}
