package com.hrerp.candidatems.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidatingErrorResponse {
    private int status;
    private Date timestamp;
    private String message;
    private String details;
}