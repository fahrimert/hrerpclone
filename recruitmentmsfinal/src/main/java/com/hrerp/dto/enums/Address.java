package com.hrerp.dto.enums;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Address {
    private  String city;
    private String country;
    private  String address;
}
