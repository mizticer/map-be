package com.employeedashboard.oirs.controller;

import com.employeedashboard.oirs.config.BaseIntegrationTest;
import com.employeedashboard.oirs.dto.address.AddressRequest;
import com.employeedashboard.oirs.dto.auth.AuthRequest;
import com.employeedashboard.oirs.dto.auth.AuthResponse;
import com.employeedashboard.oirs.dto.employee.EmployeeRequest;
import com.employeedashboard.oirs.dto.employee.EmployeeRequestEdit;
import com.employeedashboard.oirs.dto.employee.EmployeeResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class EmployeeControllerTest extends BaseIntegrationTest {

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
    @DisplayName("Should return a list of employees including John Doe")
    void findAll_shouldReturnListOfEmployees() throws Exception {
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/employee").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<EmployeeResponse> employeeResponseDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<EmployeeResponse>>() {
        });
        assertEquals("John", employeeResponseDtos.get(0).getFirstName());
        assertEquals("Doe", employeeResponseDtos.get(0).getLastName());
    }

    @Test
    @DisplayName("Should successfully save an employee with specified details")
    void save_shouldInsertNewEmployee_whenSavingValidEmployee() throws Exception {
        //Given
        AddressRequest addressRequest = AddressRequest.builder()
                .street("testStreet")
                .postcode("123")
                .city("testCity")
                .state("testState")
                .country("testCountry")
                .build();
        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("testFirstName")
                .lastName("testLastName")
                .role("testRole")
                .photo(new byte[]{1, 2, 3, 4})
                .department("testDepartament")
                .squad("testSquad")
                .address(addressRequest)
                .build();
        String json = objectMapper.writeValueAsString(employeeRequest);
        //When
        postman.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());
        //Then
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/employee").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<EmployeeResponse> employeesResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<EmployeeResponse>>() {
        });
        assertEquals("testFirstName", employeesResponse.get(3).getFirstName());
        assertEquals("testLastName", employeesResponse.get(3).getLastName());
    }


    @Test
    @DisplayName("Should not save an employee with empty firstName")
    void save_shouldNotSaveEmployee_WhenFieldIsEmpty() throws Exception {
        //Given
        AddressRequest addressRequest = AddressRequest.builder()
                .street("testStreet")
                .postcode("123")
                .city("testCity")
                .state("testState")
                .country("testCountry")
                .build();
        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("")
                .lastName("testLastName")
                .role("testRole")
                .photo(new byte[]{1, 2, 3, 4})
                .department("testDepartament")
                .address(addressRequest)
                .build();
        String json = objectMapper.writeValueAsString(employeeRequest);
        //When
        postman.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
        //Then
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/employee").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<EmployeeResponse> employeesResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<EmployeeResponse>>() {
        });
        assertEquals(3, employeesResponse.size());
    }

    @Test
    @DisplayName("Should not save an employee when this Employee exists")
    void save_shouldNotSaveEmployee_WhenEmployeeExists() throws Exception {
        //Given
        AddressRequest addressRequest = AddressRequest.builder()
                .street("testStreet")
                .postcode("123")
                .city("testCity")
                .state("testState")
                .country("testCountry")
                .build();
        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .role("Role1")
                .photo(new byte[]{1, 2, 3, 4})
                .department("IT")
                .squad("404_squad")
                .address(addressRequest)
                .build();
        String json = objectMapper.writeValueAsString(employeeRequest);
        //When
        postman.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
        //Then
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/employee").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<EmployeeResponse> employeesResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<EmployeeResponse>>() {
        });
        assertEquals(3, employeesResponse.size());
    }

    @Test
    @DisplayName("Should get error when user does not have permission")
    void save_shouldGetError_WhenUserDoesNotHavePermission() throws Exception {
        //Given
        AddressRequest addressRequest = AddressRequest.builder()
                .street("testStreet")
                .postcode("123")
                .city("testCity")
                .state("testState")
                .country("testCountry")
                .build();
        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("John")
                .lastName("Doel")
                .role("Role1")
                .photo(new byte[]{1, 2, 3, 4})
                .department("IT")
                .squad("404_squad")
                .address(addressRequest)
                .build();
        String json = objectMapper.writeValueAsString(employeeRequest);
        //Then
        postman.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + tokenRegular))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should get employee by id")
    void getEmployee_shouldFindEmployeeById_WhenEmployeeExists() throws Exception {
        //Given
        String employeeIdPath = "/employee/550e8400-e29b-41d4-a716-446655440004";
        //When
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get(employeeIdPath).header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        //Then
        EmployeeResponse employeeResponseDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<EmployeeResponse>() {
        });
        assertEquals("Helena", employeeResponseDtos.getFirstName());
        assertEquals("Smith", employeeResponseDtos.getLastName());
    }

    @Test
    @DisplayName("Should return 404 when trying to find not existing employee")
    void getEmployee_shouldGetError_WhenEmployeeNotExists() throws Exception {
        //Given
        String employeeIdPath = "/employee/550e8400-1111-aaaa-bbbb-446655440004";
        //Then
        postman.perform(MockMvcRequestBuilders.get(employeeIdPath).header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should edit employee details when this employee exists")
    void editEmployee_shouldEditEmployee_WhenEmployeeExists() throws Exception {
        // Given
        String employeeIdPath = "/employee/550e8400-e29b-41d4-a716-446655440004";
        EmployeeRequestEdit employeeRequestEdit = new EmployeeRequestEdit();
        employeeRequestEdit.setFirstName("editedFirstName");
        employeeRequestEdit.setLastName("editedLastName");
        employeeRequestEdit.setDepartment("editedDepartment");
        employeeRequestEdit.setSquad("editedSquad");
        employeeRequestEdit.setRole("editedRole");
        employeeRequestEdit.setPhoto("editedPhotoURL".getBytes());
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity("editedCity");
        addressRequest.setStreet("editedStreet");
        addressRequest.setCountry("editedCountry");
        addressRequest.setState("editedState");
        addressRequest.setPostcode("88888");
        employeeRequestEdit.setAddress(addressRequest);

        // When
         postman.perform(put(employeeIdPath)
                        .content(objectMapper.writeValueAsString(employeeRequestEdit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        MvcResult mvcResult = postman.perform(get(employeeIdPath)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();
        EmployeeResponse employeeResponseDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<EmployeeResponse>() {
        });

        //Then
        assertEquals("editedFirstName", employeeResponseDtos.getFirstName());
        assertEquals("editedLastName", employeeResponseDtos.getLastName());
        assertEquals("editedDepartment", employeeResponseDtos.getDepartment());
        assertEquals("editedSquad", employeeResponseDtos.getSquad());
        assertEquals("editedRole", employeeResponseDtos.getRole());
        assertArrayEquals(("editedPhotoURL".getBytes()), employeeResponseDtos.getPhoto());
    }

    @Test
    @DisplayName("Should return 404 when trying to edit not existing employee")
    void editEmployee_shouldGetError_WhenEmployeeNotExists() throws Exception {
        // Given
        String employeeIdPath = "/employee/550e8400-1111-aaaa-bbbb-446655440004";
        EmployeeRequestEdit employeeRequestEdit = new EmployeeRequestEdit();
        employeeRequestEdit.setFirstName("editedFirstName");
        employeeRequestEdit.setLastName("editedLastName");
        employeeRequestEdit.setDepartment("editedDepartment");
        employeeRequestEdit.setSquad("editedSquad");
        employeeRequestEdit.setRole("editedRole");
        employeeRequestEdit.setPhoto("editedPhotoURL".getBytes());
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity("editedCity");
        addressRequest.setStreet("editedStreet");
        addressRequest.setCountry("editedCountry");
        addressRequest.setState("editedState");
        addressRequest.setPostcode("88888");
        employeeRequestEdit.setAddress(addressRequest);

        // Then
        postman.perform(MockMvcRequestBuilders.put(employeeIdPath)
                        .content(objectMapper.writeValueAsString(employeeRequestEdit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should get succes when trying to edit employee with admin permission")
    void editEmployee_shouldGetSuccessEmployee_WhenUserHavePermission() throws Exception {
        // Given
        String employeeIdPath = "/employee/550e8400-e29b-41d4-a716-446655440004";
        EmployeeRequestEdit employeeRequestEdit = new EmployeeRequestEdit();
        employeeRequestEdit.setFirstName("editedFirstName");
        employeeRequestEdit.setLastName("editedLastName");
        employeeRequestEdit.setDepartment("editedDepartment");
        employeeRequestEdit.setSquad("editedSquad");
        employeeRequestEdit.setRole("editedRole");
        employeeRequestEdit.setPhoto("editedPhotoURL".getBytes());
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity("editedCity");
        addressRequest.setStreet("editedStreet");
        addressRequest.setCountry("editedCountry");
        addressRequest.setState("editedState");
        addressRequest.setPostcode("88888");
        employeeRequestEdit.setAddress(addressRequest);

        // Then
        postman.perform(put(employeeIdPath)
                        .content(objectMapper.writeValueAsString(employeeRequestEdit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRegular))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Should delete an employee")
    void delete_shouldDeleteEmployee() throws Exception {
        //Given
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/employee").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<EmployeeResponse> employeeResponseDtosBefore = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<EmployeeResponse>>() {
        });

       //When
        postman.perform(MockMvcRequestBuilders.delete("/employee/550e8400-e29b-41d4-a716-446655440003")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        MvcResult mvcResultAfter = postman.perform(MockMvcRequestBuilders.get("/employee").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<EmployeeResponse> employeeResponseDtosAfter = objectMapper.readValue(mvcResultAfter.getResponse().getContentAsString(), new TypeReference<List<EmployeeResponse>>() {
        });

        //Then
        assertTrue(employeeResponseDtosAfter.size()<employeeResponseDtosBefore.size());
    }

    @Test
    @DisplayName("Should delete an employee")
    void delete_shouldNotDeleteEmployee_ifTheyDoNotExist() throws Exception {
        //Given
        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.get("/employee").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<EmployeeResponse> employeeResponseDtosBefore = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<EmployeeResponse>>() {
        });

        //When
        postman.perform(MockMvcRequestBuilders.delete("/employee/550e8400-0000-0000-0000-446655440003")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());

        MvcResult mvcResultAfter = postman.perform(MockMvcRequestBuilders.get("/employee").header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<EmployeeResponse> employeeResponseDtosAfter = objectMapper.readValue(mvcResultAfter.getResponse().getContentAsString(), new TypeReference<List<EmployeeResponse>>() {
        });

        //Then
        assertEquals(employeeResponseDtosAfter.size(), employeeResponseDtosBefore.size());
    }

    @Test
    @DisplayName("Should get error when user does not have permission to delete")
    void delete_shouldGetError_whenUserDoesNotHavePermission() throws Exception {
        //Then
        postman.perform(MockMvcRequestBuilders.delete("/employee/550e8400-e29b-41d4-a716-446655440003")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRegular))
                .andExpect(status().isForbidden());
    }
}


