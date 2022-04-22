package com.whatsapp.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import com.whatsapp.authentication.utils.JwtUtils;
import com.whatsapp.library.Response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.bind.MethodArgumentNotValidException;

@WebMvcTest(ApiController.class)
@ExtendWith(MockitoExtension.class)
class ApiControllerTest {
      @Autowired
      private ApiController controller;
      @MockBean
      private JwtUtils jwtUtils;

      @ParameterizedTest
      @EmptySource
      @ValueSource(strings = { " " })
      void should_not_get_user_roles_invalid_jwt(String jwt) throws Exception {
            Executable action = () -> controller.getRoles(jwt);

            assertThrows(IllegalArgumentException.class, action);
      }

      @Test
      void should_get_1_user_role() throws Exception {
            // arrange
            when(jwtUtils.extractRoles("secret")).thenReturn(new String[] { "USER" });
            // act
            Response<String[]> response = controller.getRoles("secret");
            String[] roles=response.getData();
            //assert
            assertTrue(Arrays.asList(roles).contains("USER"));
      }

      @Test
      void should_get_all_user_role() throws Exception {
            // arrange
            when(jwtUtils.extractRoles("secret")).thenReturn(new String[] { "USER", "ADMIN" });
            // act
            Response<String[]> response = controller.getRoles("secret");
            String[] roles = response.getData();
            // assert
            assertTrue(Arrays.asList(roles).contains("USER"));
            assertTrue(Arrays.asList(roles).contains("ADMIN"));
      }
}
