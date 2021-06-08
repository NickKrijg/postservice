package com.kwetter.postservice.models;

public class ProfanityResponse {
    private Boolean isProfane;

    public ProfanityResponse(Boolean isProfane) {
        this.isProfane = isProfane;
    }

    public Boolean getProfane() {
        return isProfane;
    }
}
