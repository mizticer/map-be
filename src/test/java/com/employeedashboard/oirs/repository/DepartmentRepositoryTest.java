package com.employeedashboard.oirs.repository;

import com.employeedashboard.oirs.config.BaseIntegrationTest;
import com.employeedashboard.oirs.model.Department;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DepartmentRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    @DisplayName("Finding all departments should return a list with departments count")
    void findAll_shouldReturnListOfDepartments() {
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).isNotNull();
        assertEquals(4, departmentList.size());
    }

    @Test
    @DisplayName("Inserting an department should increase the department count")
    void save_shouldInsertNewDepartment_whenSavingValidDepartment() {
        //Given
        UUID departmentId = UUID.randomUUID();
        Department department = Department.builder()
                .id(departmentId)
                .name("testName")
                .build();
        //When
        departmentRepository.save(department);
        //Then
        List<Department> departmentList = departmentRepository.findAll();
        assertEquals(5, departmentList.size());
        assertEquals(departmentList.get(4).getId(), departmentId);
    }

    @Test
    @DisplayName("Checking whether an department exists")
    void checkDepartmentExists_shouldReturnTrue_whenCheckingExistingDepartment() {
        //Given
        String name = "IT";
        //When
        boolean departmentExists = departmentRepository.checkDepartmentExists(name);
        //Then
        assertTrue(departmentExists);
    }
}