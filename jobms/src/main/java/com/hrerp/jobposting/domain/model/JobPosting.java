package com.hrerp.jobposting.domain.model;



import com.hrerp.jobposting.domain.model.enums.JobStatus;
import com.hrerp.jobposting.domain.model.enums.JobType;
import com.hrerp.jobposting.domain.model.enums.Location;
import com.hrerp.jobposting.domain.model.valueObjects.InternalJobPostingId;
import com.hrerp.jobposting.domain.model.valueObjects.JobDescription;
import com.hrerp.jobposting.domain.model.valueObjects.JobPostingId;
import com.hrerp.jobposting.domain.model.valueObjects.Salary;
import jakarta.persistence.*;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;



public class JobPosting {
    private JobPostingId id;
    @Embedded
    private InternalJobPostingId internalJobId;

    private String jobTitle;
//burdan devam
    private JobDescription jobPostingDescription;
    private  JobDescription internalDescripton;

    private Salary salary;

    private JobType jobType;

    private List<String> requiredSkillsList;

    private  String department;

    private  String hiringManagerName;

    private  Integer applicationCount = 0;
    private Location location;

    private JobStatus postingStatus;

    private  boolean isReplacement = false;
    private String replacementFor ;

    private  String internalHrNote;
    private Date jobPostingDeadline;
    private LocalDate internalPostingDate;
    private Date jobPostingDate;

    public JobPosting(JobPostingId id, InternalJobPostingId internalJobId, String jobTitle, JobDescription jobPostingDescription, JobDescription internalDescripton, Salary salary, JobType jobType, List<String> requiredSkillsList, String department, String hiringManagerName, Integer applicationCount, Location location, JobStatus postingStatus, boolean isReplacement, String replacementFor, String internalHrNote, Date jobPostingDeadline, LocalDate internalPostingDate, Date jobPostingDate) {
        Assert.notNull(jobTitle,"Job title must not be null");
        if (jobTitle == null || jobTitle .isEmpty()){
            throw  new IllegalArgumentException("job title must not be null");
        }
        this.id = id;
        this.internalJobId = internalJobId;
        this.jobTitle = jobTitle;
        this.jobPostingDescription = jobPostingDescription;
        this.internalDescripton = internalDescripton;
        if (salary.value() < 0 || salary == null){
            throw  new IllegalArgumentException("Salary cannot be negative and cannot be null");
        }
        this.salary = salary;
        if (jobType == null ){
            throw  new IllegalArgumentException("Job Type must not be null");
        }
        this.jobType = jobType;
        if (requiredSkillsList == null ){
            throw  new IllegalArgumentException("Required Skills List must not be null");
        }
        this.requiredSkillsList = requiredSkillsList;
        if (department == null ){
            throw  new IllegalArgumentException("department must not be null");
        }
        this.department = department;
        if (hiringManagerName == null ){
            throw  new IllegalArgumentException("Hiring Manager Name must not be null");
        }
        this.hiringManagerName = hiringManagerName;
        if (applicationCount == null || applicationCount < 0  ){
            throw  new IllegalArgumentException("Applicaation count must not be null and must not be negative");
        }
        this.applicationCount = applicationCount;
        if (location == null ){
            throw  new IllegalArgumentException("Location must not be null");
        }
        this.location = location;
        if (postingStatus == null ){
            throw  new IllegalArgumentException("Posting status must not be null");
        }
        this.postingStatus = JobStatus.DRAFT;

        this.isReplacement = isReplacement;
        this.replacementFor = replacementFor;
        this.internalHrNote = internalHrNote;
        if (jobPostingDeadline == null ){
            throw  new IllegalArgumentException("Job Posting Deadline must not be null");
        }
        this.jobPostingDeadline = jobPostingDeadline;
        this.internalPostingDate = internalPostingDate;
        this.jobPostingDate = jobPostingDate;
    }
    public  void  recruiterSpesificUpdate(
            InternalJobPostingId internalJobId,
            JobDescription internalDescripton,
            String hiringManagerName,
            boolean isReplacement,
            String replacementFor,
            String internalHrNote,
            LocalDate internalPostingDate

    ){
        this.internalJobId = internalJobId;
        this.internalDescripton = internalDescripton;
        if (hiringManagerName == null ){
            throw  new IllegalArgumentException("Hiring Manager Name must not be null");
        }
        this.hiringManagerName = hiringManagerName;
        if (applicationCount == null || applicationCount < 0  ){
            throw  new IllegalArgumentException("Applicaation count must not be null and must not be negative");
        }
        this.isReplacement = isReplacement;
        this.replacementFor = replacementFor;
        this.internalHrNote = internalHrNote;
        if (jobPostingDeadline == null ){
            throw  new IllegalArgumentException("Job Posting Deadline must not be null");
        }
        this.internalPostingDate = internalPostingDate;

    }

    public  void updateJobPosting(
              JobPostingId id,
     String jobTitle,
     JobDescription jobPostingDescription,
     Salary salary,
      JobType jobType,
     List<String> requiredSkillsList,
      String department,
      Integer applicationCount ,
     Location location,
     JobStatus postingStatus,
     Date jobPostingDeadline,
     Date jobPostingDate

    ){
        if (jobTitle == null || jobTitle .isEmpty()){
            throw  new IllegalArgumentException("job title must not be null");
        }
        this.id = id;
        this.jobTitle = jobTitle;
        this.jobPostingDescription = jobPostingDescription;
        if (salary.value() < 0 || salary == null){
            throw  new IllegalArgumentException("Salary cannot be negative and cannot be null");
        }
        this.salary = salary;
        if (jobType == null ){
            throw  new IllegalArgumentException("Job Type must not be null");
        }
        this.jobType = jobType;
        if (requiredSkillsList == null ){
            throw  new IllegalArgumentException("Required Skills List must not be null");
        }
        this.requiredSkillsList = requiredSkillsList;
        if (department == null ){
            throw  new IllegalArgumentException("department must not be null");
        }
        this.department = department;


        this.applicationCount = applicationCount;
        if (location == null ){
            throw  new IllegalArgumentException("Location must not be null");
        }
        this.location = location;
        if (postingStatus == null ){
            throw  new IllegalArgumentException("Posting status must not be null");
        }
        this.postingStatus = JobStatus.DRAFT;
        this.jobPostingDeadline = jobPostingDeadline;
        this.jobPostingDate = jobPostingDate;
    }

    public  void incrementApplications(){
        this.applicationCount = (this.applicationCount == null) ? 1 : this.applicationCount + 1;

    }


    public JobPostingId getId() {
        return id;
    }

    public InternalJobPostingId getInternalJobId() {
        return internalJobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public JobDescription getJobPostingDescription() {
        return jobPostingDescription;
    }

    public JobDescription getInternalDescripton() {
        return internalDescripton;
    }

    public Salary getSalary() {
        return salary;
    }

    public JobType getJobType() {
        return jobType;
    }

    public List<String> getRequiredSkillsList() {
        return requiredSkillsList;
    }

    public String getDepartment() {
        return department;
    }

    public String getHiringManagerName() {
        return hiringManagerName;
    }

    public Integer getApplicationCount() {
        return applicationCount;
    }

    public Location getLocation() {
        return location;
    }

    public JobStatus getPostingStatus() {
        return postingStatus;
    }

    public boolean isReplacement() {
        return isReplacement;
    }

    public String getReplacementFor() {
        return replacementFor;
    }

    public String getInternalHrNote() {
        return internalHrNote;
    }

    public Date getJobPostingDeadline() {
        return jobPostingDeadline;
    }

    public LocalDate getInternalPostingDate() {
        return internalPostingDate;
    }

    public Date getJobPostingDate() {
        return jobPostingDate;
    }
}
