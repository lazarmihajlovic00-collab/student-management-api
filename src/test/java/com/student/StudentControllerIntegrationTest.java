package com.student;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.auth.LoginRequest;
import com.student.auth.RegisterRequest;
import com.student.refreshtoken.*;
import com.student.student.StudentRepository;
import com.student.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void cleanDatabase() {

        refreshTokenRepository.deleteAll();
        studentRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnUnauthorizedWhenNoJwtProvided() throws Exception {

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRegisterNewUser() throws Exception {

        RegisterRequest request = new RegisterRequest();

        request.setName("Lazar");
        request.setEmail("lazar@mail.com");
        request.setPassword("password123");

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk());

        assertTrue(
                userRepository.findByEmail("lazar@mail.com")
                        .isPresent()
        );
    }

    @Test
    void shouldLoginAndAccessProtectedEndpoint() throws Exception {

        RegisterRequest registerRequest =
                new RegisterRequest();

        registerRequest.setName("Lazar");
        registerRequest.setEmail("lazar@mail.com");
        registerRequest.setPassword("password123");

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                registerRequest
                                        )
                                )
                )
                .andExpect(status().isOk());

        LoginRequest loginRequest =
                new LoginRequest();

        loginRequest.setEmail("lazar@mail.com");
        loginRequest.setPassword("password123");

        String loginResponse =
                mockMvc.perform(
                                post("/auth/login")
                                        .contentType(
                                                MediaType.APPLICATION_JSON
                                        )
                                        .content(
                                                objectMapper.writeValueAsString(
                                                        loginRequest
                                                )
                                        )
                        )
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        JsonNode jsonNode =
                objectMapper.readTree(loginResponse);

        String accessToken =
                jsonNode.get("data")
                        .get("accessToken")
                        .asText();

        mockMvc.perform(
                        get("/api/students")
                                .header(
                                        "Authorization",
                                        "Bearer " + accessToken
                                )
                )
                .andExpect(status().isOk());
    }
}