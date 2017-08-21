package com.weizilla.workouts.config;

import com.weizilla.garmin.configuration.Credentials;
import com.weizilla.garmin.configuration.UrlBases;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class GarminConfiguration {
    @Valid
    @NotNull
    private Credentials credentials;

    @Valid
    @NotNull
    private UrlBases urlBases;

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public UrlBases getUrlBases() {
        return urlBases;
    }

    public void setUrlBases(UrlBases urlBases) {
        this.urlBases = urlBases;
    }
}
