package com.hrerp.jobposting.domain.model.subEntities;

import com.hrerp.jobposting.domain.model.enums.PipelineStage;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;



public class CandidatePipeline {

    @Enumerated(EnumType.STRING)
    private PipelineStage stage;
}
