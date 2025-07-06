import com.hrerp.common.dto.JobPostingResponseDTO;
import com.hrerp.common.dto.enums.JobStatus;
import com.hrerp.common.dto.enums.JobType;
import com.hrerp.common.dto.enums.Location;
import com.hrerp.common.dto.enums.WorkType;
import com.hrerp.jobposting.application.controller.JobPostingController;
import com.hrerp.jobposting.application.dto.ApiResponse;
import com.hrerp.jobposting.application.dto.JobPostingRequestDTO;
import com.hrerp.jobposting.infrastructure.mapper.JobPostingMapper;
import com.hrerp.jobposting.domain.model.JobPosting;
import com.hrerp.jobposting.application.service.JobPostingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
@ExtendWith(MockitoExtension.class)
public class ControllerUnitTest {
        @InjectMocks
        private JobPostingController controller;

        @Mock
        private JobPostingService jobPostingService;
        @Mock
        private JobPostingMapper jobPostingMapper;


        @Test
        void shouldCreateJobPostings() throws Exception {

            com.hrerp.common.dto.enums.Location location = new Location();
            location.setCountry("Türkiye");
            location.setCity("Ankara");
            location.setAddress("Balgat");
            location.setOfficeDays("Monday,Wednesday");
            location.setWorkType(WorkType.HYBRID);

            JobPosting jobPosting = new JobPosting();
            jobPosting.setJobTitle("Frontend Developer");
            jobPosting.setJobPostingDescription("Frontend dev with React");
            jobPosting.setSalary(40000);
            jobPosting.setPostingStatus(JobStatus.LIVE);
            jobPosting.setJobType(JobType.FULL_TIME);
            jobPosting.setRequiredSkillsList(List.of("React","Angular","CASS"));
            jobPosting.setLocation(location);
            jobPosting.setDepartment("Software Development");

            JobPostingRequestDTO mockJobPostingRequestDTO =  new JobPostingRequestDTO();
            mockJobPostingRequestDTO.setJobTitle("Frontend Developer");
            mockJobPostingRequestDTO.setJobDescription("Frontend dev with React");
            mockJobPostingRequestDTO.setSalary(40000);
            mockJobPostingRequestDTO.setJobPostingStatus(JobStatus.LIVE);
            mockJobPostingRequestDTO.setJobType(JobType.FULL_TIME);
            mockJobPostingRequestDTO.setRequiredSkillsList(List.of("React","Angular","CASS"));
            mockJobPostingRequestDTO.setLocation(location);
            mockJobPostingRequestDTO.setDepartment("Software Development");


            ApiResponse mockApiResponse = ApiResponse.success("Frontend Developer");
            ResponseEntity<ApiResponse> mockResponseEntity = ResponseEntity.status(ACCEPTED).body(mockApiResponse);
            when(jobPostingService.createJobPosting(mockJobPostingRequestDTO)).thenReturn(mockResponseEntity);

            ResponseEntity<ApiResponse> result = controller.createJobPosting(mockJobPostingRequestDTO);
            assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
            System.out.println(result.getBody().getData());
            assertEquals("Frontend Developer", result.getBody().getData());
            assertNotNull(result);
            verify(jobPostingService).createJobPosting(mockJobPostingRequestDTO);




        }

        @Test
        void  shouldGetJobPostings() throws Exception{
            Location location = new Location();
            location.setCountry("Türkiye");
            location.setCity("Ankara");
            location.setAddress("Balgat");
            location.setOfficeDays("Tuesday,Friday");
            location.setWorkType(WorkType.HYBRID);


            JobPostingResponseDTO jobPostingResponseDTO = new JobPostingResponseDTO();
            jobPostingResponseDTO.setJobTitle("Frontend Developer");
            jobPostingResponseDTO.setJobDescription("Frontend dev with React");
            jobPostingResponseDTO.setSalary(40000);
            jobPostingResponseDTO.setJobStatus(JobStatus.LIVE);
            jobPostingResponseDTO.setJobType(JobType.FULL_TIME);
            jobPostingResponseDTO.setRequiredSkillsList(List.of("React","Angular","CASS"));
            jobPostingResponseDTO.setLocation(location);
            jobPostingResponseDTO.setDepartment("Software Development");

            when(jobPostingService.findAllJobPostings())
                    .thenReturn(ResponseEntity.ok(List.of(jobPostingResponseDTO)));

            ResponseEntity<List<JobPostingResponseDTO>> result = controller.getAllJobPostings();
            assertEquals(OK , result.getStatusCode());
            assertEquals(List.of(jobPostingResponseDTO), result.getBody());
            assertNotNull(result);

        }
 }
