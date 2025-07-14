package com.hrerp.jobposting.application.controller.exception;

import java.util.Map;

public record CustomErrorResponse(Map<String,String> errors) {
}
