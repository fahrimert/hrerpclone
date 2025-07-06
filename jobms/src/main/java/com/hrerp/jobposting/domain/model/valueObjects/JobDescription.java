package com.hrerp.jobposting.domain.model.valueObjects;

public record JobDescription(String value) {
    public JobDescription {
        if (value == null || value.trim().isEmpty()){
            throw  new  NullPointerException("Job Description is null");
        }
        if (value.length() > 4 || value.length() < 30){
            throw  new  IllegalArgumentException("Job Description Size  is not valid");
        }

    }
}
