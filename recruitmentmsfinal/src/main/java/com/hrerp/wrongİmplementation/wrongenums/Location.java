package com.hrerp.wrongİmplementation.wrongenums;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Location {

    private  String city;
    private String country;
    private  String address;
    private WorkType workType;
    private  String officeDays;
}
