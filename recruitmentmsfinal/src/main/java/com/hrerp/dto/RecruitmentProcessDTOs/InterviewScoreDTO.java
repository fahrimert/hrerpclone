package com.hrerp.dto.RecruitmentProcessDTOs;

import com.hrerp.model.enums.InterviewScore;
import lombok.Data;

@Data
public class        InterviewScoreDTO {
    private  Double score ;

    public  InterviewScore  toEnum (){
        return InterviewScore.fromScore(score);
    }
}