package com.hrerp.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrerp.dto.RecruitmentProcessDTOs.CaseStudyInterviewDTOs.CaseStudyProcesses;
import com.hrerp.model.enums.InterviewProcesses;
import com.hrerp.model.enums.InterviewQuestions;
import com.hrerp.model.enums.InterviewScore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;
import  java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @NotNull
    @Column(name = "candidateId")
    private  Long candidateId;

    @Nullable
    private  String interviewRatingQuote;

    @Enumerated(EnumType.STRING)
    private InterviewProcesses interviewProcesses;

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    private  List<InterviewQuestions> interviewQuestions;

    //sonradan çalışan entitysi altında olacak bu
    private  String interviewerName;

    @Column(columnDefinition = "jsonb",nullable = true)
    @JdbcTypeCode(SqlTypes.JSON)

    @Nullable
    private String processSpecificData;

    private CaseStudyProcesses caseStudyProcesses;
    private Double interviewScore;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "process_id"
    )
    @JsonIgnore
    private RecruitmentProcess process;

    private  Date interviewScheduleTime;
    @CreatedDate
    private Date createdAt ;
    private LocalDateTime lastUpdated;




}
