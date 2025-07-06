package com.hrerp.candidatems.model;

import lombok.Builder;

import java.time.LocalDate;

public enum ApplicationStatus {
    APPLIED("APPLIED"),
    INTERVIEW_SCHEDULED("INTERVIEW_SCHEDULED"),
    REJECTED("REJECTED"),
    OFFER_MADE("OFFER_MADE");

    private final  String applicationStatusName;

    ApplicationStatus(String applicationStatusName) {
        this.applicationStatusName = applicationStatusName;
    }
}


