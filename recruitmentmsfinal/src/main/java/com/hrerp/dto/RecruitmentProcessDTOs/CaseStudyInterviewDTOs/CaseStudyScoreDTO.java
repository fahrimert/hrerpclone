package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs;

import com.hrerp.model.enums.InterviewScore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CaseStudyScoreDTO {
    private  Double score ;

    public  CaseStudyScore  toEnum (){
        return CaseStudyScore.fromScore(score);
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}