package com.student;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.auth.LoginRequest;
import com.student.auth.RegisterRequest;
import com.student.department.Department;
import com.student.department.DepartmentRepository;
import com.student.department.DepartmentRequest;
import com.student.refreshtoken.*;
import com.student.student.Student;
import com.student.student.StudentRepository;
import com.student.student.StudentStatus;
import com.student.user.Role;
import com.student.user.User;
import com.student.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.student.student.StudentRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void cleanDatabase() {
        refreshTokenRepository.deleteAll();
        studentRepository.deleteAll();
        departmentRepository.deleteAll();
        userRepository.deleteAll();
    }

    private String getAdminToken() throws Exception {

        User admin = new User();

        admin.setName("Admin");
        admin.setEmail("admin@mail.com");
        admin.setPassword(
                passwordEncoder.encode("password123")
        );
        admin.setRole(Role.ADMIN);

        userRepository.save(admin);

        LoginRequest loginRequest =
                new LoginRequest();

        loginRequest.setEmail("admin@mail.com");
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
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        return objectMapper.readTree(loginResponse)
                .get("data")
                .get("accessToken")
                .asText();
    }

    private String getUserToken() throws Exception {

        RegisterRequest request =
                new RegisterRequest();

        request.setName("User");
        request.setEmail("user@mail.com");
        request.setPassword("password123");

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        request
                                )
                        )
        );

        LoginRequest loginRequest =
                new LoginRequest();

        loginRequest.setEmail("user@mail.com");
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
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        return objectMapper.readTree(loginResponse)
                .get("data")
                .get("accessToken")
                .asText();
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

    @Test
    void shouldGraduateStudentAsAdmin() throws Exception {

        String accessToken = getAdminToken();

        Student student = new Student();

        student.setName("Lazar");
        student.setEmail("student@mail.com");
        student.setAge(20);
        student.setStatus(StudentStatus.ACTIVE);

        studentRepository.save(student);

        mockMvc.perform(
                        patch(
                                "/api/students/" +
                                        student.getId() +
                                        "/graduate"
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + accessToken
                                )
                )
                .andExpect(status().isOk());

        Student updatedStudent =
                studentRepository.findById(
                        student.getId()
                ).orElseThrow();

        assertEquals(
                StudentStatus.GRADUATED,
                updatedStudent.getStatus()
        );
    }

    @Test
    void shouldReturnForbiddenWhenUserTriesToGraduateStudent()
            throws Exception {

        RegisterRequest request =
                new RegisterRequest();

        request.setName("User");
        request.setEmail("user@mail.com");
        request.setPassword("password123");

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        request
                                )
                        )
        );

        LoginRequest loginRequest =
                new LoginRequest();

        loginRequest.setEmail("user@mail.com");
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
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        String token =
                objectMapper.readTree(loginResponse)
                        .get("data")
                        .get("accessToken")
                        .asText();

        Student student = new Student();

        student.setName("Student");
        student.setEmail("student@mail.com");
        student.setAge(20);
        student.setStatus(StudentStatus.ACTIVE);

        studentRepository.save(student);

        mockMvc.perform(
                        patch(
                                "/api/students/" +
                                        student.getId() +
                                        "/graduate"
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnBadRequestWhenStudentAlreadyGraduated()
            throws Exception {

        String adminToken = getAdminToken();

        Student student = new Student();

        student.setName("Lazar");
        student.setEmail("lazar@mail.com");
        student.setAge(20);
        student.setStatus(StudentStatus.GRADUATED);

        studentRepository.save(student);

        mockMvc.perform(
                        patch(
                                "/api/students/" +
                                        student.getId() +
                                        "/graduate"
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + adminToken
                                )
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundWhenStudentDoesNotExist()
            throws Exception {

        String adminToken = getAdminToken();

        mockMvc.perform(
                        patch(
                                "/api/students/999/graduate"
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + adminToken
                                )
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateStudentWithActiveStatus()
            throws Exception {

        String token = getAdminToken();

        StudentRequest request =
                new StudentRequest();

        request.setName("Lazar");
        request.setEmail("lazar@mail.com");
        request.setAge(20);

        mockMvc.perform(
                        post("/api/students")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated());

        Student saved =
                studentRepository.findByEmail(
                        "lazar@mail.com"
                ).orElseThrow();

        assertEquals(
                StudentStatus.ACTIVE,
                saved.getStatus()
        );
    }

    @Test
    void shouldCreateDepartmentAsAdmin() throws Exception {

        String accessToken =
                getAdminToken();

        DepartmentRequest request =
                new DepartmentRequest();

        request.setName(
                "Software Engineering"
        );

        request.setCode(
                "SE"
        );

        mockMvc.perform(
                        post("/api/departments")
                                .header(
                                        "Authorization",
                                        "Bearer " + accessToken
                                )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated());

        assertTrue(
                departmentRepository
                        .findByCode("SE")
                        .isPresent()
        );
    }

    @Test
    void shouldRejectDepartmentCreationForUser()
            throws Exception {

        String accessToken =
                getUserToken();

        DepartmentRequest request =
                new DepartmentRequest();

        request.setName(
                "Software Engineering"
        );

        request.setCode(
                "SE"
        );

        mockMvc.perform(
                        post("/api/departments")
                                .header(
                                        "Authorization",
                                        "Bearer " + accessToken
                                )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldGetDepartmentById()
            throws Exception {

        String accessToken =
                getUserToken();

        Department department =
                new Department();

        department.setName(
                "Software Engineering"
        );

        department.setCode(
                "SE"
        );

        departmentRepository.save(
                department
        );

        mockMvc.perform(
                        get(
                                "/api/departments/" +
                                        department.getId()
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + accessToken
                                )
                )
                .andExpect(status().isOk());
    }





}