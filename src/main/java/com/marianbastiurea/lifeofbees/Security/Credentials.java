package com.marianbastiurea.lifeofbees.Security;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Credentials {

    private Google google;

    public Google getGoogle() {
        return google;
    }

    public void setGoogle(Google google) {
        this.google = google;
    }

    public static class Google {
        private String client_id;
        private String client_secret;

        public String getClient_id() {
            return client_id;
        }
        public String getClient_secret() {
            return client_secret;
        }
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "google=" + google +
                '}';
    }

    public static class OpenWeatherMap {
        private String api_key;

        // Getters și Setters
    }

    public static class NOAA {
        private String api_key;

        // Getters și Setters
    }

    // Getters și Setters pentru `google`, `openweathermap`, `noaa`
}
