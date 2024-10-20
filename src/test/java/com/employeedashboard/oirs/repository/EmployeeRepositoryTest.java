package com.employeedashboard.oirs.repository;

import com.employeedashboard.oirs.config.BaseIntegrationTest;
import com.employeedashboard.oirs.exception.EmployeeNotFoundException;
import com.employeedashboard.oirs.model.Address;
import com.employeedashboard.oirs.model.Employee;
import com.employeedashboard.oirs.model.Permission;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.flywaydb.core.internal.util.JsonUtils.toList;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Test
    @DisplayName("Inserting an employee should increase the employee count")
    void save_shouldInsertNewEmployee_whenSavingValidEmployee() {
        //Given
        UUID employeeId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        Address address = Address.builder()
                .id(addressId)
                .street("testStreet")
                .postcode("123")
                .city("testCity")
                .country("testCountry")
                .state("testState")
                .build();
        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("testFirstName")
                .lastName("testLastName")
                .photo(new byte[]{1, 2, 3, 4})
                .role("testRole")
                .department("testDepartament")
                .username("testUsername")
                .password("testPassword")
                .address(address)
                .permission(Permission.ADMIN)
                .build();
        //When
        addressRepository.save(address);
        employeeRepository.save(employee);
        //Then
        List<Employee> employeeList = employeeRepository.findAll(null,null);
        assertEquals(4, employeeList.size());
        assertEquals(employeeList.get(3).getId(), employeeId);
    }

    @Test
    @DisplayName("Finding all employees should return a list with employee count")
    void findAll_shouldReturnListOfEmployees() {
        List<Employee> employeeList = employeeRepository.findAll(null,null);
        assertThat(employeeList).isNotNull();
        assertEquals(3, employeeList.size());
    }

    @Test
    @DisplayName("Checking whether an employee exists")
    void checkEmployeeExists_shouldReturnTrue_whenCheckingExistingEmployee() {
        //Given
        String firstName = "John";
        String lastName = "Doe";
        String role = "Role1";
        String department = "IT";
        //When
        boolean employeeExists = employeeRepository.checkEmployeeExists(firstName, lastName, role, department);
        //Then
        assertTrue(employeeExists);
    }

    @Test
    @DisplayName("Finding employee by id")
    void findById_shouldReturnEmployee() {
        //Given
        UUID id = UUID.fromString("550e8400-e29b-41d4-a716-446655440004");
        //WHen
        Optional<Employee> employee = employeeRepository.findById(id);
        //Then
        assertTrue(employee.isPresent());
    }
    @Test
    @DisplayName("Editing employee details")
    void testEditEmployee() {
        // Given
        UUID id = UUID.fromString("550e8400-e29b-41d4-a716-446655440004");
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        // When
        employee.setFirstName("editedFirstName");
        employee.setLastName("editedLastName");
        employee.setDepartment("editedDepartment");
        employee.setSquad("editedSquad");
        employee.setRole("editedRole");
        employee.setPhoto("editedPhotoURL".getBytes());
        employeeRepository.editByAdminUser(employee);

        Employee editedEmployee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        // Then
        assertEquals("editedFirstName", editedEmployee.getFirstName());
        assertEquals("editedLastName", editedEmployee.getLastName());
        assertEquals("editedDepartment", editedEmployee.getDepartment());
        assertEquals("editedSquad", editedEmployee.getSquad());
        assertEquals("editedRole", editedEmployee.getRole());
        assertArrayEquals("editedPhotoURL".getBytes(), editedEmployee.getPhoto());
    }

    @Test
    @DisplayName("Deleting an employee should decrease the employee count")
    void delete_shouldDeleteEmployee_whenEmployeeExist() {
        //Given
        UUID employeeId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        Address address = Address.builder().id(addressId).street("testStreet").postcode("123").city("testCity").country("testCountry").state("testState")
                .build();
        Employee employee = Employee.builder().id(employeeId).firstName("testFirstName").lastName("testLastName").photo(new byte[]{1, 2, 3, 4}).role("testRole").department("testDepartament").username("testUsername").password("testPassword").address(address)
                .build();

        addressRepository.save(address);
        employeeRepository.save(employee);
        List<Employee> employeeListBefore = employeeRepository.findAll(null,null);

        //When
        employeeRepository.delete(employeeId);

        //Then
        List<Employee> employeeListAfter = employeeRepository.findAll(null,null);
        assertNotNull(employeeListBefore);
        assertFalse(employeeListBefore.isEmpty());
        assertTrue(employeeListBefore.size()>employeeListAfter.size());

    }

}