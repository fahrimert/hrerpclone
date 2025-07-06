package com.hrerp.candidatems.repository;

import com.hrerp.candidatems.model.Candidate;
import com.hrerpclone.model.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

}
