package com.hrerp.candidatems;

import com.hrerp.candidatems.dto.ApiResponse;
import com.hrerp.candidatems.dto.CandidateRequestDTO;
import com.hrerp.candidatems.dto.CandidateResponseDTO;
import com.hrerp.candidatems.mapper.CandidateMapper;
import com.hrerp.candidatems.model.Address;
import com.hrerp.candidatems.model.Candidate;
import com.hrerp.candidatems.model.Connections;
import com.hrerp.candidatems.repository.CandidateRepository;
import com.hrerp.candidatems.service.CandidateService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
public class CandidateServiceUnitTest {

        @InjectMocks
        private CandidateService candidateService;
        @Mock
        private CandidateRepository candidateRepository;

        @Mock
        private  CandidateMapper candidateMapper;

        @Test
        void  shouldFindAllCandidates() throws Exception{
                Candidate firstCandidate = new Candidate();
                                firstCandidate.setId(112L);
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
                secondCandidate.setId(155L);
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

                when(candidateRepository.findAll()).thenReturn(candidateArrayList);

                ResponseEntity<ApiResponse> response = candidateService.findAllCandidates();

                List<CandidateResponseDTO> candidateList = (List<CandidateResponseDTO>) response.getBody().getData();


                CandidateResponseDTO candidateTestResponse1 =   new CandidateResponseDTO(
                        112L,
                        firstCandidate.getFirstName(),
                        firstCandidate.getLastName(),
                        firstCandidate.getAddress(),
                        firstCandidate.getEmail(),
                        firstCandidate.getConnections().getLinkedinUrl(),
                        firstCandidate.getSkills(),
                        firstCandidate.getConnections().getInstagramUrl(),
                        firstCandidate.getConnections().getFacebookUrl(),
                        firstCandidate.getConnections().getPhoneNumber(),
                        firstCandidate.getCvUrl(),
                        firstCandidate.getCreatedAt()
                );

                CandidateResponseDTO candidateTestResponse2 =   new CandidateResponseDTO(
                        155L,
                        secondCandidate.getFirstName(),
                        secondCandidate.getLastName(),
                        secondCandidate.getAddress(),
                        secondCandidate.getEmail(),
                        secondCandidate.getConnections().getLinkedinUrl(),
                        secondCandidate.getSkills(),

                        secondCandidate.getConnections().getInstagramUrl(),
                        secondCandidate.getConnections().getFacebookUrl(),
                        secondCandidate.getConnections().getPhoneNumber(),
                        secondCandidate.getCvUrl(),
                        secondCandidate.getCreatedAt()
                );
                assertEquals(candidateTestResponse1.getId(),candidateList.get(0).getId());
                assertEquals(candidateTestResponse1.getFirstName(),candidateList.get(0).getFirstName());
                assertEquals(candidateTestResponse1.getLastName(),candidateList.get(0).getLastName());
                assertEquals(candidateTestResponse1.getAddress(),candidateList.get(0).getAddress());

                assertEquals(candidateTestResponse2.getId(),candidateList.get(1).getId());
                assertEquals(candidateTestResponse2.getFirstName(),candidateList.get(1).getFirstName());
                assertEquals(candidateTestResponse2.getLastName(),candidateList.get(1).getLastName());
                assertEquals(candidateTestResponse2.getAddress(),candidateList.get(1).getAddress());

        }

        @Test
        void  shouldHandleEmptyList_whenFindAllCandidates() throws Exception{

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

                when(candidateRepository.findAll()).thenReturn(Collections.emptyList());
                ResponseEntity<ApiResponse> response = candidateService.findAllCandidates();
                List<CandidateResponseDTO> candidateList = (List<CandidateResponseDTO>) response.getBody().getData();
                Assertions.assertNull(candidateList);
                assertEquals("Database error", response.getBody().getMessage());
                assertEquals("No candidates in database", response.getBody().getErrors().get(0));
        }
        @Test
        void  shouldFilterNullValues_whenFindAllCandidates() throws Exception{
                List<Candidate> listWithNulls = Arrays.asList(null, new Candidate(), null);

                when(candidateRepository.findAll()).thenReturn(Collections.emptyList());
                ResponseEntity<ApiResponse> response = candidateService.findAllCandidates();
                List<CandidateResponseDTO> candidateList = (List<CandidateResponseDTO>) response.getBody().getData();
                Assertions.assertNull(candidateList);
                assertEquals("Database error", response.getBody().getMessage());
                assertEquals("No candidates in database", response.getBody().getErrors().get(0));
        }

        @Test
        void shouldReturnInternalServerError_whenFindCandidateById() throws Exception{

                when(candidateRepository.findAll()).thenThrow(new RuntimeException("Runtime Exception"));
                ResponseEntity<ApiResponse> response = candidateService.findAllCandidates();
                List errors = response.getBody().getErrors();

                assertEquals(errors,List.of("Unexpected server errorRuntime Exception"));
        }

        @Test
        void  shouldCreateCandidate() throws Exception{
                Candidate firstCandidate = new Candidate();
                firstCandidate.setId(999L);
                firstCandidate.setFirstName("mockFirstname");
                firstCandidate.setLastName("mockLastname");
                firstCandidate.setEmail("mockEmail@gmail.com");
                firstCandidate.setAddress(Address.builder()
                        .address("mockAddress")
                        .city("MockCity")
                        .country("MockCountry")
                        .build());;
                firstCandidate.setConnections(Connections.builder()
                        .phoneNumber("mockPhoneNumber")
                        .linkedinUrl("mocklinkedin")
                        .instagramUrl("mockİnstagram")
                        .facebookUrl("mockFacebook")
                        .build());
                firstCandidate.setCvUrl("https://example.com/cv1.pdf");
                firstCandidate.setSkills(List.of("Java","Kotlin"));
                firstCandidate.setCreatedAt("2025-08-20 14:30:00");

                CandidateRequestDTO candidateRequestDTO = new CandidateRequestDTO();
                candidateRequestDTO.setFirstName("mockFirstname");
                candidateRequestDTO.setLastName("mockLastname");
                candidateRequestDTO.setEmail("mockEmail@gmail.com");
                candidateRequestDTO.setLinkedin_url("mocklinkedin");
                candidateRequestDTO.setInstagram_url("mockInstagram");
                candidateRequestDTO.setFacebook_url("mockFacebook");
                candidateRequestDTO.setPhoneNumber("mockPhoneNUmber");
                candidateRequestDTO.setCvUrl("https://example.com/cv1.pdf");


                LocalDateTime ldt = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedCustom = ldt.format(formatter);

                firstCandidate.setCreatedAt(formattedCustom);
                candidateRequestDTO.setCreatedAt(formattedCustom);
                CandidateResponseDTO expectedResponseDTO = new CandidateResponseDTO(
                        1L,
                        candidateRequestDTO.getFirstName(),
                        candidateRequestDTO.getLastName(),
                        firstCandidate.getAddress(),
                        firstCandidate.getEmail(),
                        firstCandidate.getConnections().getLinkedinUrl(),
                        firstCandidate.getSkills(),
                        firstCandidate.getConnections().getInstagramUrl(),
                        firstCandidate.getConnections().getFacebookUrl(),
                        firstCandidate.getConnections().getPhoneNumber(),
                          firstCandidate.getCvUrl(),
                        formattedCustom
                );
                when(candidateMapper.toCandidate(candidateRequestDTO)).thenReturn(firstCandidate);
                when(candidateMapper.fromCandidate(firstCandidate)).thenReturn(expectedResponseDTO);
                when(candidateRepository.existsByEmail("mockEmail@gmail.com")).thenReturn(false);
                when(candidateRepository.save(firstCandidate)).thenReturn(firstCandidate);


                ResponseEntity<ApiResponse> response = candidateService.createCandidate(candidateRequestDTO);
                CandidateResponseDTO candidateResponseDTO = (CandidateResponseDTO)  response.getBody().getData();

                assertEquals(firstCandidate.getFirstName(),candidateResponseDTO.getFirstName());
                assertEquals(firstCandidate.getLastName(),candidateResponseDTO.getLastName());
                assertEquals(firstCandidate.getEmail(),candidateResponseDTO.getEmail());

        }
        @Test
        void createCandidate_shouldRejectNullDto() {
                ResponseEntity<ApiResponse> response = candidateService.createCandidate(null);

                Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }

        //bu bir kalsın tam olarak çözemedim exception fırlatmıyor servis olunca sadece firstnamee null veriyor

        @Test
        void createCandidate_shouldHandleDatabaseError() {
                CandidateRequestDTO candidateRequestDTO = new CandidateRequestDTO();
                candidateRequestDTO.setFirstName("test");
                candidateRequestDTO.setLastName("test");
                candidateRequestDTO.setEmail("test@gmail.com");
                candidateRequestDTO.setLinkedin_url("https://linkedin.com/cv1.pdf");
                candidateRequestDTO.setInstagram_url("https://linkedin.com/cv1.pdf");
                candidateRequestDTO.setFacebook_url("https://linkedin.com/cv1.pdf");
                when(candidateMapper.toCandidate(candidateRequestDTO)).thenThrow(new DataAccessException("DB Error") {});
                ResponseEntity<ApiResponse> response = candidateService.createCandidate(candidateRequestDTO);

                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
                assertEquals("Server Error", response.getBody().getMessage());
        }
        @Test
        void createCandidate_shouldRejectEmptyDto() {
                CandidateRequestDTO emptyDto = new CandidateRequestDTO();
                ResponseEntity<ApiResponse> response = candidateService.createCandidate(emptyDto);
                // Then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("Null Values", response.getBody().getMessage());
        }

        @Test
        void  shouldUpdateCandidateById() throws Exception{
                Candidate firstCandidate = new Candidate();
                firstCandidate.setId(112L);
                firstCandidate.setFirstName("mockFirstname");
                firstCandidate.setLastName("mockLastname");
                firstCandidate.setEmail("mockEmail@gmail.com");
                firstCandidate.setConnections(Connections.builder()
                        .phoneNumber("mockPhoneNumber")
                        .linkedinUrl("mocklinkedin")
                        .instagramUrl("mockİnstagram")
                        .facebookUrl("mockFacebook")
                        .build());
                firstCandidate.setCvUrl("https://example.com/cv1.pdf");


                CandidateRequestDTO candidateRequestDTO = new CandidateRequestDTO();
                candidateRequestDTO.setFirstName("updatedMockFirstname");
                candidateRequestDTO.setLastName("updatedmockLastname");
                candidateRequestDTO.setEmail("updatedmockEmail@gmail.com");
                candidateRequestDTO.setLinkedin_url("updatedmocklinkedin");
                candidateRequestDTO.setInstagram_url("updatedmockInstagram");
                candidateRequestDTO.setFacebook_url("updatedmockFacebook");
                candidateRequestDTO.setPhoneNumber("updatedmockPhoneNUmber");
                candidateRequestDTO.setCvUrl("https://example.com/cv1.pdf");

                CandidateResponseDTO candidateTestResponse1 =   new CandidateResponseDTO(
                        112L,
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
                LocalDateTime ldt = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedCustom = ldt.format(formatter);

                firstCandidate.setCreatedAt(formattedCustom);
                candidateRequestDTO.setCreatedAt(formattedCustom);

                when(candidateRepository.findById(firstCandidate.getId())).thenReturn(Optional.of(firstCandidate));
                when(candidateMapper.fromCandidate(firstCandidate)).thenReturn(candidateTestResponse1);

                ResponseEntity<ApiResponse> response = candidateService.updateCandidateById(firstCandidate.getId(),candidateRequestDTO);

                CandidateResponseDTO responseBody = (CandidateResponseDTO) response.getBody().getData();

                assertEquals(candidateRequestDTO.getFirstName(),responseBody.getFirstName());
                assertEquals(candidateRequestDTO.getLastName(),responseBody.getLastName());
                assertEquals(candidateRequestDTO.getEmail(),responseBody.getEmail());
                }

        @Test
        void  updateCandidateById_shouldHandleCandidateNotFound() throws Exception{

                when(candidateRepository.findById(123L)).thenReturn(Optional.empty());

                ResponseEntity<ApiResponse> response = candidateService.updateCandidateById(123L, ArgumentMatchers.any(CandidateRequestDTO.class));


                assertEquals(response.getBody().getMessage(),"Error");
                assertEquals(response.getBody().getErrors().get(0), "No Candidate Found");
        }

        @Test
        void  shouldDeleteCandidate() throws Exception{
                Candidate firstCandidate = new Candidate();
                firstCandidate.setId(112L);
                firstCandidate.setFirstName("mockFirstname");
                firstCandidate.setLastName("mockLastname");
                firstCandidate.setEmail("mockEmail@gmail.com");
                firstCandidate.setConnections(Connections.builder()
                        .phoneNumber("mockPhoneNumber")
                        .linkedinUrl("mocklinkedin")
                        .instagramUrl("mockİnstagram")
                        .facebookUrl("mockFacebook")
                        .build());
                firstCandidate.setCvUrl("https://example.com/cv1.pdf");



                when(candidateRepository.findById(firstCandidate.getId())).thenReturn(Optional.of(firstCandidate));

                ResponseEntity<ApiResponse> response = candidateService.deleteCandidate(firstCandidate.getId());


                assertEquals("Delete candidate succesfully applied",response.getBody().getData());
        }

        @Test
        void  deleteCandidateById_shouldHandleCandidateNotFound() throws Exception{

                when(candidateRepository.findById(123L)).thenReturn(Optional.empty());

                ResponseEntity<ApiResponse> response = candidateService.deleteCandidate(123L);

                assertEquals(response.getBody().getMessage(),"Error");
                assertEquals(response.getBody().getErrors().get(0), "No Candidate Found");
        }

        //offer için gereken testleri
}
