package com.hrerp.dto.OfferDTOs;

import com.hrerp.model.enums.OfferStatus;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CounterOfferDTO {
    private OfferStatus offerStatus;
    @Nullable
    private  Double counterOfferSalary;
    @Nullable
    private  String counterOfferDemands;
    private  String role;

}
