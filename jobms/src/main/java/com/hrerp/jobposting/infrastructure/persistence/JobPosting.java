package com.hrerp.jobposting.infrastructure.persistence;


import com.hrerp.jobposting.application.dto.enums.JobStatus;
import com.hrerp.jobposting.application.dto.enums.JobType;
import com.hrerp.jobposting.application.dto.enums.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "job_posting")

public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    //for recruitmentt ıd
    private Long internalJobId;

    @NotNull(message = "Job Title should be present")
    @NotEmpty(message = "Job Title  should be present")
    @NotBlank(message = "Job Title should be present")
    @Column(unique = true)
    private String jobTitle;


    @Column(name = "job_posting_description",length = 255)
    @Size(min = 4,max = 30,message = "Description must be between 4 and 15 characters")
    private String jobPostingDescription;

    @Column(name = "hr_internal_description",length = 255)
    @Size(min = 4,max = 30,message = "HR Description must be between 4 and 15 characters")
    private  String internalDescripton;



    @PositiveOrZero(message = "Salary must be positive")
    private  Integer salary;

    private JobType jobType;

    @ElementCollection
    private List<String> requiredSkillsList;

    private  String department;

    //daha sonra burası employee modeli olacak şimdilik ismi oluyor bu kısım işe alan kişinin bilgileri
    private  String hiringManagerName;

    @Column(name = "application_count")
    private  Integer applicationCount = 0;

    @Embedded
    private Location location;



    //Finans sistemindeki karşılık gelen kodu
    //ınternal fields
//    private String budgetCode;
//    private String requester;
//    private String hiringManagerEmail;
//    private  Integer budget;

    private JobStatus postingStatus;

    // is it replacement for already working worker
    private  boolean isReplacement = false;
    private String replacementFor ;

    private  String internalHrNote;
    private Date jobPostingDeadline;

    private LocalDate internalPostingDate;



    @CreatedDate
    private Date jobPostingDate;





}
