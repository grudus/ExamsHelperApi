package com.grudus.examshelper.commons;

public class IdResponse {
    private final Long id;

    public IdResponse(Long id) {
        this.id = id;
    }

    @Deprecated //for jackson only
    public IdResponse() {
        this(null);
    }

    public Long getId() {
        return id;
    }
}
