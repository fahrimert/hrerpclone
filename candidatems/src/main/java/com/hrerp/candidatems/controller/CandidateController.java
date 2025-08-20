package com.hrerp.candidatems.controller;

import com.hrerp.candidatems.dto.ApiResponse;
import com.hrerp.candidatems.dto.CandidateRequestDTO;
import com.hrerp.candidatems.dto.CounterOfferCandidateDTO;
import com.hrerp.candidatems.service.CandidateService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/candidates")
public class CandidateController {
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {

        this.candidateService = candidateService;
    }


    @GetMapping
    public ResponseEntity<ApiResponse> getAllCandidates(){
        return  candidateService.findAllCandidates();
    }

    @GetMapping("/{id}")
    public  ResponseEntity<ApiResponse> getCandidateById(@PathVariable Long id){
        return  candidateService.findCandidateById(id);
    }



        @GetMapping("/existsById/{id}")
    public  boolean candidateExistsById(@PathVariable Long id){
        return  candidateService.candidateExistsById(id);
    }



    //bunun integrasyonundan devam edicem
    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponse> createCandidate(
            @RequestBody @Valid CandidateRequestDTO candidateRequestDTO
    ){
        return  candidateService.createCandidate(candidateRequestDTO);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<ApiResponse> updateCandidate(@PathVariable Long id, @RequestBody  CandidateRequestDTO candidateRequestDTO){
        return  candidateService.updateCandidateById(id,candidateRequestDTO);
    }
//bunu api responseye çevirdim return tipini
    @DeleteMapping("/{id}")
    @Transactional
    public   ResponseEntity<ApiResponse> deleteCandidate(@PathVariable Long id){
        return  candidateService.deleteCandidate(id);
    }

    //buradan sonrasının controller unit testi oluyor mu bilmiyorum şahsen.
    @GetMapping("/{candidateId}/getMyOffers")
    public  ResponseEntity<ApiResponse> getMyOffers(@PathVariable Long candidateId){
        return  candidateService.getMyOffers(candidateId);
    }

    @GetMapping("/{offerId}/getInduvualOffer")
    public  ResponseEntity<ApiResponse> getMyInduvualOffer(@PathVariable Long offerId){
        return  candidateService.getMyInduvualOffer(offerId);
    }

    @PutMapping("/candidateMakeCounterOffer/{offerId}")
    public  ResponseEntity<ApiResponse> candidateMakeCounterOffer(@RequestBody CounterOfferCandidateDTO counterOfferDTO , @PathVariable Long offerId){
        return  candidateService.candidateMakeCounterOffer(counterOfferDTO,offerId);
    }

}
