package com.hrerp.wrongİmplementation.mapper;


import com.hrerp.dto.JobPostingDTOs.JobPostingRequestRecruiterSpesificDTO;
import com.hrerp.dto.JobPostingDTOs.JobPostingResponseRecruiterSpesificDTO;
import org.springframework.stereotype.Service;

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
