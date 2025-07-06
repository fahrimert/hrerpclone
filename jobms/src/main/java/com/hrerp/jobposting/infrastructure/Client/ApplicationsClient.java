package com.hrerp.jobposting.infrastructure.Client;

import com.hrerp.jobposting.application.dto.ApiResponse;
import com.hrerp.jobposting.application.dto.ApplicationListDTO;
import com.hrerp.jobposting.application.dto.ApplicationsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
    @FeignClient(
            name = "candidatems",
            url = "http://localhost:8085"
    )
    public interface ApplicationsClient {
        @GetMapping("/api/v1/applications/{jobId}/getApplications")
        List<ApplicationListDTO> getApplications(@PathVariable Long jobId);

        @GetMapping("/api/v1/applications/{jobPostingId}/getApplication/{candidateId}")
        ApiResponse<ApplicationsDTO> getApplicationSingle(@PathVariable Long jobPostingId, @PathVariable Long candidateId);
    }
