package com.hrerp.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hrerp.model.enums.InterviewProcesses;
import com.hrerp.model.enums.InterviewScore;
import com.hrerp.model.enums.InterviewTypes;
import com.hrerp.model.enums.InterviewResult;
import jakarta.persistence.*;
import lombok.*;
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

    private  Long candidateId;


    //internal rating quote
    private  String interviewRatingQuote;

    @Enumerated
    private InterviewProcesses interviewProcesses;

    @Enumerated(EnumType.STRING)
    private InterviewResult interviewResult;

    private  List<String> interviewQuestions;

    @Enumerated
    private InterviewTypes interviewTypes;

    //sonradan çalışan entitysi altında olacak bu
    private  String interviewerName;


    @Enumerated
    private InterviewScore interviewScore;



    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "process_id"
    )
    @JsonBackReference
    private RecruitmentProcess process;

    private  Date interviewScheduleTime;
    @CreatedDate
    private Date createdAt ;
    private LocalDateTime lastUpdated;




}
