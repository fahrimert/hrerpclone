package com.hrerp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrerp.Client.CandidateClient;
import com.hrerp.dto.ApiResponse;
import com.hrerp.dto.CandidateResponseDTO;
import com.hrerp.dto.FinalOverviewCandidateDTO;
import com.hrerp.dto.OfferDTOs.CounterOfferDTO;
import com.hrerp.dto.OfferDTOs.CounterOfferResponseDTO;
import com.hrerp.dto.OfferDTOs.OfferResponseWhenGetOfferDTO;
import com.hrerp.model.Offer;
import com.hrerp.model.mapper.Dto.OfferRequestDTO;
import com.hrerp.model.mapper.Dto.OfferResponseDTO;
import com.hrerp.model.mapper.OfferMapper;
import com.hrerp.repository.OfferRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OfferService {
    private  final OfferRepository offerRepository;
    private final  OfferMapper offerMapper;
    private  final  RecruitmentProcessService recruitmentProcessService;
    private  final CandidateClient candidateClient;

    public OfferService(OfferRepository offerRepository, OfferMapper offerMapper, RecruitmentProcessService recruitmentProcessService, CandidateClient candidateClient) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.recruitmentProcessService = recruitmentProcessService;
        this.candidateClient = candidateClient;
    }



    public ResponseEntity<ApiResponse> createOfferToCandidate(OfferRequestDTO offerRequestDTO ) {
   try {
        Offer savedOffer = offerMapper.toMakeAnOffer(offerRequestDTO);

        if (offerRepository.existsByCandidateIdAndJobPostingId(offerRequestDTO.getCandidateId(),offerRequestDTO.getJobPostingId())){
            return        ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            "Candidate Offer Process Initialized Already On This JobPos" ,
                            null,
                            HttpStatus.CONFLICT
                    ));
        }
        Offer savedOfferToRepo =  offerRepository.save(savedOffer);


        ResponseEntity<ApiResponse> candidateResponseDTO = candidateClient.getTheInduvualCandidate(savedOfferToRepo.getCandidateId());


        ObjectMapper objectMapper = new ObjectMapper();
        Object responseBody =  candidateResponseDTO.getBody().getData();


       CandidateResponseDTO dto = objectMapper.convertValue(responseBody, CandidateResponseDTO.class);

       List<FinalOverviewCandidateDTO> finalOverviewCandidateDTOS = recruitmentProcessService.getCandidateAveragesOnJobPosting(offerRequestDTO.getJobPostingId());

     Boolean candidateInFinalOverview =  finalOverviewCandidateDTOS.stream().map(a -> a.getCandidateResponseDTO()).anyMatch(c ->c.getId().equals(dto.getId()));

     if (candidateInFinalOverview) {
        return     ResponseEntity.ok(ApiResponse.success(offerMapper.fromOfferWhenCreateOffer(savedOffer,dto)));
        }

else {
     return        ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            "Cant offer to candidate because candidate does not pass the final overview step" ,
                            null,
                            HttpStatus.CONFLICT
                    ));
        }
    }
    catch (Exception e) {
     return    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error(
                        "Server Error",
                        List.of( "Unexpected server error" + e.getMessage()),
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
        );
    }
}


    public ResponseEntity<ApiResponse> getInduvualOfferCandidate(Long offerId) {
        try {
            Optional<Offer> offer = offerRepository.findById(offerId);
            if (offer.isEmpty()){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Requested Offer  not found" ,
                                null,
                                HttpStatus.CONFLICT
                        ));
            }
            Offer existsOffer = offer.get();
            return  ResponseEntity.ok(ApiResponse.success(offerMapper.fromOfferWhenGetOffer(existsOffer)));
        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(       ApiResponse.error(
                            "Server Error",
                            List.of( "Unexpected server error"  + e.getMessage()),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }

    public ResponseEntity<ApiResponse> getMyOffersCandidate(Long candidateId) {
        try {

            Optional<List<Offer>> offerList = offerRepository.findAllByCandidateId(candidateId);
            if (offerList.isEmpty()){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Requested Offer  not found" ,
                                null,
                                HttpStatus.CONFLICT
                        ));
            }
            List<OfferResponseWhenGetOfferDTO> offerResponseForCandidateDTOS = offerList.get().stream().map(offerMapper::fromOfferWhenGetOffer).toList();

            return    ResponseEntity.ok(ApiResponse.success(offerResponseForCandidateDTOS));


        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(       ApiResponse.error(
                            "Server Error",
                            List.of( "Unexpected server error"  + e.getMessage()),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }


    public  ResponseEntity<ApiResponse> getInduvualOfferForInternal(Long internalJobId){
        try {
            Optional<List<Offer>> induvualOffer = offerRepository.findAllByInternalJobId(internalJobId);
            if (induvualOffer.isEmpty()){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Requested Offer not found" ,
                                null,
                                HttpStatus.CONFLICT
                        ));
            }

            List<OfferResponseWhenGetOfferDTO> offerGet = induvualOffer.get().stream().map(offerMapper::fromOfferWhenGetOffer).toList();

            return  ResponseEntity.ok(ApiResponse.success(offerGet));
        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(       ApiResponse.error(
                            "Server Error",
                            List.of( "Unexpected server error"  + e.getMessage()),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }


    public  ResponseEntity<ApiResponse> candidateMakeCounterOffer(CounterOfferDTO counterOfferDTO , Long offerId){
        try{
            Optional<Offer> offer = offerRepository.findById(offerId);
            if (offer.isEmpty()){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Requested Offer not found" ,
                                null,
                                HttpStatus.CONFLICT
                        ));
            }
            Offer existsOffer = offer.get();
            if (existsOffer.getCounterOfferDemandsInternal() == null && existsOffer.getCounterOfferSalaryInternal() == null){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Counter offer already sent" ,
                                null,
                                HttpStatus.CONFLICT
                        ));
            }
            existsOffer.setOfferStatus(counterOfferDTO.getOfferStatus());
            existsOffer.setCounterOfferSalaryCandidate(counterOfferDTO.getCounterOfferSalary());
            existsOffer.setCounterOfferDemandsCandidate(counterOfferDTO.getCounterOfferDemands());

            offerRepository.save(existsOffer);
            CounterOfferResponseDTO counterOfferResponseDTO
                    = new CounterOfferResponseDTO(existsOffer.getOfferStatus(),
                    existsOffer.getProposedSalary(),
                    existsOffer.getCounterOfferSalaryCandidate(),
                    existsOffer.getCounterOfferDemandsCandidate(),
                    counterOfferDTO.getRole(),
                    existsOffer.getOfferExpiryDate(),
                    existsOffer.getCandidateStartDate()
                    );

            return  ResponseEntity.ok(ApiResponse.success(counterOfferResponseDTO));


        }    catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(       ApiResponse.error(
                            "Server Error",
                            List.of( "Unexpected server error"  + e.getMessage()),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }

    public  ResponseEntity<ApiResponse> internalMakeCounterOffer(CounterOfferDTO counterOfferDTO ,Long offerId){
        try{
            Optional<Offer> offer = offerRepository.findById(offerId);
            if (offer.isEmpty()){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Requested Offer not found" ,
                                null,
                                HttpStatus.CONFLICT
                        ));
            }
            Offer existsOffer = offer.get();

//            if (existsOffer.getCounterOfferSalaryCandidate() == null  ||  existsOffer.getCounterOfferDemandsCandidate() == null  ){
//                return ResponseEntity.status(HttpStatus.CONFLICT)
//                        .body(ApiResponse.error(
//                                "Candidate dont have counter offer yet." ,
//                                null,
//                                HttpStatus.CONFLICT
//                        ));
//            }
            existsOffer.setOfferStatus(counterOfferDTO.getOfferStatus());
            existsOffer.setCounterOfferSalaryInternal(counterOfferDTO.getCounterOfferSalary());
            existsOffer.setCounterOfferDemandsInternal(counterOfferDTO.getCounterOfferDemands());
            offerRepository.save(existsOffer);

            CounterOfferResponseDTO counterOfferResponseDTO
                    = new CounterOfferResponseDTO(existsOffer.getOfferStatus(),
                    existsOffer.getProposedSalary(),
                    existsOffer.getCounterOfferSalaryInternal(),
                    existsOffer.getCounterOfferDemandsInternal(),
                    counterOfferDTO.getRole(),
                    existsOffer.getOfferExpiryDate(),
                    existsOffer.getCandidateStartDate()
            );

            return  ResponseEntity.ok(ApiResponse.success(counterOfferResponseDTO));


        }    catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(       ApiResponse.error(
                            "Server Error",
                            List.of( "Unexpected server error"  + e.getMessage()),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }


}
