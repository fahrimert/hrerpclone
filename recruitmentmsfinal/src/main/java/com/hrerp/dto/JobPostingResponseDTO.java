package com.hrerp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPostingResponseDTO {

    private  String jobTitle;
    private  String jobDescription;
    private  Integer salary;
    private com.hrerp.dto.enums.JobType jobType;
    private List<String> requiredSkillsList;
    private  String department;
    private com.hrerp.dto.enums.Location location;
    private com.hrerp.dto.enums.JobStatus jobStatus;
    private Date jobPostingDeadline;

}
