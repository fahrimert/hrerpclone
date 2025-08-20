package com.hrerp.candidatems.controller.exception;

import java.util.Map;

public record CustomErrorResponse(Map<String,String> errors) {
}
