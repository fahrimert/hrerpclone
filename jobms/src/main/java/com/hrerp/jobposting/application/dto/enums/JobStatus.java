package com.hrerp.jobposting.application.dto.enums;

public enum JobStatus {
    DRAFT("DRAFT"),
    PENDING_APPROVAL("PENDING_APPROVAL"),
    LIVE("LIVE"),
    CLOSED("CLOSED");

    private final  String jobStatusName;

    JobStatus(String jobStatusName) {
        this.jobStatusName = jobStatusName;
    }
}

