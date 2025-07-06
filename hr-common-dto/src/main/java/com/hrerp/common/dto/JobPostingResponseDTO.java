package com.hrerpclone.common.dto;


import com.hrerpclone.common.dto.enums.JobStatus;
import com.hrerpclone.common.dto.enums.JobType;
import com.hrerpclone.common.dto.enums.Location;
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
