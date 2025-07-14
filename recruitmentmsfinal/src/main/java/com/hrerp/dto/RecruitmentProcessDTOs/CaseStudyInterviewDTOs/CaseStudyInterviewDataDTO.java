package com.hrerp.dto.RecruitmentProcessDTOs.TechnicalInterviewDTOs;

import lombok.Data;

public record   TechnicalInterviewDataDTO (

        String codeExerciseUrl,
        Integer codeQualityScore,
        String technicalNotes,
        String candidateTechnicalBackgroundNote,
        TechnicalKnowledgeScore technicalKnowledgeScore

        ){


}
