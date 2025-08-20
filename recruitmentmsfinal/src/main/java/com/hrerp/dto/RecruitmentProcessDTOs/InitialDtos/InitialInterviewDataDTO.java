package com.hrerp.dto.RecruitmentProcessDTOs.InitialDtos;


public record InitialInterviewDataDTO(
        String generalImpression,
        String candidateTeamCompabilityNote,
        Boolean locatedInTheSameCity,
        Boolean candidateWorkInTheOffice,
        String candidateCareerGoals,
        String salaryExpectation,
        String availabilityToStart

) {
}
