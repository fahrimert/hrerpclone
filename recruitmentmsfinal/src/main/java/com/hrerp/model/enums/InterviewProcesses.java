package com.hrerp.model.enums;

public enum InterviewProcesses {
    HR_SCREENING("HR_SCREENING"),
    TECHNICAL_INTERVIEW("TECHNICAL_INTERVIEW"),
    CASE_PROJECT("CASE_PROJECT"),
    FINAL_OVERVİEW("FINAL_OVERVİEW"),
    REJECTED("REJECTED");

    private String displayName;

    InterviewProcesses(String displayName) {
        this.displayName = displayName;
    }
}
