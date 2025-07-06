package com.hrerp.jobposting.domain.model.enums;

public enum WorkType {
    OFFICE(
            "OFFICE"
    ),
    REMOTE(
            "REMOTE"
    ),
    HYBRID(
      "HYBRID"
    );

    private  final String workTypeName;


    WorkType(String workTypeName) {
        this.workTypeName = workTypeName;
    }
}
