package com.hrerp.jobposting.domain.model.enums;



public enum PipelineStage {
    SCREENING("SCREENING"), INTERVIEW("INTERVIEW"), OFFER("OFFER");

    private  final String pipelineStageName;

    PipelineStage(String pipelineStageName) {
        this.pipelineStageName = pipelineStageName;
    }
}
