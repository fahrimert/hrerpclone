package com.hrerp.candidatems;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrerp.candidatems.dto.ApiResponse;
import com.hrerp.candidatems.dto.CandidateRequestDTO;
import com.hrerp.candidatems.dto.CandidateResponseDTO;
import com.hrerp.candidatems.mapper.CandidateMapper;
import com.hrerp.candidatems.model.Applications;
import com.hrerp.candidatems.model.Candidate;
import com.hrerp.candidatems.model.Connections;
import com.hrerp.candidatems.repository.ApplicationRepository;
import com.hrerp.candidatems.repository.CandidateRepository;
import com.hrerp.candidatems.service.CandidateService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT ,
        properties = {
                "eureka.client.enabled=false",
                "spring.cloud.discovery.enabled=false"
        })
@Transactional
@AutoConfigureMockMvc
public class CandidateIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private  CandidateService candidateService;

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private  ObjectMapper objectMapper;
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateMapper candidateMapper;



    @Test
    @Transactional
    void shouldReturn200_whenFindAllCandidates() throws Exception{
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(4L);
        firstCandidate.setEmail("integrationTestCandidate1@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("integrationTestCandidatePhoneNumber")
                .linkedinUrl("integrationTestCandidatelinkedin")
                .instagramUrl("integrationTestCandidateInstagram")
                .facebookUrl("integrationTestCandidateFacebook")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName("integrationTestCandidateLastname");
        firstCandidate.setFirstName("integrationTestCandidateFirstname");
        LocalDateTime ldtFirstCandidate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCustom = ldtFirstCandidate.format(formatter);

        firstCandidate.setCreatedAt(formattedCustom);

        Candidate secondCandidate = new Candidate();
        secondCandidate.setId(1L);
        secondCandidate.setEmail("integrationTestCandidateEmailSecond@gmail.com");
        secondCandidate.setConnections(Connections.builder()
                .phoneNumber("integrationTestCandidatePhoneNumberSecond")
                .linkedinUrl("integrationTestCandidatelinkedinSecond")
                .instagramUrl("integrationTestCandidateInstagramSecond")
                .facebookUrl("integrationTestCandidateFacebookSecond")
                .build());
        secondCandidate.setCvUrl("https://example.com/cv1.pdf");
        secondCandidate.setLastName("integrationTestSecondCandidateLastName");
        secondCandidate.setFirstName("integrationTestSecondCandidateFirstname");
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatterSecondCandidate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCustomSecondCandidate = ldt.format(formatter);
        firstCandidate.setCreatedAt(formattedCustom);
        secondCandidate.setCreatedAt(formattedCustomSecondCandidate);
        candidateRepository.saveAndFlush(firstCandidate);
        candidateRepository.saveAndFlush(secondCandidate);



        ResponseEntity<ApiResponse> candidateList = candidateService.findAllCandidates();



        mockMvc.perform(get("/api/v1/candidates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Success")))
                .andExpect(jsonPath("$.data[*].firstName").value(Matchers.hasItem(containsString(firstCandidate.getFirstName()))))
                .andExpect(jsonPath("$.data[*].lastName").value(Matchers.hasItem(containsString(firstCandidate.getLastName()))))
                .andExpect(jsonPath("$.data[*].email").value(Matchers.hasItem(containsString(firstCandidate.getEmail()))))
                .andExpect(jsonPath("$.data[*].firstName").value(Matchers.hasItem(containsString(secondCandidate.getFirstName()))))
                .andExpect(jsonPath("$.data[*].lastName").value(Matchers.hasItem(containsString(secondCandidate.getLastName()))))
                .andExpect(jsonPath("$.data[*].email").value(Matchers.hasItem(containsString(secondCandidate.getEmail()))));
    }


    @Test
    @Transactional
    void shouldFilterNullValues_whenFindAllCandidates() throws Exception{
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(4L);
        firstCandidate.setEmail(null);
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("integrationTestCandidatePhoneNumber")
                .linkedinUrl("integrationTestCandidatelinkedin")
                .instagramUrl("integrationTestCandidateInstagram")
                .facebookUrl("integrationTestCandidateFacebook")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName(null);
        firstCandidate.setFirstName(null);
        LocalDateTime ldtFirstCandidate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCustom = ldtFirstCandidate.format(formatter);

        firstCandidate.setCreatedAt(formattedCustom);

        Candidate secondCandidate = new Candidate();
        secondCandidate.setId(1L);
        secondCandidate.setEmail("integrationTestCandidateEmailSecond@gmail.com");
        secondCandidate.setConnections(Connections.builder()
                .phoneNumber("integrationTestCandidatePhoneNumberSecond")
                .linkedinUrl("integrationTestCandidatelinkedinSecond")
                .instagramUrl("integrationTestCandidateInstagramSecond")
                .facebookUrl("integrationTestCandidateFacebookSecond")
                .build());
        secondCandidate.setCvUrl("https://example.com/cv1.pdf");
        secondCandidate.setLastName("integrationTestSecondCandidateLastName");
        secondCandidate.setFirstName("integrationTestSecondCandidateFirstname");
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatterSecondCandidate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCustomSecondCandidate = ldt.format(formatter);
        firstCandidate.setCreatedAt(formattedCustom);
        secondCandidate.setCreatedAt(formattedCustomSecondCandidate);
        candidateRepository.saveAndFlush(firstCandidate);
        candidateRepository.saveAndFlush(secondCandidate);



        ResponseEntity<ApiResponse> candidateList = candidateService.findAllCandidates();



        mockMvc.perform(get("/api/v1/candidates"))
                .andExpect(status().isOk());
 }


    @Test
    @Transactional
    void shouldReturn200_whenFindCandidateById() throws Exception {
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(4L);
        firstCandidate.setEmail("integrationTestCandidate1@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("integrationTestCandidatePhoneNumber")
                .linkedinUrl("integrationTestCandidatelinkedin")
                .instagramUrl("integrationTestCandidateInstagram")
                .facebookUrl("integrationTestCandidateFacebook")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName("integrationTestCandidateLastname");
        firstCandidate.setFirstName("integrationTestCandidateFirstname");
        LocalDateTime ldtFirstCandidate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCustom = ldtFirstCandidate.format(formatter);

        firstCandidate.setCreatedAt(formattedCustom);

        Candidate savedCandidate = candidateRepository.saveAndFlush(firstCandidate);
        Optional<Candidate> savedCandidateFromRepo = candidateRepository.findById(savedCandidate.getId());


        ResponseEntity<ApiResponse> savedCandidateOnRepo = candidateService.findCandidateById(firstCandidate.getId());

        mockMvc.perform(get("/api/v1/candidates/" + savedCandidateFromRepo.get().getId()))
                .andExpect(jsonPath("$.data.firstName").value(savedCandidateFromRepo.get().getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(savedCandidateFromRepo.get().getLastName()))
                .andExpect(jsonPath("$.data.email").value(savedCandidateFromRepo.get().getEmail()))
                .andExpect(status().isOk());

    }

    @Test
    @Transactional
    void shouldReturn200_whenCreateCandidate() throws Exception {
        CandidateRequestDTO candidateRequestDTO = new CandidateRequestDTO();
        candidateRequestDTO.setFirstName("integrationTestCandidateFirstname");
        candidateRequestDTO.setLastName("integrationTestCandidateLastname");
        candidateRequestDTO.setEmail("integrationTestCandidate312@gmail.com");
        candidateRequestDTO.setLinkedin_url("integrationTestCandidatelinkedin");
        candidateRequestDTO.setInstagram_url("integrationTestCandidateInstagram");
        candidateRequestDTO.setFacebook_url("integrationTestCandidateFacebook");
        candidateRequestDTO.setPhoneNumber("integrationTestCandidatePhoneNumber");
        candidateRequestDTO.setCvUrl("https://example.com/cv1.pdf");
        String requestBody = new ObjectMapper().writeValueAsString(candidateRequestDTO);



        mockMvc.perform(post("/api/v1/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.firstName").value(candidateRequestDTO.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(candidateRequestDTO.getLastName()))
                .andExpect(status().isOk());

    }


    @Test
    @Transactional
    void shouldReturn200_whenUpdateCandidate() throws Exception {
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(4L);
        firstCandidate.setEmail("integrationTestCandidate1@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("integrationTestCandidatePhoneNumber")
                .linkedinUrl("integrationTestCandidatelinkedin")
                .instagramUrl("integrationTestCandidateInstagram")
                .facebookUrl("integrationTestCandidateFacebook")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName("integrationTestCandidateLastname");
        firstCandidate.setFirstName("integrationTestCandidateFirstname");
        LocalDateTime ldtFirstCandidate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCustom = ldtFirstCandidate.format(formatter);

        firstCandidate.setCreatedAt(formattedCustom);

        CandidateRequestDTO candidateRequestDTO = new CandidateRequestDTO();
        candidateRequestDTO.setFirstName("updatedF");
        candidateRequestDTO.setLastName("updatedL");
        candidateRequestDTO.setEmail("updatedEmaiş");
        candidateRequestDTO.setLinkedin_url("updatedLinkedin");
        candidateRequestDTO.setInstagram_url("updatedInstagram");
        candidateRequestDTO.setFacebook_url("updatedFacebook");
        candidateRequestDTO.setPhoneNumber("UpdatedPhoneNumber");
        candidateRequestDTO.setCvUrl("UpdatedCvUrl");

        ResponseEntity<ApiResponse> savedCandidateOnRepo = candidateService.updateCandidateById(firstCandidate.getId(),candidateRequestDTO);
        String requestBody = new ObjectMapper().writeValueAsString(candidateRequestDTO);

        mockMvc.perform(put("/api/v1/candidates/{id}",firstCandidate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(jsonPath("$.data.firstName").value(candidateRequestDTO.getFirstName()))
                .andExpect(jsonPath("$.data.cvUrl").value(candidateRequestDTO.getCvUrl()))
                .andExpect(status().isOk());

    }
    @Test
    @Transactional
    void shouldHandleNotFoundUser_whenUpdateCandidate() throws Exception {

        CandidateRequestDTO candidateRequestDTO = new CandidateRequestDTO();
        candidateRequestDTO.setFirstName("updatedF");
        candidateRequestDTO.setLastName("updatedL");
        candidateRequestDTO.setEmail("updatedEmaiş");
        candidateRequestDTO.setLinkedin_url("updatedLinkedin");
        candidateRequestDTO.setInstagram_url("updatedInstagram");
        candidateRequestDTO.setFacebook_url("updatedFacebook");
        candidateRequestDTO.setPhoneNumber("UpdatedPhoneNumber");
        candidateRequestDTO.setCvUrl("UpdatedCvUrl");

        ResponseEntity<ApiResponse> savedCandidateOnRepo = candidateService.updateCandidateById(9999L,candidateRequestDTO);
        String requestBody = new ObjectMapper().writeValueAsString(candidateRequestDTO);

        mockMvc.perform(put("/api/v1/candidates/{id}",9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(jsonPath("$.message").value("Error"))
                .andExpect(jsonPath("$.errors[0]").value("No Candidate Found"))
                .andExpect(status().isConflict());

    }


    //bunun integrasyon testini yazamıyorum useri yarattıktan sonra direkt silemediği için
    @Test
    void shouldReturn200_whenDeletingCandidate() throws Exception {
        Candidate candidateGoingToBeDeleted = new Candidate();
        candidateGoingToBeDeleted.setEmail("integrationTestCandidate1@gmail.com");
        candidateGoingToBeDeleted.setConnections(Connections.builder()
                .phoneNumber("integrationTestCandidatePhoneNumber")
                .linkedinUrl("integrationTestCandidatelinkedin")
                .instagramUrl("integrationTestCandidateInstagram")
                .facebookUrl("integrationTestCandidateFacebook")
                .build());
        candidateGoingToBeDeleted.setCvUrl("https://example.com/cv1.pdf");
        candidateGoingToBeDeleted.setLastName("integrationTestCandidateLastname");
        candidateGoingToBeDeleted.setFirstName("integrationTestCandidateFirstname");
        LocalDateTime ldtFirstCandidate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCustom = ldtFirstCandidate.format(formatter);

        candidateGoingToBeDeleted.setCreatedAt(formattedCustom);

        Candidate savedCandidate = candidateRepository.saveAndFlush(candidateGoingToBeDeleted);

        mockMvc.perform(delete("/api/v1/candidates/{id}",savedCandidate.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").value("Delete candidate succesfully applied"));

        assertFalse(candidateRepository.existsById(savedCandidate.getId()));

    }

    @Test
    void shouldHandleInvalidId_whenDeletingCandidate() throws Exception {
        mockMvc.perform(delete("/api/v1/candidates/{id}","abcdefg"))
                .andExpect(status().isBadRequest());
    }





}
