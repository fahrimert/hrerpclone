package RecruitmentTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrerp.controller.RecruitmentController;
import com.hrerp.dto.ApiResponse;
import com.hrerp.dto.JobPostingRequestRecruiterSpesificDTO;
import com.hrerp.dto.JobPostingResponseRecruiterSpesificDTO;
import com.hrerp.service.RecruitmentProcessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class RecruitmentControllerUnitTest {

    @Mock
    private RecruitmentProcessService recruitmentProcessService;

    @InjectMocks
    private RecruitmentController recruitmentController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void updateJobPosting_withRecruiter_return200 () throws Exception{
        JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO = new JobPostingRequestRecruiterSpesificDTO();


        jobPostingRequestRecruiterSpesificDTO.setInternalJobId(5L);
        jobPostingRequestRecruiterSpesificDTO.setHiringManagerName("Mock Hiring Manager");
        jobPostingRequestRecruiterSpesificDTO.setReplacement(false);
        jobPostingRequestRecruiterSpesificDTO.setReplacementFor(null);
        jobPostingRequestRecruiterSpesificDTO.setInternalHrNote("Hr Interview Could Have Been Better");
        jobPostingRequestRecruiterSpesificDTO.setInternalPostingDate(LocalDateTime.now());


        ResponseEntity<ApiResponse> response = recruitmentController.updateRecruiterSpesificSectionsOnJobPosting(jobPostingRequestRecruiterSpesificDTO, jobPostingRequestRecruiterSpesificDTO.getInternalJobId());

        System.out.println(response);



    }

}
