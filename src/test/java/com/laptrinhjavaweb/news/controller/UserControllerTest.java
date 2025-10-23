package com.laptrinhjavaweb.news.controller;

import static org.mockito.ArgumentMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laptrinhjavaweb.news.dto.request.UserCreationRequest;
import com.laptrinhjavaweb.news.dto.response.UserResponse;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerTest {
    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driverClassname", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private MockMvc mockMvc;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private ArrayList<Long> roles;

    @BeforeEach
    void init() {
        roles = new ArrayList<>(List.of(46L, 47L));
        LocalDate dob = LocalDate.of(2003, 11, 17);
        request = UserCreationRequest.builder()
                .userName("john55")
                .phone("0352542203")
                .email("lhuy123hl@gmail.com")
                .fullName("Đàm Đức Huy")
                .password("123H123h123hh@")
                .dateOfBirth(dob)
                .roleCode(roles)
                .build();
        userResponse = UserResponse.builder()
                .id(12)
                .userName("john")
                .phone("0352542203")
                .email("lhuy123hl@gmail.com")
                .fullName("Đàm Đức Huy")
                .dateOfBirth(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.userName").value("john55"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.dateOfBirth").value("2003-11-17"));
    }

    @Test
    void createUser_invalidRequest_fail() throws Exception {
        // GIVEN
        request.setUserName("jo");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1003))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("username must be at least 3 characters"));
    }

    @Test
    void createUser_userExisted_fail() throws Exception {
        // GIVEN

        request.setUserName("john55");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1002))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("User existed"));
    }
}
