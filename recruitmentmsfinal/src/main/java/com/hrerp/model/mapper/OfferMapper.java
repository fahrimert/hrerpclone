package com.hrerp.model.mapper;

import com.hrerp.dto.CandidateResponseDTO;
import com.hrerp.dto.OfferDTOs.OfferResponseWhenGetOfferDTO;
import com.hrerp.model.Offer;
    import com.hrerp.model.enums.OfferStatus;
import com.hrerp.model.mapper.Dto.OfferRequestDTO;
import com.hrerp.model.mapper.Dto.OfferResponseDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OfferMapper {


    public Offer toMakeAnOffer(@Valid OfferRequestDTO offerRequestDTO) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");

        LocalDateTime dateTimeExpiryDate = LocalDateTime.parse(offerRequestDTO.getOfferExpiryDate(), formatter);
        LocalDateTime dateTimeStartedTime = LocalDateTime.parse(offerRequestDTO.getCandidateStartDate(), formatter);

        Offer offer =  Offer.builder()
                .candidateId(offerRequestDTO.getCandidateId())
                .internalJobId(offerRequestDTO.getInternalJobId())
                .jobPostingId(offerRequestDTO.getJobPostingId())
                .proposedSalary(offerRequestDTO.getProposedSalary())
                .offerStatus(OfferStatus.OFFER_PENDING)
                .offerExpiryDate(dateTimeExpiryDate)
                .candidateStartDate(dateTimeStartedTime)
                .createdAt(LocalDateTime.now())
                .build();
        return  offer;

    }
    public OfferResponseDTO fromOfferWhenCreateOffer(Offer offer, CandidateResponseDTO candidateResponseDTO){
        return  new OfferResponseDTO(
                candidateResponseDTO ,
                offer.getProposedSalary(),
                offer.getOfferExpiryDate(),
                LocalDateTime.now()
        );




    }

    public OfferResponseWhenGetOfferDTO fromOfferWhenGetOffer(Offer offer) {
        return new OfferResponseWhenGetOfferDTO(
                offer.getOfferStatus(),
                offer.getProposedSalary(),
                offer.getCounterOfferSalaryCandidate(),
                offer.getCounterOfferDemandsCandidate(),
                offer.getOfferExpiryDate(),
                LocalDateTime.now()
        );
    }
}
