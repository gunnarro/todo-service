package org.gunnarro.microservice.todoservice;

import org.apache.hc.client5.http.utils.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.Serial;
import java.nio.charset.StandardCharsets;

public class Utility {
    public static HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {
            @Serial
            private static final long serialVersionUID = 1L;

            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
                set("Authorization", String.format("Basic %s", new String(encodedAuth)));
                set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                set("X-XSRF-TOKEN", "must be read from the response");
            }
        };
    }

    public static String encodeBasic(String username, String password) {
        return "Basic " + Base64.encodeBase64String(String.format("%s:%s", username, password).getBytes());
    }

}
