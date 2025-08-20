import com.hrerp.jobposting.application.dto.*;
import com.hrerp.jobposting.application.dto.enums.JobStatus;
import com.hrerp.jobposting.application.dto.enums.JobType;
import com.hrerp.jobposting.application.dto.enums.Location;
import com.hrerp.jobposting.application.dto.enums.WorkType;
import com.hrerp.jobposting.application.service.JobPostingServiceImpl;
import com.hrerp.jobposting.infrastructure.Client.ApplicationsClient;
import com.hrerp.jobposting.infrastructure.mapper.JobPostingMapper;
import com.hrerp.jobposting.infrastructure.persistence.JobPosting;
import com.hrerp.jobposting.infrastructure.repository.JobPostingRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
        @ExtendWith(MockitoExtension.class)
        @Transactional
public class ServiceUnitTest {

    @Mock
    private JobPostingRepository jobPostingRepository;

    @InjectMocks
    private JobPostingServiceImpl jobPostingServiceImpl;

    @Mock
    private ApplicationsClient applicationsClient;




    private JobPostingMapper jobPostingMapper = new JobPostingMapper();


    @BeforeEach
    void setUp() {

        jobPostingServiceImpl.setJobPostingMapper(jobPostingMapper);
    }




    @Test
    void  shouldGetJobPostings() throws Exception{
        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(15L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setJobPostingDescription("mock Job Posting Description");
        mockJobPosting.setInternalDescripton("mock Job Posting Internal Description");
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("Required Skill 1","Required Skill 2"));
        mockJobPosting.setDepartment("Software");
        mockJobPosting.setHiringManagerName(null);

        Location mockLocation = new Location();
        mockLocation.setOfficeDays("Wednesday, Friday");
        mockLocation.setCity("Mock City");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setCountry("Mock Country");
        mockLocation.setAddress("Mock Address");

        mockJobPosting.setLocation(mockLocation);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();

        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar4.getTime();

        mockJobPosting.setJobPostingDate(tenDaysAgo);
        mockJobPosting.setJobPostingDeadline(tenDaysLater);

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));



            JobPosting mockJobPosting16 = new JobPosting();
            mockJobPosting16.setId(16L);
            mockJobPosting16.setInternalJobId(16L);
            mockJobPosting16.setJobTitle("mockJobPosting16");
            mockJobPosting16.setPostingStatus(JobStatus.CLOSED);
            mockJobPosting16.setJobPostingDescription("mock Job Posting Description");
            mockJobPosting16.setInternalDescripton("mock Job Posting Internal Description");
            mockJobPosting16.setJobTitle("mockJobPosting");
            mockJobPosting16.setSalary(40000);
            mockJobPosting16.setJobType(JobType.FULL_TIME);
            mockJobPosting16.setRequiredSkillsList(List.of("Required Skill 1","Required Skill 2"));
            mockJobPosting16.setDepartment("Software");
            mockJobPosting16.setHiringManagerName(null);

            Location mockLocation16 = new Location();
            mockLocation16.setOfficeDays("Wednesday, Friday");
            mockLocation16.setCity("Mock City");
            mockLocation16.setWorkType(WorkType.HYBRID);
            mockLocation16.setCountry("Mock Country");
            mockLocation16.setAddress("Mock Address");

            mockJobPosting16.setLocation(mockLocation);
            mockJobPosting16.setPostingStatus(JobStatus.LIVE);
            mockJobPosting16.setReplacementFor(null);
            mockJobPosting16.setInternalHrNote("mock hr note");

            mockJobPosting16.setJobPostingDate(tenDaysAgo);
            mockJobPosting16.setJobPostingDeadline(tenDaysLater);

            mockJobPosting16.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
            ));

        ArrayList<JobPosting> jobPostingArrayList = new ArrayList<>();

        jobPostingArrayList.add(mockJobPosting);
        jobPostingArrayList.add(mockJobPosting16);




        JobPostingResponseDTO jobPostingResponseDTO = new JobPostingResponseDTO();
        jobPostingResponseDTO.setJobTitle(mockJobPosting.getJobTitle());
        jobPostingResponseDTO.setJobDescription(mockJobPosting.getJobPostingDescription());
        jobPostingResponseDTO.setSalary(mockJobPosting.getSalary());
        jobPostingResponseDTO.setJobType(mockJobPosting.getJobType());
        jobPostingResponseDTO.setRequiredSkillsList(mockJobPosting.getRequiredSkillsList());
        jobPostingResponseDTO.setDepartment(mockJobPosting.getDepartment());
        jobPostingResponseDTO.setLocation(mockJobPosting.getLocation());
        jobPostingResponseDTO.setJobStatus(mockJobPosting.getPostingStatus());

        JobPostingResponseDTO jobPostingResponseDTO16 = new JobPostingResponseDTO();
        jobPostingResponseDTO16.setJobTitle(mockJobPosting16.getJobTitle());
        jobPostingResponseDTO16.setJobDescription(mockJobPosting16.getJobPostingDescription());
        jobPostingResponseDTO16.setSalary(mockJobPosting16.getSalary());
        jobPostingResponseDTO16.setJobType(mockJobPosting16.getJobType());
        jobPostingResponseDTO16.setRequiredSkillsList(mockJobPosting16.getRequiredSkillsList());
        jobPostingResponseDTO16.setDepartment(mockJobPosting16.getDepartment());
        jobPostingResponseDTO16.setLocation(mockJobPosting16.getLocation());
        jobPostingResponseDTO16.setJobStatus(mockJobPosting16.getPostingStatus());



        ArrayList<JobPostingResponseDTO> jobPostingResponseArrayList = new ArrayList<>();
        jobPostingResponseArrayList.add(jobPostingResponseDTO);
        jobPostingResponseArrayList.add(jobPostingResponseDTO16);


        when(jobPostingRepository.findAll()).thenReturn(jobPostingArrayList);




        ResponseEntity<List<JobPostingResponseDTO>> responseService = jobPostingServiceImpl.findAllJobPostings();

    }


    @Test
    void  shouldCreateJobPosting() throws Exception{


        JobPostingRequestDTO jobPostingRequestDTO = new JobPostingRequestDTO();
        jobPostingRequestDTO.setId(15L);
        jobPostingRequestDTO.setJobTitle("mockJobPosting");
        jobPostingRequestDTO.setJobDescription("mock Job Posting Description");
        jobPostingRequestDTO.setSalary(40000);
        jobPostingRequestDTO.setJobPostingStatus(JobStatus.LIVE);
        jobPostingRequestDTO.setJobType(JobType.FULL_TIME);
        jobPostingRequestDTO.setRequiredSkillsList(List.of("Required Skill 1","Required Skill 2"));


        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Monday,Wednesday");
        location.setWorkType(WorkType.HYBRID);


        jobPostingRequestDTO.setLocation(location);
        jobPostingRequestDTO.setDepartment("Software Development");

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(15L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setJobPostingDescription("mock Job Posting Description");
        mockJobPosting.setInternalDescripton("mock Job Posting Internal Description");
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("Required Skill 1","Required Skill 2"));
        mockJobPosting.setDepartment("Software");
        mockJobPosting.setHiringManagerName(null);

        Location mockLocation = new Location();
        mockLocation.setOfficeDays("Wednesday, Friday");
        mockLocation.setCity("Mock City");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setCountry("Mock Country");
        mockLocation.setAddress("Mock Address");

        mockJobPosting.setLocation(mockLocation);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();

        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar4.getTime();

        mockJobPosting.setJobPostingDate(tenDaysAgo);
        mockJobPosting.setJobPostingDeadline(tenDaysLater);

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));




        ResponseEntity<ApiResponse> responseService = jobPostingServiceImpl.createJobPosting(jobPostingRequestDTO);




        assertEquals(ACCEPTED,responseService.getStatusCode());
        assertEquals(mockJobPosting.getJobTitle(),responseService.getBody().getData());

    }

    @Test
    void  shouldGetJobById() throws Exception{

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(15L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setJobPostingDescription("mock Job Posting Description");
        mockJobPosting.setInternalDescripton("mock Job Posting Internal Description");
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("Required Skill 1","Required Skill 2"));
        mockJobPosting.setDepartment("Software");
        mockJobPosting.setHiringManagerName(null);


        Location mockLocation = new Location();
        mockLocation.setOfficeDays("Wednesday, Friday");
        mockLocation.setCity("Mock City");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setCountry("Mock Country");
        mockLocation.setAddress("Mock Address");

        mockJobPosting.setLocation(mockLocation);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();

        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar4.getTime();

        mockJobPosting.setJobPostingDate(tenDaysAgo);
        mockJobPosting.setJobPostingDeadline(tenDaysLater);

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));
        when(jobPostingRepository.findById(mockJobPosting.getId())).thenReturn(Optional.of(mockJobPosting));


        ResponseEntity<?> responseService = jobPostingServiceImpl.findJobById(mockJobPosting.getId());

        ApiResponse apiResponse =(ApiResponse) responseService.getBody();
        JobPostingResponseDTO jobPostingResponseDTO = (JobPostingResponseDTO) apiResponse.getData();



        assertEquals(OK,responseService.getStatusCode());
        assertEquals(mockJobPosting.getJobTitle(),jobPostingResponseDTO.getJobTitle());
        assertEquals(mockJobPosting.getJobPostingDescription(),jobPostingResponseDTO.getJobDescription());
        assertEquals(mockJobPosting.getSalary(),jobPostingResponseDTO.getSalary());


    }

    @Test
    void  shouldUpdateJobById() throws Exception{


        JobPostingRequestDTO jobPostingUpdateRequestDTO = new JobPostingRequestDTO();
        jobPostingUpdateRequestDTO.setId(15L);
        jobPostingUpdateRequestDTO.setJobTitle("mockJobPosting Updated Title");
        jobPostingUpdateRequestDTO.setJobDescription("mock Job Posting Updated Description");
        jobPostingUpdateRequestDTO.setSalary(50000);
        jobPostingUpdateRequestDTO.setJobPostingStatus(JobStatus.LIVE);
        jobPostingUpdateRequestDTO.setJobType(JobType.FULL_TIME);
        jobPostingUpdateRequestDTO.setRequiredSkillsList(List.of("Required Skill 1 Updated","Required Skill 2 Updated"));


        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("İstanbul");
        location.setAddress("Esenler");
        location.setOfficeDays("Tuesday,Friday");
        location.setWorkType(WorkType.HYBRID);


        jobPostingUpdateRequestDTO.setLocation(location);
        jobPostingUpdateRequestDTO.setDepartment("Elektrical Development");

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(15L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setJobPostingDescription("mock Job Posting Description");
        mockJobPosting.setInternalDescripton("mock Job Posting Internal Description");
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("Required Skill 1","Required Skill 2"));
        mockJobPosting.setDepartment("Software");
        mockJobPosting.setHiringManagerName(null);

        Location mockLocation = new Location();
        mockLocation.setOfficeDays("Wednesday, Friday");
        mockLocation.setCity("Mock City");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setCountry("Mock Country");
        mockLocation.setAddress("Mock Address");

        mockJobPosting.setLocation(mockLocation);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();

        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar4.getTime();

        mockJobPosting.setJobPostingDate(tenDaysAgo);
        mockJobPosting.setJobPostingDeadline(tenDaysLater);

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));
        when(jobPostingRepository.findById(mockJobPosting.getId())).thenReturn(Optional.of(mockJobPosting));


        ResponseEntity<JobPostingResponseDTO> responseService = jobPostingServiceImpl.updateJobById(mockJobPosting.getId(),jobPostingUpdateRequestDTO);

        assertEquals(OK,responseService.getStatusCode());
        assertEquals(jobPostingUpdateRequestDTO.getJobTitle(),responseService.getBody().getJobTitle());
        assertEquals(jobPostingUpdateRequestDTO.getJobDescription(),responseService.getBody().getJobDescription());
        assertEquals(jobPostingUpdateRequestDTO.getJobPostingStatus(),responseService.getBody().getJobStatus());

    }


    @Test
    void  deleteJobPosting() throws Exception{

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(15L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setJobPostingDescription("mock Job Posting Description");
        mockJobPosting.setInternalDescripton("mock Job Posting Internal Description");
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("Required Skill 1","Required Skill 2"));
        mockJobPosting.setDepartment("Software");
        mockJobPosting.setHiringManagerName(null);

        Location mockLocation = new Location();
        mockLocation.setOfficeDays("Wednesday, Friday");
        mockLocation.setCity("Mock City");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setCountry("Mock Country");
        mockLocation.setAddress("Mock Address");

        mockJobPosting.setLocation(mockLocation);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));
        when(jobPostingRepository.findById(mockJobPosting.getId())).thenReturn(Optional.of(mockJobPosting));


        ResponseEntity<ApiResponse> responseService = jobPostingServiceImpl.deleteJobPosting(mockJobPosting.getId());

        assertEquals(OK,responseService.getStatusCode());
        assertEquals("Delete Job posting succesfully applied",responseService.getBody().getData());


    }

    @Test
    void  incrementApplication() throws Exception{

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(15L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setJobPostingDescription("mock Job Posting Description");
        mockJobPosting.setInternalDescripton("mock Job Posting Internal Description");
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("Required Skill 1","Required Skill 2"));
        mockJobPosting.setDepartment("Software");
        mockJobPosting.setHiringManagerName(null);

        Location mockLocation = new Location();
        mockLocation.setOfficeDays("Wednesday, Friday");
        mockLocation.setCity("Mock City");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setCountry("Mock Country");
        mockLocation.setAddress("Mock Address");

        mockJobPosting.setLocation(mockLocation);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));
        when(jobPostingRepository.findById(mockJobPosting.getId())).thenReturn(Optional.of(mockJobPosting));
        when(jobPostingRepository.save(mockJobPosting)).thenReturn(mockJobPosting);

        ResponseEntity<ApiResponse> responseService = jobPostingServiceImpl.incrementApplication(mockJobPosting.getId());

        assertEquals(OK,responseService.getStatusCode());
        assertEquals("Application Count Incremented",responseService.getBody().getData());


    }

    @Test
    void  getJobTitle() throws Exception{

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(15L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setJobPostingDescription("mock Job Posting Description");
        mockJobPosting.setInternalDescripton("mock Job Posting Internal Description");
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("Required Skill 1","Required Skill 2"));
        mockJobPosting.setDepartment("Software");
        mockJobPosting.setHiringManagerName(null);

        Location mockLocation = new Location();
        mockLocation.setOfficeDays("Wednesday, Friday");
        mockLocation.setCity("Mock City");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setCountry("Mock Country");
        mockLocation.setAddress("Mock Address");

        mockJobPosting.setLocation(mockLocation);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));
        when(jobPostingRepository.findById(mockJobPosting.getId())).thenReturn(Optional.of(mockJobPosting));

        String responseGetJobTitle = jobPostingServiceImpl.getJobTitle(mockJobPosting.getId());

        assertEquals(mockJobPosting.getJobTitle(),responseGetJobTitle);


    }

    @Test
    void  getApplicationList() throws Exception{

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(15L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setJobPostingDescription("mock Job Posting Description");
        mockJobPosting.setInternalDescripton("mock Job Posting Internal Description");
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("Required Skill 1","Required Skill 2"));
        mockJobPosting.setDepartment("Software");
        mockJobPosting.setHiringManagerName(null);

        Location mockLocation = new Location();
        mockLocation.setOfficeDays("Wednesday, Friday");
        mockLocation.setCity("Mock City");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setCountry("Mock Country");
        mockLocation.setAddress("Mock Address");

        mockJobPosting.setLocation(mockLocation);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));

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


        when(jobPostingRepository.findById(mockJobPosting.getId())).thenReturn(Optional.of(mockJobPosting));
        when(applicationsClient.getApplications(mockJobPosting.getId())).thenReturn(listDTOArrayList);


        ResponseEntity<ApiResponse> responseGetApplicationList = jobPostingServiceImpl.getApplicationList(mockJobPosting.getId());

        JobPostingWithApplicationsResponseDTO response = (JobPostingWithApplicationsResponseDTO) responseGetApplicationList.getBody().getData();

        assertEquals(mockJobPosting.getJobTitle(),response.getJobTitle());
        assertEquals(mockJobPosting.getJobPostingDeadline(),response.getJobPostingDeadline());
        assertEquals(mockJobPosting.getJobPostingDate(),response.getJobPostingDate());
        assertEquals(mockJobPosting.getLocation().getCity(),response.getCity());






    }

    @Test
    void  getApplicationSingle() throws Exception{

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(15L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setJobPostingDescription("mock Job Posting Description");
        mockJobPosting.setInternalDescripton("mock Job Posting Internal Description");
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("Required Skill 1","Required Skill 2"));
        mockJobPosting.setDepartment("Software");
        mockJobPosting.setHiringManagerName(null);

        Location mockLocation = new Location();
        mockLocation.setOfficeDays("Wednesday, Friday");
        mockLocation.setCity("Mock City");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setCountry("Mock Country");
        mockLocation.setAddress("Mock Address");

        mockJobPosting.setLocation(mockLocation);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));

        ApplicationsDTO applicationsDTO = new ApplicationsDTO();

        applicationsDTO.setApplicationId(10L);
        applicationsDTO.setApplicationDate(LocalDate.now());
        applicationsDTO.setCandidateEmail("mockcandidateemail");
        applicationsDTO.setCandidateFullName("mock candidate full name");
        applicationsDTO.setCandidateId(10L);




        when(jobPostingRepository.findById(mockJobPosting.getId())).thenReturn(Optional.of(mockJobPosting));
        when(applicationsClient.getApplicationSingle(mockJobPosting.getId(),applicationsDTO.getApplicationId())).thenReturn(ApiResponse.success(applicationsDTO));


        ResponseEntity<ApiResponse> responseGetApplicationSingle = jobPostingServiceImpl.getApplication(mockJobPosting.getId(),applicationsDTO.getCandidateId());
        JobPostingWithSingleApplicationResponseDTO response = (JobPostingWithSingleApplicationResponseDTO) responseGetApplicationSingle.getBody().getData();

        assertEquals(applicationsDTO.getCandidateId(),response.getCandidate().getCandidateId());
        assertEquals(mockJobPosting.getJobTitle(),response.getJobTitle());
        assertEquals(applicationsDTO.getApplicationId(),response.getCandidate().getApplicationId());
    }


    @Test
    void  recruiterSpesificUpdate() throws Exception{

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(15L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setJobPostingDescription("mock Job Posting Description");
        mockJobPosting.setInternalDescripton("mock Job Posting Internal Description");
        mockJobPosting.setJobTitle("mockJobPosting");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("Required Skill 1","Required Skill 2"));
        mockJobPosting.setDepartment("Software");
        mockJobPosting.setHiringManagerName(null);

        Location mockLocation = new Location();
        mockLocation.setOfficeDays("Wednesday, Friday");
        mockLocation.setCity("Mock City");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setCountry("Mock Country");
        mockLocation.setAddress("Mock Address");

        mockJobPosting.setLocation(mockLocation);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));



        JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO = new JobPostingRequestRecruiterSpesificDTO();
        jobPostingRequestRecruiterSpesificDTO.setInternalJobId(mockJobPosting.getInternalJobId());
        jobPostingRequestRecruiterSpesificDTO.setReplacement(mockJobPosting.isReplacement());
        jobPostingRequestRecruiterSpesificDTO.setReplacementFor(mockJobPosting.getReplacementFor());
        jobPostingRequestRecruiterSpesificDTO.setInternalHrNote("Internal Mock HR note RecruiterSPesiifc");
        jobPostingRequestRecruiterSpesificDTO.setHiringManagerName("Mock HR manager name RecruiterSpeisfic");
        jobPostingRequestRecruiterSpesificDTO.setInternalPostingDate(LocalDate.now());




        when(jobPostingRepository.findById(mockJobPosting.getId())).thenReturn(Optional.of(mockJobPosting));


        ResponseEntity<ApiResponse> responseUpdateRecruiterSpesific = jobPostingServiceImpl.recruiterSpesificUpdate(jobPostingRequestRecruiterSpesificDTO,mockJobPosting.getId());
        JobPostingResponseRecruiterSpesificDTO responsee =(JobPostingResponseRecruiterSpesificDTO) responseUpdateRecruiterSpesific.getBody().getData();

        assertEquals(jobPostingRequestRecruiterSpesificDTO.getHiringManagerName(),responsee.getHiringManagerName());
        assertEquals(jobPostingRequestRecruiterSpesificDTO.getInternalJobId(),responsee.getInternalJobId());
        assertEquals(jobPostingRequestRecruiterSpesificDTO.getInternalHrNote(),responsee.getInternalHrNote());
        assertEquals(jobPostingRequestRecruiterSpesificDTO.getReplacementFor(),responsee.getReplacementFor());

    }

    @Test
    void  recruiterSpesificFetch() throws Exception{

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(13L);
        mockJobPosting.setInternalJobId(13L);
        mockJobPosting.setJobTitle("spesific fetch");
        mockJobPosting.setJobPostingDescription("spesific description");
        mockJobPosting.setInternalDescripton("spesific internal description");
        mockJobPosting.setJobTitle("spesific job title");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("Required Skill Spesific Fetch 1","Required Skill Spesific Fetch 2"));
        mockJobPosting.setDepartment("Spesific Fetch");
        mockJobPosting.setHiringManagerName(null);

        Location mockLocation = new Location();
        mockLocation.setOfficeDays("Wednesday, Friday");
        mockLocation.setCity("Mock City");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setCountry("Mock Country");
        mockLocation.setAddress("Mock Address");

        mockJobPosting.setLocation(mockLocation);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));

        when(jobPostingRepository.findById(mockJobPosting.getId())).thenReturn(Optional.of(mockJobPosting));


        ResponseEntity<ApiResponse> responseUpdateRecruiterSpesific = jobPostingServiceImpl.recruiterSpesificFetch(mockJobPosting.getId());
        JobPostingResponseRecruiterSpesificDTO responsee =(JobPostingResponseRecruiterSpesificDTO) responseUpdateRecruiterSpesific.getBody().getData();

        assertEquals(mockJobPosting.getInternalHrNote(),responsee.getInternalHrNote());
        assertEquals(mockJobPosting.getInternalPostingDate(),responsee.getInternalPostingDate());
        assertEquals(mockJobPosting.getInternalJobId(),responsee.getInternalJobId());
        assertEquals(OK,responseUpdateRecruiterSpesific.getStatusCode());




    }


    @Test
    void  jobPostingsExistById() throws Exception{

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(13L);
        mockJobPosting.setInternalJobId(13L);
        mockJobPosting.setJobTitle("exitsbyId");
        mockJobPosting.setJobPostingDescription("exitsbyId description");
        mockJobPosting.setInternalDescripton("exitsbyIdinternal description");
        mockJobPosting.setJobTitle("exitsbyIdjob title");
        mockJobPosting.setSalary(40000);
        mockJobPosting.setJobType(JobType.FULL_TIME);
        mockJobPosting.setRequiredSkillsList(List.of("Required Skill exitsbyId","Required Skill exitsbyId 2"));
        mockJobPosting.setDepartment("Spesific exitsbyId");
        mockJobPosting.setHiringManagerName(null);

        Location mockLocation = new Location();
        mockLocation.setOfficeDays("Wednesday, Friday");
        mockLocation.setCity("Mock City");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setCountry("Mock Country");
        mockLocation.setAddress("Mock Address");

        mockJobPosting.setLocation(mockLocation);
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));

        when(jobPostingRepository.existsById(mockJobPosting.getId())).thenReturn(true);


        Boolean existsOrNot = jobPostingServiceImpl.jobPostingExistsById(mockJobPosting.getId());


        assertEquals(true,existsOrNot);




    }

}
