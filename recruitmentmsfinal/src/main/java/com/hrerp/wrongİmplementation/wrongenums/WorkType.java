package com.hrerp.wrongİmplementation.wrongenums;

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
