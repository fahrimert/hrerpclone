package com.hrerp.mapper;


import com.hrerp.dto.JobPostingRequestDTO;
import com.hrerp.dto.JobPostingRequestRecruiterSpesificDTO;
import com.hrerp.dto.JobPostingResponseRecruiterSpesificDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.hrerp.dto.JobPostingResponseDTO;

@Service
public class JobPostingMapper {



    public JobPostingResponseRecruiterSpesificDTO fromJobPostingToRecruiterSpesificFetch(JobPostingRequestRecruiterSpesificDTO jobPosting){
        return  new JobPostingResponseRecruiterSpesificDTO(
                jobPosting.getInternalJobId(),
                jobPosting.getHiringManagerName(),
                jobPosting.isReplacement(),
                jobPosting.getReplacementFor(),
                jobPosting.getInternalHrNote(),
                jobPosting.getInternalPostingDate()
        );
    }




}
