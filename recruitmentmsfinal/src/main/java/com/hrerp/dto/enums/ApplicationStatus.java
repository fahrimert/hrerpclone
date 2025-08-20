package com.hrerp.dto.enums;

public enum ApplicationStatus {
    APPLIED("APPLIED"),
    INTERVIEW_SCHEDULED("INTERVIEW_SCHEDULED"),
    REJECTED("REJECTED");

    private final  String applicationStatusName;

    ApplicationStatus(String applicationStatusName) {
        this.applicationStatusName = applicationStatusName;
    }
}


