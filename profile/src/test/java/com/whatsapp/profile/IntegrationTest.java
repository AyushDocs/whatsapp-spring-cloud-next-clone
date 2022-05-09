package com.whatsapp.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.PUT;

import java.util.Map;

import com.whatsapp.library.Response;
import com.whatsapp.profile.controllers.ProfileController;
import com.whatsapp.profile.dto.AddImageUrlDto;
import com.whatsapp.profile.dto.FindUserDto;
import com.whatsapp.profile.dto.SaveUserDto;
import com.whatsapp.profile.repository.ProfileRepository;
import com.whatsapp.profile.service.ProfileService;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class IntegrationTest {
      @Autowired
      private ProfileController profileController;
      @Autowired
      private TestRestTemplate restTemplate;
      @Autowired
      private ProfileService profileService;
      @Autowired
      private ProfileRepository profileRepository;

      private static final String USERNAME = "ayush";
      private static final String EMAIL = "ayy@g.com";
      private static final String TEXT = "ayu";
      
      @BeforeEach
      void setup() throws Exception {
            profileRepository.deleteAll();
      }
      @Test
      void controller_should_be_present() throws Exception {
            assertNotNull(profileController);
            assertNotNull(profileService);
            assertNotNull(profileRepository);
      }

      @ParameterizedTest
      @EmptySource
      @ValueSource(strings = { " " })
      void should_not_fetch_all_users_invalid_text(String text) throws Exception {
            // act
            ResponseEntity<Void> result = restTemplate
                        .getForEntity("/api/v1/profiles/?text="+text+"&offset=0&limit=2",
                                    Void.class);
            // assert
            assertEquals(400, result.getStatusCodeValue());
      }

      @Test
      void should_fetch_all_friends() throws Exception {
            SaveUserDto saveUserDto = new SaveUserDto(EMAIL, USERNAME);
            restTemplate.postForEntity("/api/v1/profiles/", saveUserDto,Void.class);

            // arrange
            // act
            String url = "/api/v1/profiles/?text="+TEXT+"&offset=0&limit=2";
            ResponseEntity<String> res = restTemplate
            .getForEntity(url,String.class);

            // assert
            assertNotNull(res.getBody());
            JSONObject pageData = new JSONObject(res.getBody());
            JSONObject dataObj = pageData.getJSONObject("data").getJSONArray("content").getJSONObject(0);
            assertEquals(EMAIL, dataObj.getString("email"));
            assertEquals(USERNAME, dataObj.getString("username"));
            assertNotNull(dataObj.getString("uuid"));
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
      @SuppressWarnings({ "unchecked", "rawtypes" })
      void should_not_save_user_invalid_username_email(String text) throws Exception {
            // arrange
            SaveUserDto saveUserDto = new SaveUserDto(text, text);

            ResponseEntity<Response> res = restTemplate
                        .postForEntity("/api/v1/profiles", saveUserDto, Response.class);

            assertEquals(400, res.getStatusCodeValue());
            Map<String, String> map = (Map<String, String>) res.getBody().getData();
            assertNotNull(map.get("email"));
            assertNotNull(map.get("username"));
      }

      @Test
      void should_save_user() throws Exception {
            SaveUserDto saveUserDto = new SaveUserDto("a@g.com", "username");

            ResponseEntity<Void> res = restTemplate
                        .postForEntity("/api/v1/profiles", saveUserDto, Void.class);

            assertEquals(201, res.getStatusCodeValue());
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
      @SuppressWarnings({"unchecked","rawtypes"})
      void should_not_update_user_image_url_invalid_uuid(String text) throws Exception {
            AddImageUrlDto dto = new AddImageUrlDto(text, text);

            ResponseEntity<Response> res = restTemplate
                        .exchange("/api/v1/profiles/", PUT, new HttpEntity<AddImageUrlDto>(dto),
                                    Response.class);

            assertEquals(400, res.getStatusCodeValue());
            Map<String, String> data = (Map<String, String>) res.getBody().getData();
            assertNotNull(data.get("userUuid"));
            assertNotNull(data.get("imageUrl"));
      }

      @Test
      void should_update_user() throws Exception {
            AddImageUrlDto dto = new AddImageUrlDto("uuid", "http://a.com/api/upload");

            ResponseEntity<Void> res = restTemplate
                        .exchange("/api/v1/profiles/", PUT,
                                    new HttpEntity<AddImageUrlDto>(dto), Void.class);

            assertEquals(200, res.getStatusCodeValue());
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " ", "agcom", "ag.com" })
      void should_not_find_user_by_email(String text) throws Exception {
            ResponseEntity<Void> res = restTemplate.getForEntity("/api/v1/profiles/" + text, Void.class);

            assertEquals(400, res.getStatusCodeValue());
      }

      @Test
      void should_find_user_by_email() throws Exception {
            SaveUserDto saveUserDto = new SaveUserDto("a@g.com","username");
            restTemplate.postForObject("/api/v1/profiles/", saveUserDto, Void.class);

            ResponseEntity<FindUserDto> res = restTemplate
                        .getForEntity("/api/v1/profiles/" + "a@g.com",
                                    FindUserDto.class);

            assertEquals(200, res.getStatusCodeValue());
            FindUserDto dto=res.getBody();
            assertNotNull(dto);
            assertEquals("a@g.com",dto.getEmail());
            assertEquals("username",dto.getUsername());
            assertNull(dto.getImgUrl());
      }
}