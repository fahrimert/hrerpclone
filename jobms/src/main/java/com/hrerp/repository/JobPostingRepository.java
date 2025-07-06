package com.hrerpclone.repository;

import com.hrerpclone.model.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostingRepository  extends JpaRepository<JobPosting, Long> {

}
