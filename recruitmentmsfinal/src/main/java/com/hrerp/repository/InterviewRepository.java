package com.hrerp.repository;

import com.hrerp.model.Interview;
import com.hrerp.model.RecruitmentProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository  extends JpaRepository<Interview,Long> {

    void deleteAllByProcessId(@Param("processId") Long processId);
}
