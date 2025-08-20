package com.hrerp.Client;

import com.hrerp.dto.ApiResponse;
import com.hrerp.dto.ApplicationStatusUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "candidatems",
        url = "http://localhost:8085"
)


public interface CandidateClient {

    @GetMapping("/api/v1/candidates/existsById/{id}")
    boolean candidateExistsById(@PathVariable  Long id);

    @GetMapping("/api/v1/candidates/{id}")
    ResponseEntity<ApiResponse> getTheInduvualCandidate(@PathVariable  Long id);


    @GetMapping("/api/v1/applications/{jobPostingId}/getTheProperCandidates")
    ResponseEntity<ApiResponse<?>> getTheProperCandidates(@PathVariable  Long jobPostingId);

    @PutMapping("/api/v1/applications/{candidateId}/updateTheCandidateApplicationStatus")
    ResponseEntity<ApiResponse> updateTheCandidateApplicationStatus(@PathVariable  Long candidateId, @RequestBody ApplicationStatusUpdateDTO applicationStatus);
}
