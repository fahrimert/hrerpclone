package com.hrerp.jobposting.domain.model.valueObjects;

public record Salary(Integer value) {
    public Salary {
        if (value == null ){
            throw  new  NullPointerException("Salary is null");
        }
        if (value < 0  ){
            throw new IllegalArgumentException("Salary cannot be negative");
        }

    }
}
