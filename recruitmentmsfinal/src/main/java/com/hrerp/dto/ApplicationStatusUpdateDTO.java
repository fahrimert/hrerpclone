package com.hrerp.dto;

import com.hrerp.dto.enums.ApplicationStatus;
import lombok.Data;

@Data
public class ApplicationStatusUpdateDTO {
    private com.hrerp.dto.enums.ApplicationStatus applicationStatus;
}