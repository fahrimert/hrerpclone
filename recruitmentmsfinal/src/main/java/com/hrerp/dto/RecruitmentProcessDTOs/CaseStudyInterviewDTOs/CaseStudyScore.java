package com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs;

public enum CaseStudyScore {

    BELOW_AVERAGE(
            "BELOW_AVERAGE",
            0.0,2.5
    ),
    AVERAGE(
            "AVERAGE",
            2.5,5.0
    ),

    ABOVE_AVERAGE(
            "ABOVE_AVERAGE",
            5.0,7.5
    ),
    EXCELLENT(
            "EXCELLENT",
            7.5,10.0
    );
    private String display_name;
    private  Double min_score;
    private  Double max_score;


    CaseStudyScore(String display_name, Double min_score, Double max_score) {
        this.display_name = display_name;
        this.min_score = min_score;
        this.max_score = max_score;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public Double getMin_score() {
        return min_score;
    }

    public Double getMax_score() {
        return max_score;
    }

    public  static CaseStudyScore fromScore(double score){
        for (CaseStudyScore value : values()){
            if (score >= value.min_score && score <= value.max_score){
                return  value;
            }
        }
        throw new IllegalArgumentException("Verilen case study puanı aralıkta değil: " + score);
    }
}
