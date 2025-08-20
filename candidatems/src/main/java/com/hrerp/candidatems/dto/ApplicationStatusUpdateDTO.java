package com.hrerp.candidatems.dto;

import com.hrerp.candidatems.model.ApplicationStatus;
import lombok.Data;

@Data
public class ApplicationStatusUpdateDTO {
    private ApplicationStatus applicationStatus;
}