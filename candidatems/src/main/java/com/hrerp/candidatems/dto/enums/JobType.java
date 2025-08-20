package com.hrerp.candidatems.dto.enums;

public enum JobType {
    FULL_TIME(
"FULL_TIME"
    ),
    PART_TIME(
            "PART_TIME"
    ),
    INTERNSHIP(
            "INTERNSHIP"
    ),
    ;


    private  final String JobTypeName;

    JobType(String jobTypeName) {
        JobTypeName = jobTypeName;
    }
}
