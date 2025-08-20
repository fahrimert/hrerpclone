package com.hrerp.candidatems;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hrerp.candidatems.controller.CandidateController;
import com.hrerp.candidatems.dto.ApiResponse;
import com.hrerp.candidatems.dto.CandidateRequestDTO;
import com.hrerp.candidatems.dto.CandidateResponseDTO;
import com.hrerp.candidatems.mapper.CandidateMapper;
import com.hrerp.candidatems.model.Address;
import com.hrerp.candidatems.model.Candidate;
import com.hrerp.candidatems.model.Connections;
import com.hrerp.candidatems.repository.CandidateRepository;
import com.hrerp.candidatems.service.CandidateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CandidateController.class)


public class CandidateControllerTest {

    private static final String BASE_URL = "/api/v1/candidates";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CandidateService candidateService;

    @MockitoBean
    private CandidateRepository candidateRepository;

    @MockitoBean
    private CandidateMapper candidateMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Configuration
    public class JacksonConfig {

        private final ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Test
    void shouldFindAllCandidates() throws Exception{
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(1L);
        firstCandidate.setEmail("mockEmail@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                        .phoneNumber("mockPhoneNumber")
                        .linkedinUrl("mocklinkedin")
                        .instagramUrl("mockİnstagram")
                        .facebookUrl("mockFacebook")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName("mockLastname");
        firstCandidate.setFirstName("mockFirstname");

        Candidate secondCandidate = new Candidate();
        secondCandidate.setId(1L);
        secondCandidate.setEmail("mockEmailSecond@gmail.com");
        secondCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneNumberSecond")
                .linkedinUrl("mocklinkedinSecond")
                .instagramUrl("mockİnstagramSecond")
                .facebookUrl("mockFacebookSecond")
                .build());
        secondCandidate.setCvUrl("https://example.com/cv1.pdf");
        secondCandidate.setLastName("mockLastname");
        secondCandidate.setFirstName("mockFirstname");

        ArrayList<Candidate> candidateArrayList = new ArrayList<>();

        candidateArrayList.add(firstCandidate);
        candidateArrayList.add(secondCandidate);
        when(candidateService.findAllCandidates()).thenReturn(
          ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(candidateArrayList)

        ));


        mockMvc.perform(get("/api/v1/candidates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data[0].firstName").value(firstCandidate.getFirstName()))
                .andExpect(jsonPath("$.data[0].lastName").value(firstCandidate.getLastName()))
                .andExpect(jsonPath("$.data[0].email").value(firstCandidate.getEmail()))
                .andExpect(jsonPath("$.data[1].firstName").value(secondCandidate.getFirstName()))
                .andExpect(jsonPath("$.data[1].lastName").value(secondCandidate.getLastName()))
                .andExpect(jsonPath("$.data[1].email").value(secondCandidate.getEmail()));
    }

    @Test
    void findAllCandidates_shouldHandleNoCandidates() throws Exception{

        when(candidateService.findAllCandidates()).thenReturn(
                ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(
                        "Database error",
                        List.of("No candidates in database"),
                        HttpStatus.CONFLICT
                )));

        mockMvc.perform(get("/api/v1/candidates"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andExpect(jsonPath("$.errors[0]").value("No candidates in database"))
                .andExpect(jsonPath("$.data").doesNotExist());

    }

    @Test
    void findAllCandidates_shouldHandleServerException() throws Exception{
        when(candidateService.findAllCandidates()).thenReturn(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        ApiResponse.error(
                                "Server Error",
                                List.of( "Unexpected server error"  ),
                                HttpStatus.INTERNAL_SERVER_ERROR
                        )
                ));

        mockMvc.perform(get("/api/v1/candidates"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors[0]").value("Unexpected server error"))
                .andExpect(jsonPath("$.message").value("Server Error"));

    }

    @Test
    void shouldFindCandidateById() throws Exception{
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(1L);
        firstCandidate.setEmail("mockEmail@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneNumber")
                .linkedinUrl("mocklinkedin")
                .instagramUrl("mockİnstagram")
                .facebookUrl("mockFacebook")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName("mockLastname");
        firstCandidate.setFirstName("mockFirstname");

        when(candidateService.findCandidateById(firstCandidate.getId())).thenReturn(
                ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(firstCandidate)

                ));


        mockMvc.perform(get("/api/v1/candidates/" + firstCandidate.getId() ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.firstName").value(firstCandidate.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(firstCandidate.getLastName()))
                .andExpect(jsonPath("$.data.connections.phoneNumber").value("mockPhoneNumber"))
                .andExpect(jsonPath("$.data.email").value(firstCandidate.getEmail()));

    }

    @Test
    void shouldReturnBadRequest_whenInvalidTypeId() throws Exception{
        mockMvc.perform(get("/api/v1/candidates/" + "abc" ))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenInvalidId() throws Exception{
        mockMvc.perform(get("/api/v1/candidates/" + "abc" ))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleNullId() throws Exception{
        mockMvc.perform(get("/api/v1/candidates/" ))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldReturnCandidateNotFound_whenCandidateDoesNotExists() throws Exception{

        when(candidateService.findCandidateById(1234L)).thenReturn(
                ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(
                        "Database error" ,
                        List.of("Candidate Not Found"),
                        HttpStatus.CONFLICT
                )));

        mockMvc.perform(get("/api/v1/candidates/" + 1234L ))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Database error"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[0]").value("Candidate Not Found"))
        ;
    }

    @Test
    void shouldReturnCandidateNotFound_whenFindCandidateById() throws Exception{

        when(candidateService.findCandidateById(103L)).thenReturn(  ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(
                "Database error" ,
                List.of("Candidate Not Found"),
                HttpStatus.CONFLICT
        )));


        mockMvc.perform(get("/api/v1/candidates/" + 103L ))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Database error"));

    }

    @Test
    void shouldReturnInternalServerError_whenFindCandidateById() throws Exception{

        when(candidateService.findCandidateById(103L)).thenReturn(  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error(
                        "Server Error",
                        List.of( "Unexpected server error"),
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
        ));

        mockMvc.perform(get("/api/v1/candidates/" + 103L ))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Server Error"));

    }

    @Test
    void shouldReturnTrue_whenCandidateExistsById() throws Exception{
        Candidate firstCandidate = new Candidate();
        firstCandidate.setId(1L);
        firstCandidate.setEmail("mockEmail@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneNumber")
                .linkedinUrl("mocklinkedin")
                .instagramUrl("mockİnstagram")
                .facebookUrl("mockFacebook")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName("mockLastname");
        firstCandidate.setFirstName("mockFirstname");

        when(candidateService.candidateExistsById(firstCandidate.getId())).thenReturn(true);


        mockMvc.perform(get("/api/v1/candidates/existsById/" + firstCandidate.getId() ))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

    }

    @Test
    void shouldReturnFalse_whenCandidateExistsById() throws Exception{
        when(candidateService.candidateExistsById(3541L)).thenReturn(false);

        mockMvc.perform(get("/api/v1/candidates/existsById/" +3541L))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

    }

    @Test
    void shouldCreateCandidate() throws Exception{
        Candidate firstCandidate = new Candidate();
        firstCandidate.setEmail("mockEmail@gmail.com");
        firstCandidate.setConnections(Connections.builder()
                .phoneNumber("mockPhoneNumber")
                .linkedinUrl("mocklinkedin")
                .instagramUrl("mockİnstagram")
                .facebookUrl("mockFacebook")
                .build());
        firstCandidate.setCvUrl("https://example.com/cv1.pdf");
        firstCandidate.setLastName("mockLastname");
        firstCandidate.setFirstName("mockFirstname");


        CandidateRequestDTO candidateRequestDTO = new CandidateRequestDTO();
        candidateRequestDTO.setFirstName("mockFirstname");
        candidateRequestDTO.setLastName("mockLastname");
        candidateRequestDTO.setAddress(Address.builder()
                .address("mockAddress")
                .city("MockCity")
                .country("MockCountry")
                .build());
        candidateRequestDTO.setEmail("mockEmail@gmail.com");
        candidateRequestDTO.setLinkedin_url("mocklinkedin");
        candidateRequestDTO.setInstagram_url("mockInstagram");
        candidateRequestDTO.setFacebook_url("mockFacebook");
        candidateRequestDTO.setPhoneNumber("mockPhoneNUmber");
        candidateRequestDTO.setCvUrl("https://example.com/cv1.pdf");


        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCustom = ldt.format(formatter);

        candidateRequestDTO.setCreatedAt(formattedCustom);
        firstCandidate.setCreatedAt(firstCandidate.getCreatedAt());


        when(candidateService.createCandidate(any(CandidateRequestDTO.class))).thenReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.success(candidateRequestDTO.getFirstName())

                ));

        String requestBody = new ObjectMapper().writeValueAsString(candidateRequestDTO);


        mockMvc.perform(post("/api/v1/candidates" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").value(candidateRequestDTO.getFirstName()))
                .andExpect(status().isAccepted());

    }

    @Test
    void shouldReturnBadRequest_whenRequiredFieldsIsMissing_whenCreateCandidate() throws Exception  {
        CandidateRequestDTO candidateRequestDTO = new CandidateRequestDTO();

        String requestBody = new ObjectMapper().writeValueAsString(candidateRequestDTO);
        when(candidateService.createCandidate(any(CandidateRequestDTO.class))).thenReturn(
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ApiResponse.error(
                                "Null Values",
                                List.of( "Required Fields Are Null"),
                                HttpStatus.BAD_REQUEST
                        )


                ));

        mockMvc.perform(post("/api/v1/candidates" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(jsonPath("$.errors.firstName").value("First name cannot be null"))
                .andExpect(jsonPath("$.errors.lastName").value("Last name cannot be null"))
                .andExpect(status().isNotFound());
    }



    @Test
    void shouldHandleNullValues_whenCreateCandidate() throws Exception  {
        CandidateRequestDTO candidateRequestDTO = new CandidateRequestDTO();
        candidateRequestDTO.setFirstName("mockFirstname");
        candidateRequestDTO.setLastName("mockLastname");
        candidateRequestDTO.setAddress(Address.builder()
                .address("mockAddress")
                .city("MockCity")
                .country("MockCountry")
                .build());
        candidateRequestDTO.setEmail("mockEmail@gmail.com");
        candidateRequestDTO.setLinkedin_url(null);
        candidateRequestDTO.setInstagram_url("mockInstagram");
        candidateRequestDTO.setFacebook_url("mockFacebook");
        candidateRequestDTO.setPhoneNumber("mockPhoneNUmber");
        candidateRequestDTO.setCvUrl(null);


        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCustom = ldt.format(formatter);

        candidateRequestDTO.setCreatedAt(formattedCustom);



        mockMvc.perform(post("/api/v1/candidates" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidateRequestDTO))
                )
                .andExpect(jsonPath("$.errors.cvUrl").value("must not be null"))
                .andExpect(jsonPath("$.errors.linkedin_url").value("must not be null"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateCandidate() throws Exception{


        CandidateRequestDTO candidateRequestDTO = new CandidateRequestDTO();

        candidateRequestDTO.setFirstName("mockFirstnameUpdated");
        candidateRequestDTO.setLastName("mockLastnameUpdated");
        candidateRequestDTO.setAddress(Address.builder()
                .address("mockAddressUpdated")
                .city("MockCityUpdated")
                .country("MockCountryUpdated")
                .build());
        candidateRequestDTO.setEmail("mockEmailUpdated@gmail.com");
        candidateRequestDTO.setLinkedin_url("mocklinkedinUpdated");
        candidateRequestDTO.setInstagram_url("mockInstagramUpdated");
        candidateRequestDTO.setFacebook_url("mockFacebookUpdated");
        candidateRequestDTO.setPhoneNumber("mockPhoneNUmberUpdated");
        candidateRequestDTO.setCvUrl("https://example.com/cv1.pdf");
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCustom = ldt.format(formatter);

        candidateRequestDTO.setCreatedAt(formattedCustom);


        CandidateResponseDTO candidateTestResponse =   new CandidateResponseDTO(
                31233L,
                candidateRequestDTO.getFirstName(),
                candidateRequestDTO.getLastName(),
                candidateRequestDTO.getAddress(),
                candidateRequestDTO.getEmail(),
                candidateRequestDTO.getLinkedin_url(),
                List.of("Java","Kotlin"),
                candidateRequestDTO.getInstagram_url(),
                candidateRequestDTO.getFacebook_url(),
                candidateRequestDTO.getPhoneNumber(),
                candidateRequestDTO.getCvUrl(),
                candidateRequestDTO.getCreatedAt()
        );

        when(candidateService.updateCandidateById(anyLong(),any())).thenReturn(
                ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(candidateTestResponse)));
        String requestBody = new ObjectMapper().writeValueAsString(candidateRequestDTO);


          mockMvc.perform(put("/api/v1/candidates/{id}",31233L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                  .andExpect(jsonPath("$.success").value(true))
                  .andExpect(jsonPath("$.data.firstName").value(candidateRequestDTO.getFirstName()))
                  .andExpect(jsonPath("$.data.lastName").value(candidateRequestDTO.getLastName()))
                  .andExpect(jsonPath("$.data.address.city").value(candidateRequestDTO.getAddress().getCity()))
          ;


    }

    @Test
    void shouldReturnCandidateNotFound_whenUpdateCandidate() throws Exception{

        CandidateRequestDTO candidateRequestDTO = new CandidateRequestDTO();

        when(candidateService.updateCandidateById(3123123L,candidateRequestDTO)).thenReturn(  ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        "Error",
                        List.of("No Candidate Found"),
                        HttpStatus.CONFLICT
                )));

        CandidateRequestDTO candidateRequestDTO1 = new CandidateRequestDTO();
        mockMvc.perform(put("/api/v1/candidates/" + 3123123L )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidateRequestDTO)))

                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors[0]").value(  "No Candidate Found"))
                .andExpect(jsonPath("$.message").value("Error"));

    }

    @Test
    void shouldReturnBadRequest_whenUpdateCanidate() throws Exception{
        mockMvc.perform(put("/api/v1/candidates/" + "abc" ))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteCandidate() throws Exception{

        when(candidateService.deleteCandidate(anyLong())).thenReturn( ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.success(
                        "Delete candidate succesfully applied"
                )));

        mockMvc.perform(delete("/api/v1/candidates/{id}",30))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").value("Delete candidate succesfully applied"))
                .andExpect(status().isConflict() )

        ;


    }


    @Test
    void shouldReturnCandidateNotFound_whenDeleteCandidate() throws Exception{


        when(candidateService.deleteCandidate(103L)).thenReturn( ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        "Candidate does not exists",
                        null,
                        HttpStatus.CONFLICT
                )) );

        mockMvc.perform(delete("/api/v1/candidates/" + 103L ))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.message").value("Candidate does not exists"));

    }

    @Test
    void shouldHandleInvalidUrl_whenDeleteCandidate() throws Exception{
        mockMvc.perform(delete("/api/v1/candidates/" + "abc" ))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleNullUserDeleteCandidate() throws Exception{

        when(candidateService.deleteCandidate(anyLong())).thenReturn( ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.success(
                        "Delete candidate succesfully applied"
                )));

        mockMvc.perform(delete("/api/v1/candidates/{id}",30))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").value("Delete candidate succesfully applied"))
                .andExpect(status().isConflict() )
        ;


    }



}
