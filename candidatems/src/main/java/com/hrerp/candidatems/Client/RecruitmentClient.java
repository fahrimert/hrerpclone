package com.hrerp.candidatems.Client;

import com.hrerp.candidatems.dto.ApiResponse;
import com.hrerp.candidatems.dto.CounterOfferCandidateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "recruitmenprocessms",
        url = "http://localhost:8086"
)


public interface RecruitmentClient {

    @GetMapping("/api/v1/recruitment/getOffers/{candidateId}")
    ResponseEntity<ApiResponse> getOffersByCandidateId(@PathVariable  Long candidateId);

    @GetMapping("/api/v1/recruitment/getOffer/{offerId}")
    ResponseEntity<ApiResponse> getInduvualOfferByOfferId(@PathVariable Long offerId);

    @PutMapping("/api/v1/recruitment/candidateMakeCounterOffer/{offerId}")
    ResponseEntity<ApiResponse> candidateMakeCounterOffer(@RequestBody CounterOfferCandidateDTO counterOfferCandidateDTO, @PathVariable Long offerId);

  }
