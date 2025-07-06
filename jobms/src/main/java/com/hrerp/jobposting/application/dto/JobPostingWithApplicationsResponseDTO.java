package com.hrerp.jobposting.application.dto;

import com.hrerp.jobposting.application.dto.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPostingWithApplicationsResponseDTO {
    private  Long jobId;
    private  String jobTitle;
    private String City;
    private JobStatus jobStatus;
    private  Date jobPostingDate;
    private Date jobPostingDeadline;
    private  List<ApplicationListDTO> candidateList;


}
