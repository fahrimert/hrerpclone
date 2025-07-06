package com.hrerp.jobposting.domain.model.valueObjects;

import org.springframework.util.Assert;

import java.util.UUID;

public record JobPostingId(UUID id) {

    public JobPostingId {
        Assert.notNull(id, "id must not be null");
    }

    public JobPostingId() {
        this(UUID.randomUUID());
    }
}