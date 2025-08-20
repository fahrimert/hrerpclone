package com.hrerp.repository;

import com.hrerp.model.RecruitmentProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentProcessRepository  extends JpaRepository<RecruitmentProcess,Long>
{

    Optional<RecruitmentProcess> findByCandidateIdAndJobPostingId(Long id, Long jobPostingId);

    Optional<RecruitmentProcess> findByCandidateIdAndId(Long candidateId,Long processId);

    List<RecruitmentProcess> findByCandidateId(Long candidateId);

    List<RecruitmentProcess> findByJobPostingId(Long jobPostingId);

    void deleteAllByCandidateId(long l);

    void deleteByCandidateId(long l);

    Optional<RecruitmentProcess> deleteByCandidateIdAndId(long l, long l1);
}
