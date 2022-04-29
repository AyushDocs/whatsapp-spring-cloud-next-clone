package com.whatsapp.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;

import com.whatsapp.profile.models.Profile;
import com.whatsapp.profile.repository.ProfileRepository;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class IntegrationTest {
      @Autowired
      private TestRestTemplate restTemplate;
      @Autowired
      private ProfileRepository repo;

      private static final String USERNAME = "ayush";
      private static final String EMAIL = "a@g.com";
      private static final String IMG_URL = "http://localhost:8082/api/v1/images?email=" + EMAIL;

      @Test
      void should_fetch_all_users() throws Exception {
            repo.save(new Profile(EMAIL, USERNAME, IMG_URL));

            ResponseEntity<String> res = restTemplate
                        .exchange("/api/v1/profiles?text=ayu",
                                    GET,
                                    null,
                                    new ParameterizedTypeReference<String>() {
                                    });
            String result = res.getBody();
            assertNotNull(result);
            JSONObject response=new JSONObject(result);
            assertFalse(response.optBoolean("hasError"));
            JSONObject data = response.getJSONObject("data");
            JSONArray content = data.getJSONArray("content");
            JSONObject firstProfile = content.getJSONObject(0);
            assertEquals(EMAIL, firstProfile.optString("email"));
            assertEquals(IMG_URL, firstProfile.optString("img_url"));
            assertEquals(USERNAME, firstProfile.optString("username"));
            assertNotNull(firstProfile.optString("uuid"));
      }
}
