package com.hrerp.candidatems.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandardResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private Integer status;

    public  StandardResponse(boolean success, String message, T data, List<String> errors, Integer status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.status = status;
    }


}