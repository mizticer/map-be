package com.employeedashboard.oirs.service;

import com.employeedashboard.oirs.config.BaseIntegrationTest;
import com.employeedashboard.oirs.dto.department.DepartmentRequest;
import com.employeedashboard.oirs.dto.department.DepartmentResponse;
import com.employeedashboard.oirs.model.Department;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DepartmentServiceTest extends BaseIntegrationTest {

    @Autowired
    private DepartmentService departmentService;

    @Test
    @DisplayName("Should return a list of all departments")
    void findAll_shouldReturnListOfDepartments() {
        List<DepartmentResponse> departmentResponses = departmentService.findAll();
        assertThat(departmentResponses).isNotNull();
        assertEquals(4, departmentResponses.size());
    }

    @Test
    @DisplayName("Should save a new department")
    void save_shouldSaveNewDepartment_whenInputDataIsCorrect() {
        //Given
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .name("testName")
                .build();
        UUID departmentId = UUID.randomUUID();
        Department department = new Department(departmentId, "testName");
        //When
        DepartmentResponse savedDepartment = departmentService.save(departmentRequest);
        //Then
        assertNotNull(savedDepartment);
        assertEquals(savedDepartment.getName(), department.getName());
    }
}