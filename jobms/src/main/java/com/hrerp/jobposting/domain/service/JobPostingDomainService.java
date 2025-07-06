package com.hrerp.jobposting.domain.service;

import com.hrerp.jobposting.domain.model.JobPosting;
import com.hrerp.jobposting.domain.model.valueObjects.*;
import com.hrerp.jobposting.domain.model.enums.JobStatus;
import com.hrerp.jobposting.domain.model.enums.JobType;
import com.hrerp.jobposting.domain.model.enums.Location;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class JobPostingDomainService {

    public JobPosting createJobPosting(
            JobPostingId id,
            InternalJobPostingId internalId,
            String title,
            JobDescription description,
            JobDescription internalDescription,
            Salary salary,
            JobType type,
            List<String> requiredSkills,
            String department,
            String hiringManager,
            Integer applicationCount,
            Location location,
            JobStatus status,
            boolean isReplacement,
            String replacementFor,
            String hrNote,
            Date deadline,
            LocalDate internalDate,
            Date postingDate
    ) {
        return new JobPosting(
                id,
                internalId,
                title,
                description,
                internalDescription,
                salary,
                type,
                requiredSkills,
                department,
                hiringManager,
                applicationCount,
                location,
                status,
                isReplacement,
                replacementFor,
                hrNote,
                deadline,
                internalDate,
                postingDate
        );
    }

    public void updateJobPosting(JobPosting jobPosting, /* DTO'dan gelen değerler */
                                 JobPostingId id,
                                 String title,
                                 JobDescription description,
                                 Salary salary,
                                 JobType type,
                                 List<String> requiredSkills,
                                 String department,
                                 Integer applicationCount,
                                 Location location,
                                 JobStatus status,
                                 Date deadline,
                                 Date postingDate
    ) {
        jobPosting.updateJobPosting(
                id,
                title,
                description,
                salary,
                type,
                requiredSkills,
                department,
                applicationCount,
                location,
                status,
                deadline,
                postingDate
        );
    }

    public void recruiterSpecificUpdate(
            JobPosting jobPosting,
            InternalJobPostingId internalId,
            JobDescription internalDescription,
            String hiringManagerName,
            boolean isReplacement,
            String replacementFor,
            String internalHrNote,
            LocalDate internalPostingDate
    ) {
        jobPosting.recruiterSpesificUpdate(
                internalId,
                internalDescription,
                hiringManagerName,
                isReplacement,
                replacementFor,
                internalHrNote,
                internalPostingDate
        );
    }

}
