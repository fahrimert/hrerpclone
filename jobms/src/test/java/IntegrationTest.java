import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrerp.JobMsApplication;
import com.hrerp.jobposting.application.dto.*;
import com.hrerp.jobposting.application.dto.enums.JobStatus;
import com.hrerp.jobposting.application.dto.enums.JobType;
import com.hrerp.jobposting.application.dto.enums.Location;
import com.hrerp.jobposting.application.dto.enums.WorkType;
import com.hrerp.jobposting.application.service.JobPostingServiceImpl;
import com.hrerp.jobposting.infrastructure.Client.ApplicationsClient;
import com.hrerp.jobposting.infrastructure.persistence.JobPosting;
import com.hrerp.jobposting.infrastructure.repository.JobPostingRepository;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = JobMsApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
,    properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
}
)
@Transactional

@AutoConfigureMockMvc

public class IntegrationTest {
    @Autowired
    private JobPostingServiceImpl jobPostingService;

    @Autowired
    private JobPostingRepository jobPostingRepository;



    @Autowired
    private ApplicationsClient applicationsClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    void   shouldGetAllJobPostings() throws  Exception{

        ArrayList<String> requiredSkillList = new ArrayList<>();
        requiredSkillList.add("SkillRequired1");
        requiredSkillList.add("SkillRequired2");

        Location mockLocation  = new Location();
        mockLocation.setAddress("mockAddress");
        mockLocation.setCity("mockCity");
        mockLocation.setCountry("mockCountry");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setOfficeDays("Monday,Wednesday");


        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar4.getTime();


        JobPosting mockJobPosting = JobPosting.builder()
                .internalJobId(11L)
                .jobTitle("MockJobTitle")
                .jobPostingDescription("mockDescription ")
                .internalDescripton("mockInternalDescription")
                .salary(40000)
                .jobType(JobType.FULL_TIME)
                .requiredSkillsList(requiredSkillList)
                .department("mockDepartment")
                .hiringManagerName("mockHiringManagerName")
                .location(mockLocation)
                .postingStatus(JobStatus.LIVE)
                .isReplacement(false)
                .replacementFor(null)
                .internalHrNote("mockInternalHrNote")
                .jobPostingDeadline(tenDaysLater)
                .internalPostingDate(LocalDate.now())
                .jobPostingDate(new Date())
                .build();

        jobPostingRepository.save(mockJobPosting);


        mockMvc.perform(get("/api/v1/jobPostings" ))
                .andExpect(jsonPath("$[*].jobTitle", hasItem(mockJobPosting.getJobTitle())))
                .andExpect(jsonPath("$[*].jobDescription", hasItem(mockJobPosting.getJobPostingDescription())))
                .andExpect(jsonPath("$[*].salary", hasItem(mockJobPosting.getSalary())))
                .andExpect(jsonPath("$[*].salary", hasItem(mockJobPosting.getSalary())))
                .andExpect(jsonPath("$[*].jobType", hasItem("FULL_TIME")))
                .andExpect(jsonPath("$[*].department", hasItem(mockJobPosting.getDepartment())))
                .andExpect(jsonPath("$[*].location.city", hasItem(mockJobPosting.getLocation().getCity())))
                .andExpect(jsonPath("$[*].location.address", hasItem(mockJobPosting.getLocation().getAddress())))
                .andDo(print());


    }

    @Test
    @Transactional
    void   shouldGetJobById() throws  Exception{

        ArrayList<String> requiredSkillList = new ArrayList<>();
        requiredSkillList.add("SkillRequired1");
        requiredSkillList.add("SkillRequired2");

        Location mockLocation  = new Location();
        mockLocation.setAddress("mockAddress");
        mockLocation.setCity("mockCity");
        mockLocation.setCountry("mockCountry");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setOfficeDays("Monday,Wednesday");


        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar4.getTime();


        JobPosting mockJobPosting = JobPosting.builder()
                .internalJobId(11L)
                .jobTitle("MockJobTitle")
                .jobPostingDescription("mockDescription ")
                .internalDescripton("mockInternalDescription")
                .salary(40000)
                .jobType(JobType.FULL_TIME)
                .requiredSkillsList(requiredSkillList)
                .department("mockDepartment")
                .hiringManagerName("mockHiringManagerName")
                .location(mockLocation)
                .postingStatus(JobStatus.LIVE)
                .isReplacement(false)
                .replacementFor(null)
                .internalHrNote("mockInternalHrNote")
                .jobPostingDeadline(tenDaysLater)
                .internalPostingDate(LocalDate.now())
                .jobPostingDate(new Date())
                .build();

        jobPostingRepository.save(mockJobPosting);


        mockMvc.perform(get("/api/v1/jobPostings/" + mockJobPosting.getId()))
                .andExpect(jsonPath("$.data.jobTitle" ).value(mockJobPosting.getJobTitle()))
                .andExpect(jsonPath("$.data.jobDescription").value(mockJobPosting.getJobPostingDescription()))
                .andExpect(jsonPath("$.data.salary").value(mockJobPosting.getSalary()))
                .andExpect(jsonPath("$.data.jobType").value("FULL_TIME"))
                .andExpect(jsonPath("$.data.department").value(mockJobPosting.getDepartment()))
                .andExpect(jsonPath("$.data.location.city").value(mockJobPosting.getLocation().getCity()))
                .andExpect(jsonPath("$.data.location.address").value(mockJobPosting.getLocation().getAddress()))
                .andDo(print());


    }


    @Test
    @Transactional
    void   shouldCreateJobPosting() throws  Exception{
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar3.getTime();


        JobPostingRequestDTO jobPostingRequestDTO = new JobPostingRequestDTO();
        jobPostingRequestDTO.setJobTitle("Devops Engineer");
        jobPostingRequestDTO.setJobDescription("Proficiency on Aws,Kibana,Docker");
        jobPostingRequestDTO.setSalary(30000);
        jobPostingRequestDTO.setJobPostingDeadline(tenDaysLater);
        jobPostingRequestDTO.setJobPostingStatus(JobStatus.LIVE);
        jobPostingRequestDTO.setJobType(JobType.FULL_TIME);
        jobPostingRequestDTO.setRequiredSkillsList(List.of("Aws","Kibana","Docker"));


        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Monday,Wednesday");
        location.setWorkType(WorkType.HYBRID);


        jobPostingRequestDTO.setLocation(location);
        jobPostingRequestDTO.setDepartment("DevOps");

        String requestBody = new ObjectMapper().writeValueAsString(jobPostingRequestDTO);


        mockMvc.perform(post("/api/v1/jobPostings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").value(jobPostingRequestDTO.getJobTitle()))
                .andExpect(status().isOk())
                .andDo(print());


    }

    @Test
    @Transactional
    void   createJobPosting_shouldHandleNotValidScenearios() throws  Exception{
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar3.getTime();


        JobPostingRequestDTO jobPostingRequestDTO = new JobPostingRequestDTO();
        jobPostingRequestDTO.setJobTitle(null);
        jobPostingRequestDTO.setJobDescription("");
        jobPostingRequestDTO.setSalary(null);
        jobPostingRequestDTO.setJobPostingDeadline(null);
        jobPostingRequestDTO.setJobPostingStatus(null);
        jobPostingRequestDTO.setJobType(null);
        jobPostingRequestDTO.setRequiredSkillsList(null);




        jobPostingRequestDTO.setLocation(null);
        jobPostingRequestDTO.setDepartment(null);

        String requestBody = new ObjectMapper().writeValueAsString(jobPostingRequestDTO);


        mockMvc.perform(post("/api/v1/jobPostings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.errors.jobTitle").value("Job Title  should be present"))
                .andExpect(jsonPath("$.errors.jobDescription").value("Job Description should be present"))
                .andExpect(jsonPath("$.errors.location").value("Location should be present"))
                .andExpect(jsonPath("$.errors.department").value("Department should be present"))
                .andExpect(jsonPath("$.errors.jobType").value("Job Type should be present"))


                    .andDo(print());


    }

    @Test
    @Transactional
    void   createJobPosting_shouldHandleJobTitleMin3CharacterValidation() throws  Exception{

        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar3.getTime();


        JobPostingRequestDTO jobPostingRequestDTO = new JobPostingRequestDTO();
        jobPostingRequestDTO.setJobTitle("te");
        jobPostingRequestDTO.setJobDescription("Proficiency on Aws,Kibana,Docker");
        jobPostingRequestDTO.setSalary(30000);
        jobPostingRequestDTO.setJobPostingDeadline(tenDaysLater);
        jobPostingRequestDTO.setJobPostingStatus(JobStatus.LIVE);
        jobPostingRequestDTO.setJobType(JobType.FULL_TIME);
        jobPostingRequestDTO.setRequiredSkillsList(List.of("Aws","Kibana","Docker"));


        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Monday,Wednesday");
        location.setWorkType(WorkType.HYBRID);


        jobPostingRequestDTO.setLocation(location);
        jobPostingRequestDTO.setDepartment("DevOps");

        String requestBody = new ObjectMapper().writeValueAsString(jobPostingRequestDTO);


        mockMvc.perform(post("/api/v1/jobPostings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.errors.jobTitle").value("Title must be between 4 and 30 characters"))
                .andDo(print());


    }

    @Test
    @Transactional
    void   createJobPosting_shouldHandleJobTitleMax30CharacterValidation() throws  Exception{

        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar3.getTime();


        JobPostingRequestDTO jobPostingRequestDTO = new JobPostingRequestDTO();
        jobPostingRequestDTO.setJobTitle("TestMockJobRequestTestMockJobRequestTestMockJobRequest");
        jobPostingRequestDTO.setJobDescription("Proficiency on Aws,Kibana,Docker");
        jobPostingRequestDTO.setSalary(30000);
        jobPostingRequestDTO.setJobPostingDeadline(tenDaysLater);
        jobPostingRequestDTO.setJobPostingStatus(JobStatus.LIVE);
        jobPostingRequestDTO.setJobType(JobType.FULL_TIME);
        jobPostingRequestDTO.setRequiredSkillsList(List.of("Aws","Kibana","Docker"));


        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Monday,Wednesday");
        location.setWorkType(WorkType.HYBRID);


        jobPostingRequestDTO.setLocation(location);
        jobPostingRequestDTO.setDepartment("DevOps");

        String requestBody = new ObjectMapper().writeValueAsString(jobPostingRequestDTO);


        mockMvc.perform(post("/api/v1/jobPostings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.errors.jobTitle").value("Title must be between 4 and 30 characters"))
                .andDo(print());


    }

    @Test
    @Transactional
    void   createJobPosting_shouldHandleJobDescriptionMin4CharacterValidation() throws  Exception{

        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar3.getTime();


        JobPostingRequestDTO jobPostingRequestDTO = new JobPostingRequestDTO();
        jobPostingRequestDTO.setJobTitle("TestMockJobRequest");
        jobPostingRequestDTO.setJobDescription("Pro");
        jobPostingRequestDTO.setSalary(30000);
        jobPostingRequestDTO.setJobPostingDeadline(tenDaysLater);
        jobPostingRequestDTO.setJobPostingStatus(JobStatus.LIVE);
        jobPostingRequestDTO.setJobType(JobType.FULL_TIME);
        jobPostingRequestDTO.setRequiredSkillsList(List.of("Aws","Kibana","Docker"));


        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Monday,Wednesday");
        location.setWorkType(WorkType.HYBRID);


        jobPostingRequestDTO.setLocation(location);
        jobPostingRequestDTO.setDepartment("DevOps");

        String requestBody = new ObjectMapper().writeValueAsString(jobPostingRequestDTO);


        mockMvc.perform(post("/api/v1/jobPostings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.errors.jobDescription")
                        .value("Job Description must be between 6 and 100 characterss"))
                .andDo(print());


    }


    @Test
    @Transactional
    void   createJobPosting_shouldHandleJobDescriptionMax100CharacterValidation() throws  Exception{

        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar3.getTime();


        JobPostingRequestDTO jobPostingRequestDTO = new JobPostingRequestDTO();
        jobPostingRequestDTO.setJobTitle("TestMockJobRequest");
        jobPostingRequestDTO.setJobDescription("TestMockJobRequestDescriptionTestMockJobRequestDescriptionTestMockJobRequestDescriptionTestMockJobRequestDescriptionTestMockJobRequestDescriptionTestMockJobRequestDescriptionTestMockJobRequestDescriptionTestMockJobRequestDescriptionTestMockJobRequestDescriptionTestMockJobRequestDescriptionTestMockJobRequestDescriptionTestMockJobRequestDescriptionTestMockJobRequestDescriptionTestMockJobRequestDescription");
        jobPostingRequestDTO.setSalary(30000);
        jobPostingRequestDTO.setJobPostingDeadline(tenDaysLater);
        jobPostingRequestDTO.setJobPostingStatus(JobStatus.LIVE);
        jobPostingRequestDTO.setJobType(JobType.FULL_TIME);
        jobPostingRequestDTO.setRequiredSkillsList(List.of("Aws","Kibana","Docker"));


        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Monday,Wednesday");
        location.setWorkType(WorkType.HYBRID);


        jobPostingRequestDTO.setLocation(location);
        jobPostingRequestDTO.setDepartment("DevOps");

        String requestBody = new ObjectMapper().writeValueAsString(jobPostingRequestDTO);


        mockMvc.perform(post("/api/v1/jobPostings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.errors.jobDescription")
                        .value("Job Description must be between 6 and 100 characterss"))
                .andDo(print());


    }


    @Test
    @Transactional
    void   createJobPosting_shouldHandleSalaryMustBePositiveValidation() throws  Exception{

        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar3.getTime();


        JobPostingRequestDTO jobPostingRequestDTO = new JobPostingRequestDTO();
        jobPostingRequestDTO.setJobTitle("TestMockJobRequest");
        jobPostingRequestDTO.setJobDescription("TestMockJobReque");
        jobPostingRequestDTO.setSalary(-30000);
        jobPostingRequestDTO.setJobPostingDeadline(tenDaysLater);
        jobPostingRequestDTO.setJobPostingStatus(JobStatus.LIVE);
        jobPostingRequestDTO.setJobType(JobType.FULL_TIME);
        jobPostingRequestDTO.setRequiredSkillsList(List.of("Aws","Kibana","Docker"));


        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Monday,Wednesday");
        location.setWorkType(WorkType.HYBRID);


        jobPostingRequestDTO.setLocation(location);
        jobPostingRequestDTO.setDepartment("DevOps");

        String requestBody = new ObjectMapper().writeValueAsString(jobPostingRequestDTO);


        mockMvc.perform(post("/api/v1/jobPostings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.errors.salary")
                        .value("must be greater than 0"))
                .andDo(print());


    }

    @Test
    @Transactional
    void   shouldUpdateJobPosting() throws  Exception{

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
        JobPosting savedJobPosting =   jobPostingRepository.save(mockJobPosting);



        JobPostingRequestDTO jobPostingRequestDTO = new JobPostingRequestDTO();
        jobPostingRequestDTO.setJobTitle("UpdatedTestJobPosting");
        jobPostingRequestDTO.setJobDescription("UpdatedTestJobPostingDescription");
        jobPostingRequestDTO.setSalary(50000);
        jobPostingRequestDTO.setJobPostingDeadline(tenDaysLater);
        jobPostingRequestDTO.setJobPostingStatus(JobStatus.LIVE);
        jobPostingRequestDTO.setJobType(JobType.FULL_TIME);
        jobPostingRequestDTO.setRequiredSkillsList(List.of("Aws","Kubernetes","Kibana","Docker"));


        Location location = new Location();
        location.setCountry("Türkiye");
        location.setCity("Ankara");
        location.setAddress("Balgat");
        location.setOfficeDays("Monday,Wednesday");
        location.setWorkType(WorkType.HYBRID);


        jobPostingRequestDTO.setLocation(location);
        jobPostingRequestDTO.setDepartment("DevOps");

        String requestBody = new ObjectMapper().writeValueAsString(jobPostingRequestDTO);


        mockMvc.perform(put("/api/v1/jobPostings/" + savedJobPosting.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                    .andExpect(jsonPath("$.jobTitle").value(jobPostingRequestDTO.getJobTitle()))
                    .andExpect(jsonPath("$.jobDescription").value(jobPostingRequestDTO.getJobDescription()))
                    .andExpect(jsonPath("$.salary").value(jobPostingRequestDTO.getSalary()))
                .andDo(print());



    }

    @Test
    @Transactional
    void   shouldIncrementApplication() throws  Exception{

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
        JobPosting savedJobPosting =   jobPostingRepository.save(mockJobPosting);




        mockMvc.perform(put("/api/v1/jobPostings/" + savedJobPosting.getId() + "/incrementApplication"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Application Count Incremented"))
                .andDo(print());



    }

    @Test
    @Transactional
    void   shouldGetJobTitle() throws  Exception{

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
        JobPosting savedJobPosting =   jobPostingRepository.save(mockJobPosting);




        mockMvc.perform(get("/api/v1/jobPostings/" + savedJobPosting.getId() + "/getJobTitle"))
                .andExpect(status().isOk())
                .andExpect(content().string(mockJobPosting.getJobTitle()))
                .andDo(print());



    }

    @Test
    @Transactional
    void   shouldReturnJobDoesNotExists() throws  Exception{

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(15L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("mockJobPosting");


        JobPosting savedJobPosting =   jobPostingRepository.save(mockJobPosting);




        mockMvc.perform(get("/api/v1/jobPostings/" + 300 + "/getJobTitle"))
                         .andExpect(content().string("Job does not exists"))
                .andDo(print());



    }


    @Test
    @Transactional
    void   shouldGetApplicationList() throws  Exception{

        JobPosting mockJobPosting = new JobPosting();
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
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();

        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar4.getTime();

        Location mockLocation  = new Location();
        mockLocation.setAddress("mockAddress");
        mockLocation.setCity("mockCity");
        mockLocation.setCountry("mockCountry");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setOfficeDays("Monday,Wednesday");

        mockJobPosting.setJobPostingDate(tenDaysAgo);
        mockJobPosting.setJobPostingDeadline(tenDaysLater);
        mockJobPosting.setLocation(mockLocation);

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));
        JobPosting savedJobPosting =   jobPostingRepository.save(mockJobPosting);

        List<ApplicationListDTO> mockApplications = List.of(
                new ApplicationListDTO(10L,LocalDate.now(),15L,"mockCandidateFullname","mockCandidateEmail")
        );

        when(applicationsClient.getApplications(savedJobPosting.getId())).thenReturn(mockApplications);


        mockMvc.perform(get("/api/v1/jobPostings/" + savedJobPosting.getId() + "/getApplications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.jobTitle").value(mockJobPosting.getJobTitle()))
                .andExpect(jsonPath("$.data.candidateList[0].applicationId").value(mockApplications.get(0).getApplicationId()))
                .andExpect(jsonPath("$.data.candidateList[0].candidateId").value(mockApplications.get(0).getCandidateId()))
                .andExpect(jsonPath("$.data.candidateList[0].candidateFullName").value(mockApplications.get(0).getCandidateFullName()))
                .andDo(print());



    }

    @Test
    @Transactional
    void   shouldReturnJobPostingDoesNotexists_whenGetApplicationLis() throws  Exception{

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(15L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("mockJobPosting");



        when(applicationsClient.getApplications(mockJobPosting.getId())).thenReturn(null);


        mockMvc.perform(get("/api/v1/jobPostings/" + mockJobPosting.getId() + "/getApplications"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Job posting does not exists"))
                .andExpect(jsonPath("$.success").value(false))
                .andDo(print());



    }

    @Test
    @Transactional
    void   shouldReturnInternalServerError_whenGetApplicationLis() throws  Exception{

        JobPosting mockJobPosting = new JobPosting();
        mockJobPosting.setId(15L);
        mockJobPosting.setInternalJobId(15L);
        mockJobPosting.setJobTitle("mockJobPosting");

        Location mockLocation  = new Location();

        mockLocation.setOfficeDays("Wednesday, Friday");
        mockLocation.setCity("Mock City");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setCountry("Mock Country");
        mockLocation.setAddress("Mock Address");

        JobPosting savedJobPosting = jobPostingRepository.save(mockJobPosting);

        when(applicationsClient.getApplications(savedJobPosting.getId()))     .thenThrow(new RuntimeException("mock exception"))
        ;


            mockMvc.perform(get("/api/v1/jobPostings/" + savedJobPosting.getId() + "/getApplications"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(Matchers.containsString("Server Error")))
                .andExpect(jsonPath("$.success").value(false))
                .andDo(print());



    }

    @Test
    @Transactional
        void   shouldGetApplicationSingle() throws  Exception{

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
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();

        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar4.getTime();

        Location mockLocation  = new Location();
        mockLocation.setAddress("mockAddress");
        mockLocation.setCity("mockCity");
        mockLocation.setCountry("mockCountry");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setOfficeDays("Monday,Wednesday");

        mockJobPosting.setJobPostingDate(tenDaysAgo);
        mockJobPosting.setJobPostingDeadline(tenDaysLater);
        mockJobPosting.setLocation(mockLocation);

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));
        JobPosting savedJobPosting =   jobPostingRepository.save(mockJobPosting);

        ApplicationsDTO mockApplications =
                new ApplicationsDTO(10L,LocalDate.now(),15L,"mockCandidateFullname","mockCandidateEmail"
                ,"mockcover","Ankara","Türkiye","Mustafa Kemal Mah. 123. Sok. No:45"
                        ,"https://www.linkedin.com/in/mockuser",
                        "https://www.instagram.com/mockuser",
                        "https://www.facebook.com/mockuser",
                        "+90 531 123 45 67",
                        "https://example.com/cv/mockuser-cv.pdf"
                        );
        ApiResponse<ApplicationsDTO> mockApiResponse = ApiResponse.success(mockApplications);

        when(applicationsClient.getApplicationSingle(savedJobPosting.getId(), 15L))
                .thenReturn(mockApiResponse);

        mockMvc.perform(get("/api/v1/jobPostings/"  + savedJobPosting.getId() + "/getApplication/" + mockApplications   .getCandidateId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.jobTitle").value(mockJobPosting.getJobTitle()))
                .andExpect(jsonPath("$.data.candidate.applicationId").value(mockApplications.getApplicationId()))
                .andExpect(jsonPath("$.data.candidate.candidateId").value(mockApplications.getCandidateId()))
                .andExpect(jsonPath("$.data.candidate.candidateFullName").value(mockApplications.getCandidateFullName()))
                .andDo(print());



    }

    @Test
    @Transactional
    void   shouldRecruiterSpesificUpdate() throws  Exception{

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
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();

        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar4.getTime();

        Location mockLocation  = new Location();
        mockLocation.setAddress("mockAddress");
        mockLocation.setCity("mockCity");
        mockLocation.setCountry("mockCountry");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setOfficeDays("Monday,Wednesday");

        mockJobPosting.setJobPostingDate(tenDaysAgo);
        mockJobPosting.setJobPostingDeadline(tenDaysLater);
        mockJobPosting.setLocation(mockLocation);

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));
        JobPosting savedJobPosting =   jobPostingRepository.save(mockJobPosting);


        JobPostingRequestRecruiterSpesificDTO jobPostingRequestRecruiterSpesificDTO = new JobPostingRequestRecruiterSpesificDTO();
        jobPostingRequestRecruiterSpesificDTO.setInternalJobId(12L);
        jobPostingRequestRecruiterSpesificDTO.setReplacement(false);
        jobPostingRequestRecruiterSpesificDTO.setReplacementFor(null);
        jobPostingRequestRecruiterSpesificDTO.setInternalHrNote("Internal Mock HR note");
        jobPostingRequestRecruiterSpesificDTO.setHiringManagerName("Mock HR manager name");

        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDTO = new JobPostingResponseRecruiterSpesificDTO();
        jobPostingResponseRecruiterSpesificDTO.setInternalJobId(jobPostingRequestRecruiterSpesificDTO.getInternalJobId());
        jobPostingResponseRecruiterSpesificDTO.setReplacement(jobPostingRequestRecruiterSpesificDTO.isReplacement());
        jobPostingResponseRecruiterSpesificDTO.setInternalHrNote(jobPostingRequestRecruiterSpesificDTO.getInternalHrNote());
        jobPostingResponseRecruiterSpesificDTO.setHiringManagerName(jobPostingRequestRecruiterSpesificDTO.getHiringManagerName());
        jobPostingResponseRecruiterSpesificDTO.setReplacementFor(jobPostingRequestRecruiterSpesificDTO.getReplacementFor());

        String requestBody = new ObjectMapper().writeValueAsString(jobPostingRequestRecruiterSpesificDTO);

        mockMvc.perform(put("/api/v1/jobPostings/"  + savedJobPosting.getId() + "/recruiterSpesificUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.hiringManagerName").value(jobPostingRequestRecruiterSpesificDTO.getHiringManagerName()))
                .andExpect(jsonPath("$.data..internalHrNote").value(jobPostingRequestRecruiterSpesificDTO.getInternalHrNote()))
                .andExpect(jsonPath("$.data..internalJobId").value(12))

                .andDo(print());



    }

    @Test
    @Transactional
    void   shouldRecruiterSpesificFetch() throws  Exception{

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
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();

        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar4.getTime();

        Location mockLocation  = new Location();
        mockLocation.setAddress("mockAddress");
        mockLocation.setCity("mockCity");
        mockLocation.setCountry("mockCountry");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setOfficeDays("Monday,Wednesday");

        mockJobPosting.setJobPostingDate(tenDaysAgo);
        mockJobPosting.setJobPostingDeadline(tenDaysLater);
        mockJobPosting.setLocation(mockLocation);

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));
        JobPosting savedJobPosting =   jobPostingRepository.save(mockJobPosting);

        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDTO = new JobPostingResponseRecruiterSpesificDTO();
        jobPostingResponseRecruiterSpesificDTO.setInternalJobId(mockJobPosting.getInternalJobId());
        jobPostingResponseRecruiterSpesificDTO.setReplacement(mockJobPosting.isReplacement());
        jobPostingResponseRecruiterSpesificDTO.setInternalHrNote(mockJobPosting.getInternalHrNote());
        jobPostingResponseRecruiterSpesificDTO.setHiringManagerName(mockJobPosting.getHiringManagerName());
        jobPostingResponseRecruiterSpesificDTO.setReplacementFor(mockJobPosting.getReplacementFor());


        mockMvc.perform(get("/api/v1/jobPostings/internal/" +  savedJobPosting.getId() ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.internalHrNote").value(jobPostingResponseRecruiterSpesificDTO.getInternalHrNote()))
                .andExpect(jsonPath("$.data.internalJobId").value(jobPostingResponseRecruiterSpesificDTO.getInternalJobId()))
                .andExpect(jsonPath("$.data.replacement").value(jobPostingResponseRecruiterSpesificDTO.isReplacement()))
                .andDo(print());



    }


    @Test
    @Transactional
    void   recruiterSpesificFetch_shouldReturnJobPostingNotFound() throws  Exception{

        mockMvc.perform(get("/api/v1/jobPostings/internal/" +  3123124 ))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.data.message").value("Job Posting not found"))
                .andDo(print());



    }

    @Test
    @Transactional
    void   shouldJobPostingExistById() throws  Exception{

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
        mockJobPosting.setPostingStatus(JobStatus.LIVE);
        mockJobPosting.setReplacementFor(null);
        mockJobPosting.setInternalHrNote("mock hr note");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();

        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_YEAR, +10);
        Date tenDaysLater = calendar4.getTime();

        Location mockLocation  = new Location();
        mockLocation.setAddress("mockAddress");
        mockLocation.setCity("mockCity");
        mockLocation.setCountry("mockCountry");
        mockLocation.setWorkType(WorkType.HYBRID);
        mockLocation.setOfficeDays("Monday,Wednesday");

        mockJobPosting.setJobPostingDate(tenDaysAgo);
        mockJobPosting.setJobPostingDeadline(tenDaysLater);
        mockJobPosting.setLocation(mockLocation);

        mockJobPosting.setInternalPostingDate(LocalDate.now().minus(10, ChronoUnit.DAYS
        ));
        JobPosting savedJobPosting =   jobPostingRepository.save(mockJobPosting);

        JobPostingResponseRecruiterSpesificDTO jobPostingResponseRecruiterSpesificDTO = new JobPostingResponseRecruiterSpesificDTO();
        jobPostingResponseRecruiterSpesificDTO.setInternalJobId(mockJobPosting.getInternalJobId());
        jobPostingResponseRecruiterSpesificDTO.setReplacement(mockJobPosting.isReplacement());
        jobPostingResponseRecruiterSpesificDTO.setInternalHrNote(mockJobPosting.getInternalHrNote());
        jobPostingResponseRecruiterSpesificDTO.setHiringManagerName(mockJobPosting.getHiringManagerName());
        jobPostingResponseRecruiterSpesificDTO.setReplacementFor(mockJobPosting.getReplacementFor());


        mockMvc.perform(get("/api/v1/jobPostings/internal/" +  savedJobPosting.getId() ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.internalHrNote").value(jobPostingResponseRecruiterSpesificDTO.getInternalHrNote()))
                .andExpect(jsonPath("$.data.internalJobId").value(jobPostingResponseRecruiterSpesificDTO.getInternalJobId()))
                .andExpect(jsonPath("$.data.replacement").value(jobPostingResponseRecruiterSpesificDTO.isReplacement()))
                .andDo(print());



    }


}
