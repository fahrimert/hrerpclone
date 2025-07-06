package com.hrerpclone.model.subEntities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CandidatePipeline {

    @Enumerated(EnumType.STRING)
    private  PipelineStage stage;
}
