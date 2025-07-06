package com.hrerp.candidatems.repository;

import com.hrerp.candidatems.model.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository  extends JpaRepository<Applications,Long> {
    @Query(value = "SELECT * FROM applications  WHERE job_posting_id = :jobPostingId", nativeQuery = true)
    List<Applications> findAllByJobPostingId(@Param("jobPostingId") Long jobPostingId);

    @Query(value = "SELECT * FROM applications  WHERE job_posting_id = :jobPostingId and candidate_id = :candidateId", nativeQuery = true)
    Optional<Applications> findByIdAndJobPostingId(Long jobPostingId, Long candidateId);
}
