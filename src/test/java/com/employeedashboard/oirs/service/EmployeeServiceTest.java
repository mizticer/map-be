package com.employeedashboard.oirs.service;

import com.employeedashboard.oirs.config.BaseIntegrationTest;
import com.employeedashboard.oirs.dto.address.AddressRequest;
import com.employeedashboard.oirs.dto.employee.EmployeeRequest;
import com.employeedashboard.oirs.dto.employee.EmployeeRequestEdit;
import com.employeedashboard.oirs.dto.employee.EmployeeResponse;
import com.employeedashboard.oirs.exception.EmployeeNotFoundException;
import com.employeedashboard.oirs.exception.EmployeeUnauthorizedChangeException;
import com.employeedashboard.oirs.model.Address;
import com.employeedashboard.oirs.model.Employee;
import com.employeedashboard.oirs.model.Permission;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.flywaydb.core.internal.util.JsonUtils.toList;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceTest extends BaseIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    @DisplayName("Should return a list of all employees")
    void findAll_shouldReturnListOfEmployees() {
        List<EmployeeResponse> employeesResponse = employeeService.findAll(null,null);
        assertThat(employeesResponse).isNotNull();
        assertEquals(3, employeesResponse.size());
    }

    @Test
    @DisplayName("Should save a new employee")
    void save_shouldSaveNewEmployee_whenInputDataIsCorrect() {
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
                .photo(new byte[]{1, 2, 3, 4})
                .department("testDepartament")
                .role("testRole")
                .squad("testSquad")
                .address(addressRequest)
                .permission(Permission.ADMIN)
                .build();

        UUID addressId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        Address address = new Address(addressId, "testCity", "testStreet", "testState", "testCountry", "123");
        Employee employee = new Employee(employeeId, "testFirstName", "testLastName", new byte[]{1, 2, 3, 4}, "testRole", "testSquad", "testDepartament", address, "test", "password", Permission.ADMIN);

        //When
        EmployeeResponse savedEmployee = employeeService.save(employeeRequest);
        //Then
        assertNotNull(savedEmployee);
        assertEquals(savedEmployee.getFirstName(), employee.getFirstName());
        assertEquals(savedEmployee.getLastName(), employee.getLastName());
    }

    @Test
    @DisplayName("Should return proper employee when getting by existing id")
    void getEmployeeById_shouldFindEmployee_whenIdIsCorrect() {
        //Given
        UUID id = UUID.fromString("550e8400-e29b-41d4-a716-446655440004");
        //When
        EmployeeResponse employeeResponse = employeeService.getEmployeeById(id);
        //Then
        assertEquals("Helena", employeeResponse.getFirstName());
        assertEquals("Smith", employeeResponse.getLastName());
        assertEquals("Role3", employeeResponse.getRole());
        assertEquals("Marketing", employeeResponse.getDepartment());
        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440005"), employeeResponse.getAddress().getId());
    }

    @Test
    @DisplayName("Should throw exception when getting employee by not existing id")
    void getEmployeeById_shouldThrowException_whenIdIsNotCorrect() {
        //Given
        UUID id = UUID.fromString("550e8400-e29b-dddd-0000-000000000000");
        //Then
        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(id));
    }
    @Test
    @DisplayName("Should edit employee details")
    void editEmployee_shouldEditEmployee_WhenEmployeeExists() {
        // Given
        UUID id = UUID.fromString("550e8400-e29b-41d4-a716-446655440004");
        EmployeeRequestEdit employeeRequestEdit = new EmployeeRequestEdit();
        employeeRequestEdit.setFirstName("editedFirstName");
        employeeRequestEdit.setLastName("editedLastName");
        employeeRequestEdit.setDepartment("editedDepartment");
        employeeRequestEdit.setSquad("editedSquad");
        employeeRequestEdit.setRole("editedRole");
        employeeRequestEdit.setPhoto("editedPhotoURL".getBytes());
        employeeRequestEdit.setPermission(Permission.ADMIN);

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity("editedCity");
        addressRequest.setStreet("editedStreet");
        addressRequest.setCountry("editedCountry");
        addressRequest.setState("editedState");
        addressRequest.setPostcode("88888");
        employeeRequestEdit.setAddress(addressRequest);

        // When
        EmployeeResponse editedEmployee = employeeService.edit(id, employeeRequestEdit, true, id);

        // Then
        assertNotNull(editedEmployee);
        assertEquals("editedFirstName", editedEmployee.getFirstName());
        assertEquals("editedLastName", editedEmployee.getLastName());
        assertEquals("editedDepartment", editedEmployee.getDepartment());
        assertEquals("editedSquad", editedEmployee.getSquad());
        assertEquals("editedRole", editedEmployee.getRole());
        assertArrayEquals("editedPhotoURL".getBytes(), editedEmployee.getPhoto());
        
        assertNotNull(editedEmployee.getAddress());
        assertEquals("editedCity", editedEmployee.getAddress().getCity());
        assertEquals("editedStreet", editedEmployee.getAddress().getStreet());
        assertEquals("editedCountry", editedEmployee.getAddress().getCountry());
        assertEquals("editedState", editedEmployee.getAddress().getState());
        assertEquals("88888", editedEmployee.getAddress().getPostcode());
    }

    @Test
    @DisplayName("Should edit only photo and address when employee have regular permission")
    void editEmployee_shouldThrowException_WhenEmployeeWithRegularPermissionEditAnotherEmployee() {
        // Given
        UUID id = UUID.fromString("550e8400-e29b-41d4-a716-446655440004");
        UUID editedUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        EmployeeRequestEdit employeeRequestEdit = new EmployeeRequestEdit();
        employeeRequestEdit.setFirstName("editedFirstNameNew");
        employeeRequestEdit.setLastName("editedLastNameNew");
        employeeRequestEdit.setDepartment("editedDepartmentNew");
        employeeRequestEdit.setSquad("editedSquadNew");
        employeeRequestEdit.setRole("editedRoleNew");
        employeeRequestEdit.setPhoto("editedPhotoURLNew".getBytes());
        employeeRequestEdit.setPermission(Permission.ADMIN);

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity("editedCityNew");
        addressRequest.setStreet("editedStreetNew");
        addressRequest.setCountry("editedCountryNew");
        addressRequest.setState("editedStateNew");
        addressRequest.setPostcode("88888New");
        employeeRequestEdit.setAddress(addressRequest);

        // Then
        assertThrows(EmployeeUnauthorizedChangeException.class,
                () -> employeeService.edit(id, employeeRequestEdit, false, editedUserId));
    }

    @Test
    @DisplayName("Should edit only photo and address when employee have regular permission")
    void editEmployee_shouldEditOnlyPhotoAndAddressEmployee_WhenEmployeeHaveRegularPermission() {
        // Given
        UUID id = UUID.fromString("550e8400-e29b-41d4-a716-446655440004");
        EmployeeRequestEdit employeeRequestEdit = new EmployeeRequestEdit();
        employeeRequestEdit.setFirstName("editedFirstNameNew");
        employeeRequestEdit.setLastName("editedLastNameNew");
        employeeRequestEdit.setDepartment("editedDepartmentNew");
        employeeRequestEdit.setSquad("editedSquadNew");
        employeeRequestEdit.setRole("editedRoleNew");
        employeeRequestEdit.setPhoto("editedPhotoURLNew".getBytes());
        employeeRequestEdit.setPermission(Permission.ADMIN);

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity("editedCityNew");
        addressRequest.setStreet("editedStreetNew");
        addressRequest.setCountry("editedCountryNew");
        addressRequest.setState("editedStateNew");
        addressRequest.setPostcode("88888New");
        employeeRequestEdit.setAddress(addressRequest);

        // When
        EmployeeResponse editedEmployee = employeeService.edit(id, employeeRequestEdit, false, id);

        // Then
        assertNotNull(editedEmployee);
        assertNotEquals("editedFirstNameNew", editedEmployee.getFirstName());
        assertNotEquals("editedLastNameNew", editedEmployee.getLastName());
        assertNotEquals("editedDepartmentNew", editedEmployee.getDepartment());
        assertNotEquals("editedSquadNew", editedEmployee.getSquad());
        assertNotEquals("editedRoleNew", editedEmployee.getRole());
        assertArrayEquals("editedPhotoURLNew".getBytes(), editedEmployee.getPhoto());

        assertNotNull(editedEmployee.getAddress());
        assertEquals("editedCityNew", editedEmployee.getAddress().getCity());
        assertEquals("editedStreetNew", editedEmployee.getAddress().getStreet());
        assertEquals("editedCountryNew", editedEmployee.getAddress().getCountry());
        assertEquals("editedStateNew", editedEmployee.getAddress().getState());
        assertEquals("88888New", editedEmployee.getAddress().getPostcode());
    }
}
