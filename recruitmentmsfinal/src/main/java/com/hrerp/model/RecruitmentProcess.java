package com.hrerp.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hrerp.model.enums.InterviewProcesses;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RecruitmentProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private  Long candidateId;
    private  Long jobPostingId;


    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Interview> interviews;

    @Enumerated(EnumType.STRING)
    private InterviewProcesses interviewProcesses;
    @CreatedDate
    private Date createdAt ;
    private LocalDateTime lastUpdated;




}
