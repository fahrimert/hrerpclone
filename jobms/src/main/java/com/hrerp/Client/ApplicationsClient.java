package com.hrerpclone.Client;

import com.hrerpclone.dto.ApiResponse;
import com.hrerpclone.dto.ApplicationListDTO;
import com.hrerpclone.dto.ApplicationsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import  com.hrerpclone.dto.JobPostingWithApplicationsResponseDTO;

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
