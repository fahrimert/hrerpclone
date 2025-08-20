package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs;

public enum CaseStudyProcesses {
    INITIAL("INITIAL"),
    SOLUTION("SOLUTION"),
    EVALUATION("EVALUATION");

    private String displayName;

    CaseStudyProcesses(String displayName) {
        this.displayName = displayName;
    }
}
