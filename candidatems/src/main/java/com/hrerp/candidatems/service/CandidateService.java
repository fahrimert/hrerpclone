package com.hrerp.candidatems.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrerp.candidatems.Client.RecruitmentClient;
import com.hrerp.candidatems.dto.ApiResponse;
import com.hrerp.candidatems.dto.CandidateRequestDTO;
import com.hrerp.candidatems.dto.CandidateResponseDTO;
import com.hrerp.candidatems.dto.CounterOfferCandidateDTO;
import com.hrerp.candidatems.mapper.CandidateMapper;
import com.hrerp.candidatems.model.Candidate;
import com.hrerp.candidatems.model.Connections;
import com.hrerp.candidatems.repository.CandidateRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Validated

public class CandidateService implements CandidateServiceImpl {

    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;
    private final RecruitmentClient recruitmentClient;
    @Autowired
    public CandidateService(CandidateRepository candidateRepository, CandidateMapper candidateMapper, RecruitmentClient recruitmentClient) {
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
        this.recruitmentClient = recruitmentClient;
    }


        public ResponseEntity<ApiResponse> findAllCandidates() {
        try {
            List<Candidate> candidateList = candidateRepository.findAll();
            if (candidateList.isEmpty()){
                return  ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(
                        "Database error" ,
                        List.of("No candidates in database"),
                        HttpStatus.CONFLICT
                ));
            }
            else {
                return  ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(

                        candidateRepository.findAll()
                                .stream()
                                .filter(Objects::nonNull)
                                .map(a -> {
                                    return    new CandidateResponseDTO(
                                            a.getId(),
                                            a.getFirstName(),
                                            a.getLastName(),
                                            a.getAddress(),
                                            a.getEmail(),
                                            Optional.ofNullable(a.getConnections()).map(Connections::getLinkedinUrl).orElse(null),
                                            a.getSkills(),
                                            Optional.ofNullable(a.getConnections()).map(Connections::getInstagramUrl).orElse(null),
                                            Optional.ofNullable(a.getConnections()).map(Connections::getFacebookUrl).orElse(null),
                                            Optional.ofNullable(a.getConnections()).map(Connections::getPhoneNumber).orElse(null),
                                            a.getCvUrl(),
                                            a.getCreatedAt()
                                    );
                                }).toList()
                ));



            }
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error(
                            "Server Error",
                            List.of( "Unexpected server error"  + e.getMessage()),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                   );

        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> createCandidate(CandidateRequestDTO candidateRequestDTO) {
        try{


        if (candidateRequestDTO.equals(new CandidateRequestDTO())){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.error(
                            "Null Values",
                            List.of( "Required Fields Are Null"),
                            HttpStatus.BAD_REQUEST
                    )
            );
        }
            if (candidateRepository.existsByEmail(candidateRequestDTO.getEmail())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ApiResponse.error(
                                "Email Duplication",
                                List.of( "Email Already Exists"),
                                HttpStatus.BAD_REQUEST
                        )
                );
            }


        Candidate candidate =candidateMapper.toCandidate(candidateRequestDTO);
        Candidate savedCandidate =  candidateRepository.save(candidate);


            return  ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(candidateMapper.fromCandidate(candidate)));
        }
        catch (Exception e) {
            return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error(
                            "Server Error" + e.getMessage(),
                            List.of( "Unexpected server error"),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
            );


    }}




    @Override
    public ResponseEntity<ApiResponse> findCandidateById(Long id) {
        try {
            Optional<Candidate> candidate = candidateRepository.findById(id);

            if (candidate.isEmpty()){
                return  ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(
                        "Database error" ,
                        List.of("Candidate Not Found"),
                        HttpStatus.CONFLICT
                ));
            }
            else{
                Candidate existingCandidate = candidate.get();
                return  ResponseEntity.ok(ApiResponse.success(candidateMapper.fromCandidate(existingCandidate)));

            }
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

    @Override
        public ResponseEntity<ApiResponse> updateCandidateById(Long id,CandidateRequestDTO updatedCandidate) {
        Optional<Candidate> candidate = candidateRepository.findById(id);


        CandidateResponseDTO candidateResponseDTO = new CandidateResponseDTO();
        if (candidate.isEmpty()){
            return  ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            "Error",
                            List.of("No Candidate Found"),
                            HttpStatus.CONFLICT
                    ));
        }
        Candidate existsCandidate = candidate.get();

        existsCandidate.setFirstName(updatedCandidate.getFirstName());
        existsCandidate.setAddress(updatedCandidate.getAddress());
        existsCandidate.setEmail(updatedCandidate.getEmail());
        existsCandidate.setConnections(
                Connections.builder()
                        .linkedinUrl(updatedCandidate.getLinkedin_url())
                        .instagramUrl(updatedCandidate.getInstagram_url())
                        .facebookUrl(updatedCandidate.getFacebook_url())
                        .phoneNumber(updatedCandidate.getPhoneNumber())
                        .build());
        existsCandidate.setCvUrl(updatedCandidate.getCvUrl());
        existsCandidate.setCreatedAt(updatedCandidate.getCreatedAt());


    return  ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(candidateMapper.fromCandidate(existsCandidate)));
    }

    @Override
    public  ResponseEntity<ApiResponse> deleteCandidate(Long id) {
        try{
        Optional<Candidate> candidateInduvual = candidateRepository.findById(id);
        if (candidateInduvual.isEmpty()){
            return  ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            "Error",
                            List.of("No Candidate Found"),
                            HttpStatus.CONFLICT
                    ));
        }
            candidateRepository.deleteById(id);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(
                            "Delete candidate succesfully applied"
                    ));
         } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Unexpected Server Error" + e.getMessage(),
                            null,
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }

    @Override
    public boolean candidateExistsById(Long id) {
        return  candidateRepository.existsById(id);
    }

    @Override
    public ResponseEntity<ApiResponse> getMyOffers(Long candidateId) {
            try {
                ResponseEntity<ApiResponse> offersList = recruitmentClient.getOffersByCandidateId(candidateId);

                return offersList;
            }catch (Exception e){
                  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error(
                                "Unexpected Server Error" + e.getMessage(),
                                null,
                                HttpStatus.INTERNAL_SERVER_ERROR
                        ));
            }
    }

    @Override
    public ResponseEntity<ApiResponse> getMyInduvualOffer(Long offerId) {
        try {
            ResponseEntity<ApiResponse> ınduvualOffer = recruitmentClient.getInduvualOfferByOfferId(offerId);
            return ınduvualOffer;
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Unexpected Server Error" + e.getMessage(),
                            null,
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> candidateMakeCounterOffer(CounterOfferCandidateDTO counterOfferCandidateDTO, Long offerId) {
        {
            try {
                ResponseEntity<ApiResponse> counterOfferResponse = recruitmentClient.candidateMakeCounterOffer(counterOfferCandidateDTO,offerId);

                return counterOfferResponse;

            }
            catch (FeignException e) {
                String feignBody = e.contentUTF8();
                String message = feignBody;

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(feignBody);
                    if (node.has("message")) {
                        message = node.get("message").asText();
                    }
                } catch (Exception parseEx) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(ApiResponse.error(
                                    parseEx.getMessage(),
                                    null,
                                    HttpStatus.CONFLICT
                            ));

                }
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                message,
                                null,
                                HttpStatus.CONFLICT
                        ));

            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(
                                "Feign Client Error: " + e.getMessage(),
                                null,
                                HttpStatus.CONFLICT
                        ));
            }

        }
    }


}
