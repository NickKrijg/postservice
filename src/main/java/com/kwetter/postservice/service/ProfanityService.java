package com.kwetter.postservice.service;

import com.google.gson.Gson;
import com.kwetter.postservice.models.ProfanityResponse;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ProfanityService {
    private HttpClient client;

    public ProfanityService() {
        client = HttpClient.newHttpClient();
    }

    public Boolean isContentChildFriendly(String content) {
        String requestString = "{\"content\":\"" + content + "\"}";
        byte[] requestArray = requestString.getBytes();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://eu-gb.functions.appdomain.cloud/api/v1/web/n.krijgsman%40student.fontys.nl_dev/default/profanityChecker"))
                .setHeader("Accept", "application/json")
                .setHeader("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestArray))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response != null) {
            ProfanityResponse pr = null;
            try {
                Gson g = new Gson();
                pr = g.fromJson(response.body(), ProfanityResponse.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (pr != null) {
                return !pr.getProfane();
            }
        }
        return true;
    }


}
