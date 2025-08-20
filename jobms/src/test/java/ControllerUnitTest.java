import com.hrerp.jobposting.application.controller.JobPostingController;
import com.hrerp.jobposting.application.dto.*;
import com.hrerp.jobposting.application.dto.enums.JobStatus;
import com.hrerp.jobposting.application.dto.enums.JobType;
import com.hrerp.jobposting.application.dto.enums.Location;
import com.hrerp.jobposting.application.dto.enums.WorkType;
import com.hrerp.jobposting.application.service.JobPostingService;
import com.hrerp.jobposting.infrastructure.Client.ApplicationsClient;
import com.hrerp.jobposting.infrastructure.mapper.JobPostingMapper;
import com.hrerp.jobposting.infrastructure.persistence.JobPosting;
import com.hrerp.jobposting.infrastructure.repository.JobPostingRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;


@ExtendWith(MockitoExtension.class)
@Transactional
@Import(FeignAutoConfiguration.class)
@ActiveProfiles("test")
public class ControllerUnitTest {
       @InjectMocks  private   JobPostingController controller;


    @Mock
    private  JobPostingService jobPostingService;


    @InjectMocks
    private JobPostingMapper jobPostingMapper;

    @Mock
    private ApplicationsClient applicationsClient;


      @Mock  private  JobPostingRepository jobPostingRepository;


    @Test
    void  shouldGetJobPostings() throws Exception{
        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Tuesday,Friday");
        location.setWorkType(WorkType.HYBRID);

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("Frontend Developer");
        mockJobPosting.setJobPostingDescription("Frontend dev with React");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("React","Angular","CASS"));
        mockJobPosting.setDepartment("Software Development");
        mockJobPosting.setHiringManagerName("mock hiring manager name");
        mockJobPosting.setLocation(location);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock internalhr note");


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,+10);
        Date tenDaysLater = calendar.getTime();

        mockJobPosting.setJobPostingDeadline(tenDaysLater);
        mockJobPosting.setInternalPostingDate(LocalDate.now());
        mockJobPosting.setJobPostingDate(new Date());


        JobPosting savedJobPosting = jobPostingRepository.save(mockJobPosting);


        JobPostingResponseDTO jobPostingResponseDTO = new JobPostingResponseDTO();
        jobPostingResponseDTO.setJobTitle(mockJobPosting.getJobTitle());
        jobPostingResponseDTO.setJobDescription(mockJobPosting.getJobPostingDescription());
        jobPostingResponseDTO.setSalary(mockJobPosting.getSalary());
        jobPostingResponseDTO.setJobType(mockJobPosting.getJobType());
        jobPostingResponseDTO.setRequiredSkillsList(mockJobPosting.getRequiredSkillsList());
        jobPostingResponseDTO.setDepartment(mockJobPosting.getDepartment());
        jobPostingResponseDTO.setLocation(mockJobPosting.getLocation());
        jobPostingResponseDTO.setJobStatus(mockJobPosting.getPostingStatus());


        when(jobPostingService.findAllJobPostings())
                .thenReturn(ResponseEntity.ok(List.of(jobPostingResponseDTO)));

        ResponseEntity<List<JobPostingResponseDTO>> result = controller.getAllJobPostings();

        JobPostingResponseDTO  responseOfController = (JobPostingResponseDTO) result.getBody().get(0);

        assertEquals(OK , result.getStatusCode());
        assertEquals(jobPostingResponseDTO.getJobDescription(), responseOfController.getJobDescription());
        assertEquals(jobPostingResponseDTO.getJobTitle(), responseOfController.getJobTitle());
        assertEquals(jobPostingResponseDTO.getSalary(), responseOfController.getSalary());
        assertEquals(jobPostingResponseDTO.getJobType(), responseOfController.getJobType());

        assertNotNull(result);

    }

    @Test
    void  shouldGetJobPostingById() throws Exception{
        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Tuesday,Friday");
        location.setWorkType(WorkType.HYBRID);

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("Frontend Developer");
        mockJobPosting.setJobPostingDescription("Frontend dev with React");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("React","Angular","CASS"));
        mockJobPosting.setDepartment("Software Development");
        mockJobPosting.setHiringManagerName("mock hiring manager name");
        mockJobPosting.setLocation(location);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock internalhr note");


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,+10);
        Date tenDaysLater = calendar.getTime();

        mockJobPosting.setJobPostingDeadline(tenDaysLater);
        mockJobPosting.setInternalPostingDate(LocalDate.now());
        mockJobPosting.setJobPostingDate(new Date());


        JobPosting savedJobPosting = jobPostingRepository.save(mockJobPosting);


        JobPostingResponseDTO jobPostingResponseDTO = new JobPostingResponseDTO();
        jobPostingResponseDTO.setJobTitle(mockJobPosting.getJobTitle());
        jobPostingResponseDTO.setJobDescription(mockJobPosting.getJobPostingDescription());
        jobPostingResponseDTO.setSalary(mockJobPosting.getSalary());
        jobPostingResponseDTO.setJobType(mockJobPosting.getJobType());
        jobPostingResponseDTO.setRequiredSkillsList(mockJobPosting.getRequiredSkillsList());
        jobPostingResponseDTO.setDepartment(mockJobPosting.getDepartment());
        jobPostingResponseDTO.setLocation(mockJobPosting.getLocation());
        jobPostingResponseDTO.setJobStatus(mockJobPosting.getPostingStatus());


        when(jobPostingService.findJobById(mockJobPosting.getId()))
                .thenReturn(ResponseEntity.status(OK).body(jobPostingResponseDTO));

        ResponseEntity<?> result = controller.getJobById(mockJobPosting.getId());

        JobPostingResponseDTO  responseOfController = (JobPostingResponseDTO) result.getBody();

        assertEquals(OK , result.getStatusCode());
        assertEquals(jobPostingResponseDTO.getJobDescription(), responseOfController.getJobDescription());
        assertEquals(jobPostingResponseDTO.getJobTitle(), responseOfController.getJobTitle());
        assertEquals(jobPostingResponseDTO.getSalary(), responseOfController.getSalary());
        assertEquals(jobPostingResponseDTO.getJobType(), responseOfController.getJobType());

        assertNotNull(result);

    }

    @Test
    void shouldCreateJobPostingById() throws Exception {
        JobPostingRequestDTO jobPostingRequestDTO = new JobPostingRequestDTO();
        jobPostingRequestDTO.setJobTitle("Frontend Developer");
        jobPostingRequestDTO.setJobDescription("Frontend dev with React");
        jobPostingRequestDTO.setSalary(40000);
        jobPostingRequestDTO.setJobPostingStatus(JobStatus.LIVE);
        jobPostingRequestDTO.setJobType(JobType.FULL_TIME);
        jobPostingRequestDTO.setRequiredSkillsList(List.of("React","Angular","CASS"));


        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Monday,Wednesday");
        location.setWorkType(WorkType.HYBRID);


        jobPostingRequestDTO.setLocation(location);
        jobPostingRequestDTO.setDepartment("Software Development");



        ApiResponse mockApiResponse = ApiResponse.success(jobPostingRequestDTO.getJobTitle());

        ResponseEntity<ApiResponse> mockResponseEntity = ResponseEntity.status(ACCEPTED).body(mockApiResponse);
        when(jobPostingService.createJobPosting(jobPostingRequestDTO)).thenReturn(mockResponseEntity);

        ResponseEntity<ApiResponse> result = controller.createJobPosting(jobPostingRequestDTO);



        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
        assertEquals(jobPostingRequestDTO.getJobTitle(), result.getBody().getData());
        assertNotNull(result);





    }


    @Test
    void shouldUpdateJobPosting() throws Exception {
        JobPostingRequestDTO jobPostingRequestDTO = new JobPostingRequestDTO();
        jobPostingRequestDTO.setId(132L);
        jobPostingRequestDTO.setJobTitle("Frontend Developer");
        jobPostingRequestDTO.setJobDescription("Frontend dev with React");
        jobPostingRequestDTO.setSalary(40000);
        jobPostingRequestDTO.setJobPostingStatus(JobStatus.LIVE);
        jobPostingRequestDTO.setJobType(JobType.FULL_TIME);
        jobPostingRequestDTO.setRequiredSkillsList(List.of("React","Angular","CASS"));


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,+10);
        Date tenDaysLater = calendar.getTime();

        jobPostingRequestDTO.setJobPostingDeadline(tenDaysLater);

        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Monday,Wednesday");
        location.setWorkType(WorkType.HYBRID);


        jobPostingRequestDTO.setLocation(location);
        jobPostingRequestDTO.setDepartment("Software Development");




        ApiResponse mockApiResponse = ApiResponse.success(jobPostingRequestDTO);

        JobPostingResponseDTO jobPostingResponseDTO = new JobPostingResponseDTO();
        jobPostingResponseDTO.setJobTitle(jobPostingRequestDTO.getJobTitle());
        jobPostingResponseDTO.setJobDescription(jobPostingRequestDTO.getJobDescription());
        jobPostingResponseDTO.setSalary(jobPostingRequestDTO.getSalary());
        jobPostingResponseDTO.setJobType(jobPostingRequestDTO.getJobType());
        jobPostingResponseDTO.setRequiredSkillsList(jobPostingRequestDTO.getRequiredSkillsList());
        jobPostingResponseDTO.setDepartment(jobPostingRequestDTO.getDepartment());
        jobPostingResponseDTO.setLocation(jobPostingRequestDTO.getLocation());
        jobPostingResponseDTO.setJobStatus(jobPostingRequestDTO.getJobPostingStatus());

        ResponseEntity<JobPostingResponseDTO> mockResponseEntity = ResponseEntity.ok(jobPostingResponseDTO);
        when(jobPostingService.updateJobById(jobPostingRequestDTO.getId(),jobPostingRequestDTO)).thenReturn(mockResponseEntity);

        ResponseEntity<JobPostingResponseDTO> result = controller.updateJobPosting(jobPostingRequestDTO.getId(),jobPostingRequestDTO);

        assertEquals(OK, result.getStatusCode());
        assertNotNull(result);





    }
    @Test
    void shouldIncrementApplication() throws  Exception {
        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(10L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("Frontend Developer");
        mockJobPosting.setJobPostingDescription("Frontend dev with React");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("React","Angular","CASS"));
        mockJobPosting.setDepartment("Software Development");
        mockJobPosting.setHiringManagerName("mock hiring manager name");
        mockJobPosting.setApplicationCount(5);

        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Monday,Wednesday");
        location.setWorkType(WorkType.HYBRID);


        mockJobPosting.setLocation(location);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock internalhr note");



        when(jobPostingService.incrementApplication(mockJobPosting.getId()))
                .thenReturn(ResponseEntity.ok(ApiResponse.success("Application Count Incremented")));



        ResponseEntity<ApiResponse> result = controller.incrementApplication(mockJobPosting.getId());

        ApiResponse  mockResponse = (ApiResponse) result.getBody();
        assertEquals("Application Count Incremented", mockResponse.getData());
        assertEquals("Success", mockResponse.getMessage());
                assertNotNull(result);
        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertEquals("Application Count Incremented",mockResponse.getData());

    }


    @Test
    void shouldDeleteJobPosting() throws  Exception {
        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(10L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("Frontend Developer");
        mockJobPosting.setJobPostingDescription("Frontend dev with React");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("React","Angular","CASS"));
        mockJobPosting.setDepartment("Software Development");
        mockJobPosting.setHiringManagerName("mock hiring manager name");
        mockJobPosting.setApplicationCount(5);

        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Monday,Wednesday");
        location.setWorkType(WorkType.HYBRID);


        mockJobPosting.setLocation(location);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock internalhr note");


        when(jobPostingService.deleteJobPosting(mockJobPosting.getId()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success(
                                "Delete Job posting succesfully applied"
                        )));



        ResponseEntity<ApiResponse> result = controller.deleteJobPosting(mockJobPosting.getId());

        ApiResponse  mockResponse = (ApiResponse) result.getBody();
        assertEquals(                                "Delete Job posting succesfully applied", mockResponse.getData());
        assertEquals("Success", mockResponse.getMessage());
                assertNotNull(result);


    }


    @Test
    void shouldGetJobTitle() throws  Exception {
        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(10L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("Frontend Developer");


        when(jobPostingService.getJobTitle(mockJobPosting.getId()))
                .thenReturn(mockJobPosting.getJobTitle());



        String result = controller.getJobTitle(mockJobPosting.getId());
        assertEquals( mockJobPosting.getJobTitle(), result);
        assertNotNull(result);


    }


    @Test
    void shouldGetApplicationsBasedOnJob() throws  Exception {
        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(10L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("Frontend Developer");

        Location location = new Location();

        location.setAddress("Mock Address");
        location.setCity("City Of San Fransisco");
        location.setCountry("USA");
        location.setWorkType(WorkType.OFFICE);
        location.setOfficeDays("Wednesday , Friday");

        mockJobPosting.setLocation(location);





        ApplicationListDTO applicationListDTO = new ApplicationListDTO();

        applicationListDTO.setApplicationId(10L);
        applicationListDTO.setApplicationDate(LocalDate.now());
        applicationListDTO.setCandidateEmail("mockcandidateemail");
        applicationListDTO.setCandidateFullName("mock candidate full name");
        applicationListDTO.setCandidateId(10L);

        JobPostingWithApplicationsResponseDTO jobPostingWithApplicationsResponseDTO = new JobPostingWithApplicationsResponseDTO();
        jobPostingWithApplicationsResponseDTO.setJobId(mockJobPosting.getId());
        jobPostingWithApplicationsResponseDTO.setJobTitle(mockJobPosting.getJobTitle());
        jobPostingWithApplicationsResponseDTO.setCity(mockJobPosting.getLocation().getCity());
        jobPostingWithApplicationsResponseDTO.setJobStatus(mockJobPosting.getPostingStatus());
        jobPostingWithApplicationsResponseDTO.setJobPostingDate(mockJobPosting.getJobPostingDate());
        jobPostingWithApplicationsResponseDTO.setJobPostingDeadline(mockJobPosting.getJobPostingDeadline());

        ArrayList<ApplicationListDTO> listDTOArrayList = new ArrayList<>();

        listDTOArrayList.add(applicationListDTO);
        jobPostingWithApplicationsResponseDTO.setCandidateList(listDTOArrayList);




        when(jobPostingService.getApplicationList(mockJobPosting.getId())).thenReturn(ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        jobPostingWithApplicationsResponseDTO
                )));
        ResponseEntity<ApiResponse>  result = controller.getApplicationsBasedOnJob(mockJobPosting.getId());


JobPostingWithApplicationsResponseDTO jobPostingWithApplicationsResponseDTO1 = (JobPostingWithApplicationsResponseDTO) result.getBody().getData();


        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertEquals(mockJobPosting.getJobTitle(),jobPostingWithApplicationsResponseDTO1.getJobTitle());
        assertEquals(mockJobPosting.getPostingStatus(),jobPostingWithApplicationsResponseDTO1.getJobStatus());
        assertEquals(mockJobPosting.getJobPostingDeadline(),jobPostingWithApplicationsResponseDTO1.getJobPostingDeadline());



    }


    @Test
    void shouldGetApplicationBasedOnJobAndCandidateId() throws  Exception {
        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(10L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("Frontend Developer");

        Location location = new Location();

        location.setAddress("Mock Address");
        location.setCity("City Of San Fransisco");
        location.setCountry("USA");
        location.setWorkType(WorkType.OFFICE);
        location.setOfficeDays("Wednesday , Friday");

        mockJobPosting.setLocation(location);

        ApplicationsDTO applicationSingleDTO = new ApplicationsDTO();

        applicationSingleDTO.setApplicationId(10L);
        applicationSingleDTO.setApplicationDate(LocalDate.now());
        applicationSingleDTO.setCandidateEmail("mockcandidateemail");
        applicationSingleDTO.setCandidateFullName("mock candidate full name");
        applicationSingleDTO.setCandidateId(10L);

        JobPostingWithSingleApplicationResponseDTO jobPostingWithSingleApplicationResponseDTO = new JobPostingWithSingleApplicationResponseDTO();
        jobPostingWithSingleApplicationResponseDTO.setJobId(mockJobPosting.getId());
        jobPostingWithSingleApplicationResponseDTO.setJobTitle(mockJobPosting.getJobTitle());
        jobPostingWithSingleApplicationResponseDTO.setCity(mockJobPosting.getLocation().getCity());
        jobPostingWithSingleApplicationResponseDTO.setJobStatus(mockJobPosting.getPostingStatus());
        jobPostingWithSingleApplicationResponseDTO.setJobPostingDate(mockJobPosting.getJobPostingDate());
        jobPostingWithSingleApplicationResponseDTO.setJobPostingDeadline(mockJobPosting.getJobPostingDeadline());


        jobPostingWithSingleApplicationResponseDTO.setCandidate(applicationSingleDTO);




        when(jobPostingService.getApplication(mockJobPosting.getId(),applicationSingleDTO.getCandidateId())).thenReturn(ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        jobPostingWithSingleApplicationResponseDTO
                )));
        ResponseEntity<ApiResponse>  result = controller.getApplicationsBasedOnJob(mockJobPosting.getId(),applicationSingleDTO.getCandidateId());

        JobPostingWithSingleApplicationResponseDTO jobPostingWithApplicationSingleDTO = (JobPostingWithSingleApplicationResponseDTO) result.getBody().getData();

        assertNotNull(result.getBody().getData());
        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertEquals(mockJobPosting.getJobTitle(),jobPostingWithApplicationSingleDTO.getJobTitle());
        assertEquals(mockJobPosting.getPostingStatus(),jobPostingWithApplicationSingleDTO.getJobStatus());
        assertEquals(applicationSingleDTO.getCandidateId(),jobPostingWithApplicationSingleDTO.getCandidate().getCandidateId());

        assertEquals(mockJobPosting.getJobPostingDeadline(),jobPostingWithApplicationSingleDTO.getJobPostingDeadline());


    }


    @Test
    void recruiterSpesificUpdate_ShouldReturnUpdatedJobPosting_WhenRequestIsValid() throws  Exception {



        JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO = new JobPostingRequestRecruiterSpesificDTO();
        jobPostingRequestRecruiterSpesificDTO.setInternalJobId(12L);
        jobPostingRequestRecruiterSpesificDTO.setReplacement(false);
        jobPostingRequestRecruiterSpesificDTO.setReplacementFor(null);
        jobPostingRequestRecruiterSpesificDTO.setInternalHrNote("Internal Mock HR note");
        jobPostingRequestRecruiterSpesificDTO.setHiringManagerName("Mock HR manager name");
        jobPostingRequestRecruiterSpesificDTO.setInternalPostingDate(LocalDate.now());


        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(10L);
        mockJobPosting.setInternalJobId(10L);
        mockJobPosting.setJobTitle("Frontend Developer");

        Location location = new Location();

        location.setAddress("Mock Address");
        location.setCity("City Of San Fransisco");
        location.setCountry("USA");
        location.setWorkType(WorkType.OFFICE);
        location.setOfficeDays("Wednesday , Friday");

        mockJobPosting.setLocation(location);

        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDTO  = new JobPostingResponseRecruiterSpesificDTO(
                jobPostingRequestRecruiterSpesificDTO.getInternalJobId(),
                jobPostingRequestRecruiterSpesificDTO.getHiringManagerName(),
                jobPostingRequestRecruiterSpesificDTO.isReplacement(),
                jobPostingRequestRecruiterSpesificDTO.getReplacementFor(),
                jobPostingRequestRecruiterSpesificDTO.getInternalHrNote(),
                jobPostingRequestRecruiterSpesificDTO.getInternalPostingDate()
        );



        when(jobPostingService.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO,mockJobPosting.getId())).thenReturn(
                ResponseEntity.ok(ApiResponse.success(jobPostingResponseRecruiterSpesificDTO))
        );

        ResponseEntity<ApiResponse>  result = controller.recruiterSpesificUpdateOnJobPosting(jobPostingRequestRecruiterSpesificDTO,mockJobPosting.getId());

        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDTOO = (JobPostingResponseRecruiterSpesificDTO) result.getBody().getData();
        assertNotNull(result.getBody().getData());
        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertEquals(jobPostingResponseRecruiterSpesificDTO.getInternalHrNote(),jobPostingResponseRecruiterSpesificDTOO.getInternalHrNote());
        assertEquals(jobPostingResponseRecruiterSpesificDTO.getInternalPostingDate(),jobPostingResponseRecruiterSpesificDTOO.getInternalPostingDate());
        assertEquals(jobPostingResponseRecruiterSpesificDTOO.getHiringManagerName(),jobPostingResponseRecruiterSpesificDTOO.getHiringManagerName());



    }



    @Test
    void recruiterSpesificFetch_ShouldRecruiterSpesificData_WhenRequestIsValid() throws  Exception  {

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(10L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("Frontend Developer");
        mockJobPosting.setHiringManagerName("Mock Hiring Manaer");
        mockJobPosting.setReplacement(false);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("Mock Hiring Manager note ");

        Location location = new Location();

        location.setAddress("Mock Address");
        location.setCity("City Of San Fransisco");
        location.setCountry("USA");
        location.setWorkType(WorkType.OFFICE);
        location.setOfficeDays("Wednesday , Friday");

        mockJobPosting.setLocation(location);
        mockJobPosting.setLocation(location);

        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDToo  = new JobPostingResponseRecruiterSpesificDTO(
                mockJobPosting.getInternalJobId(),
                mockJobPosting.getHiringManagerName(),
                mockJobPosting.isReplacement(),
                mockJobPosting.getReplacementFor(),
                mockJobPosting.getInternalHrNote(),
                mockJobPosting.getInternalPostingDate()
        );




        when(jobPostingService.recruiterSpesificFetch(mockJobPosting.getId())).thenReturn(
                ResponseEntity.ok(ApiResponse.success(jobPostingResponseRecruiterSpesificDToo))
        );

        ResponseEntity<ApiResponse>  result = controller.recruiterSpesificFetch(mockJobPosting.getId());

        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDTOO = (JobPostingResponseRecruiterSpesificDTO) result.getBody().getData();

        assertNotNull(result.getBody().getData());
        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertEquals(jobPostingResponseRecruiterSpesificDToo.getInternalHrNote(),jobPostingResponseRecruiterSpesificDTOO.getInternalHrNote());
        assertEquals(jobPostingResponseRecruiterSpesificDToo.getInternalPostingDate(),jobPostingResponseRecruiterSpesificDTOO.getInternalPostingDate());
        assertEquals(jobPostingResponseRecruiterSpesificDToo.getHiringManagerName(),jobPostingResponseRecruiterSpesificDTOO.getHiringManagerName());



    }


    @Test
    void ShouldReturnJobPostingExistsById_WhenRequestIsValid() throws  Exception  {

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(10L);



        when(jobPostingService.jobPostingExistsById(mockJobPosting.getId())).thenReturn(true);

        boolean  result = controller.jobPostingsExistById(  mockJobPosting.getId());

        boolean jobPostingResponseRecruiterSpesificDTOO = result;


        assertNotNull(result);
        assertEquals(true,result);



    }





}
