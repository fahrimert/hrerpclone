package com.hrerpclone.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPostingRequestDTO {
    private  Long id;

    @NotNull(message = "Job Title should be present")
    @NotEmpty(message = "Job Title  should be present")
    @NotBlank(message = "Job Title should be present")

    @Size(min = 4,max = 30,message = "Title must be between 4 and 30 characters")
    private  String jobTitle;

    @NotNull(message = "Job Description should be present")
    @NotEmpty(message = "Job Description  should be present")
    @NotBlank(message = "Job Description should be present")

    @Size(min = 6,max = 50, message = "Job Description must be between 6 and 50 characterss")
    private  String jobDescription;


    @NotNull(message = "Salary should be present")
    @Positive
    private  Integer salary;

    @NotNull(message = "Job Type should be present")
    private     com.hrerpclone.common.dto.enums.JobType jobType;
    @NotNull(message = "Required Skill List should be present")
    private List<String> requiredSkillsList;
    @NotNull(message = "Department should be present")
    private  String department;
    @NotNull(message = "Location should be present")
    private     com.hrerpclone.common.dto.enums.Location location;
    @NotNull(message = "Job Posting Status should be present")
    private     com.hrerpclone.common.dto.enums.JobStatus jobPostingStatus;
    @NotNull(message = "Job Posting Deadline should be present")
    private Date jobPostingDeadline;
}
