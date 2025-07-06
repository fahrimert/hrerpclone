package com.hrerp.jobposting.application.dto;


import com.hrerp.jobposting.application.dto.enums.JobStatus;
import com.hrerp.jobposting.application.dto.enums.JobType;
import com.hrerp.jobposting.application.dto.enums.Location;
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
    private JobType jobType;
    private List<String> requiredSkillsList;
    private  String department;
    private Location location;
    private JobStatus jobStatus;
    private Date jobPostingDeadline;

}
