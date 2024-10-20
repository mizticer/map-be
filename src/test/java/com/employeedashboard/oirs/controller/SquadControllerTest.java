package com.employeedashboard.oirs.controller;

import com.employeedashboard.oirs.config.BaseIntegrationTest;
import com.employeedashboard.oirs.dto.auth.AuthRequest;
import com.employeedashboard.oirs.dto.auth.AuthResponse;
import com.employeedashboard.oirs.dto.squad.SquadRequest;
import com.employeedashboard.oirs.dto.squad.SquadResponse;
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
class SquadControllerTest extends BaseIntegrationTest {

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
    @DisplayName("Should return a list of squad including 404_squad and YOLO")
    void findAll_shouldReturnListOfSquad() throws Exception {
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/squad").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<SquadResponse> squadResponseDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<SquadResponse>>() {
        });
        Assertions.assertEquals("404_squad", squadResponseDtos.get(0).getName());
        Assertions.assertEquals("YOLO", squadResponseDtos.get(3).getName());
    }

    @Test
    @DisplayName("Should successfully save an squad with specified details")
    void save_shouldInsertNewSquad_whenSavingValidSquad() throws Exception {
        //Given
        SquadRequest squadRequest = SquadRequest.builder()
                .name("testName")
                .build();
        String json = objectMapper.writeValueAsString(squadRequest);
        //When
        postman.perform(MockMvcRequestBuilders.post("/squad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());
        //Then
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/squad").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<SquadResponse> squadResponses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<SquadResponse>>() {
        });
        Assertions.assertEquals("testName", squadResponses.get(4).getName());
    }

    @Test
    @DisplayName("Should not save an squad when this squad exists")
    void save_shouldNotSaveSquad_WhenSquadExists() throws Exception {
        //Given
        SquadRequest squadRequest = SquadRequest.builder()
                .name("404_squad")
                .build();
        String json = objectMapper.writeValueAsString(squadRequest);
        //When
        postman.perform(MockMvcRequestBuilders.post("/squad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
        //Then
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/squad").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<SquadResponse> squadResponses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<SquadResponse>>() {
        });
        Assertions.assertEquals(4, squadResponses.size());
    }

    @Test
    @DisplayName("Should not save an squad with empty name")
    void save_shouldNotSaveSquad_WhenFieldIsEmpty() throws Exception {
        //Given
        SquadRequest squadRequest = SquadRequest.builder()
                .name("")
                .build();
        String json = objectMapper.writeValueAsString(squadRequest);
        //When
        postman.perform(MockMvcRequestBuilders.post("/squad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
        //Then
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/squad").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<SquadResponse> squadResponses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<SquadResponse>>() {
        });
        Assertions.assertEquals(4, squadResponses.size());
    }

    @Test
    @DisplayName("Should get error when user does not have permission to add squad")
    void save_shouldGetError_whenUserDoesNotHavePermission() throws Exception {
        //Given
        SquadRequest squadRequest = SquadRequest.builder()
                .name("testName")
                .build();
        String json = objectMapper.writeValueAsString(squadRequest);
        //Then
        postman.perform(MockMvcRequestBuilders.post("/squad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + tokenRegular))
                .andExpect(status().isForbidden());
    }
}