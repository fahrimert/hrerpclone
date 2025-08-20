package com.hrerp.model.enums;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrerp.model.Interview;
import com.hrerp.model.enums.InterviewProcesses;
import com.hrerp.model.enums.InterviewScore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;
import  java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class InterviewQuestions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @Size(max = 1000)
    @NotNull(message = "Interview Question should be present")
    private String questionText;

    @Size(max = 1000)
    private  String candidateAnswer;

    @NotNull(message = "Question score should be present")
    private  Double question_score;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    @JsonIgnore
    private Interview interview;

    @CreatedDate
    private Date createdAt;

}
