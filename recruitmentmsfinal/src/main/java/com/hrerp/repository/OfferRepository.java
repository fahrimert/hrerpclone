package com.hrerp.repository;

import com.hrerp.model.Offer;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Long> {
    boolean existsByCandidateIdAndJobPostingId(@NotNull(message = "Candidate Id Cannot Be Null ") Long candidateId, Long jobPostingId);

    @Query(value = "SELECT * FROM offer WHERE candidate_id = :candidateId", nativeQuery = true)
    Optional<List<Offer>> findAllByCandidateId(Long candidateId);

    @Query(value = "SELECT * FROM offer WHERE internal_job_id = :internalJobId", nativeQuery = true)
    Optional<List<Offer>> findAllByInternalJobId(Long internalJobId);
}
