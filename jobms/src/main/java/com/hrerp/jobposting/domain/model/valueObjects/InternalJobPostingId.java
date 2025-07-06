package com.hrerp.jobposting.domain.model.valueObjects;

import org.springframework.util.Assert;

import java.util.UUID;

public record InternalJobPostingId(UUID uuid)  {
    public InternalJobPostingId {
        Assert.notNull(uuid, "internal id must not be null");
    }

    public InternalJobPostingId() {
        this(UUID.randomUUID());
    }
}

