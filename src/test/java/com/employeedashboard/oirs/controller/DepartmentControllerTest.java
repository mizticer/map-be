package com.employeedashboard.oirs.controller;

import com.employeedashboard.oirs.config.BaseIntegrationTest;
import com.employeedashboard.oirs.dto.auth.AuthRequest;
import com.employeedashboard.oirs.dto.auth.AuthResponse;
import com.employeedashboard.oirs.dto.department.DepartmentRequest;
import com.employeedashboard.oirs.dto.department.DepartmentResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class DepartmentControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc postman;
    @Autowired
    private ObjectMapper objectMapper;
    private String token;
    private String tokenRegular;

    @BeforeEach
    void signIn() throws Exception {
        AuthRequest authRequest = AuthRequest.builder().
                username("John@gmail.com")
                .password("admin")
                .build();
        String json = objectMapper.writeValueAsString(authRequest);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(content, AuthResponse.class);
        token = authResponse.getToken();

        AuthRequest authRequestRegular = AuthRequest.builder().
                username("Helena@gmail.com")
                .password("admin")
                .build();
        String jsonRegular = objectMapper.writeValueAsString(authRequestRegular);

        MvcResult resultRegular = postman.perform(MockMvcRequestBuilders.post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRegular))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String contentRegular = resultRegular.getResponse().getContentAsString();
        AuthResponse authResponseRegular = objectMapper.readValue(contentRegular, AuthResponse.class);
        tokenRegular = authResponseRegular.getToken();
    }

    @Test
    @DisplayName("Should return a list of department including IT and HR")
    void findAll_shouldReturnListOfDepartment() throws Exception {
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/department").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<DepartmentResponse> departmentResponseDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<DepartmentResponse>>() {
        });
        Assertions.assertEquals("IT", departmentResponseDtos.get(0).getName());
        Assertions.assertEquals("HR", departmentResponseDtos.get(1).getName());
    }

    @Test
    @DisplayName("Should successfully save a department with specified details")
    void save_shouldInsertNewDepartment_whenSavingValidDepartment() throws Exception {
        //Given
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .name("testName")
                .build();
        String json = objectMapper.writeValueAsString(departmentRequest);
        //When
        postman.perform(MockMvcRequestBuilders.post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());
        //Then
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/department").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<DepartmentResponse> departmentResponses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<DepartmentResponse>>() {
        });
        Assertions.assertEquals("testName", departmentResponses.get(4).getName());
    }

    @Test
    @DisplayName("Should not save a department when this squad exists")
    void save_shouldNotSaveDepartment_WhenDepartmentExists() throws Exception {
        //Given
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .name("IT")
                .build();
        String json = objectMapper.writeValueAsString(departmentRequest);
        //When
        postman.perform(MockMvcRequestBuilders.post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
        //Then
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/department").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<DepartmentResponse> departmentResponses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<DepartmentResponse>>() {
        });
        Assertions.assertEquals(4, departmentResponses.size());
    }

    @Test
    @DisplayName("Should not save an department with empty name")
    void save_shouldNotSaveDepartment_WhenFieldIsEmpty() throws Exception {
        //Given
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .name("")
                .build();
        String json = objectMapper.writeValueAsString(departmentRequest);
        //When
        postman.perform(MockMvcRequestBuilders.post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
        //Then
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/department").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<DepartmentResponse> departmentResponses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<DepartmentResponse>>() {
        });
        Assertions.assertEquals(4, departmentResponses.size());
    }

    @Test
    @DisplayName("Should get error when user without permission save new department")
    void save_shouldGetError_whenUserDoesNotHavePermission() throws Exception {
        //Given
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .name("testName")
                .build();
        String json = objectMapper.writeValueAsString(departmentRequest);
        //Then
        postman.perform(MockMvcRequestBuilders.post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + tokenRegular))
                .andExpect(status().isForbidden());
    }
}