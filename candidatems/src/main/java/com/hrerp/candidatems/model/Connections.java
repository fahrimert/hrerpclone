package com.hrerp.candidatems.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.*;

@Embeddable

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Connections {

    private  String linkedinUrl;
    @Nullable
    private String instagramUrl;
    @Nullable
    private String facebookUrl;
    @Size(min = 10)
    private String phoneNumber;

}
