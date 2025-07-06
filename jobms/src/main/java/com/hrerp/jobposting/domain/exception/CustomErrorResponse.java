package com.hrerp.jobposting.domain.exception;

import java.util.Map;

public record CustomErrorResponse(Map<String,String> errors) {
}
