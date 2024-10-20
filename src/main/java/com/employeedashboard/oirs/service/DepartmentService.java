package com.employeedashboard.oirs.service;

import com.employeedashboard.oirs.dto.department.DepartmentRequest;
import com.employeedashboard.oirs.dto.department.DepartmentResponse;
import com.employeedashboard.oirs.exception.DepartmentAlreadyExistException;
import com.employeedashboard.oirs.model.Department;
import com.employeedashboard.oirs.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAll() {
        return departmentRepository.findAll()
                .stream()
                .map(DepartmentResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public DepartmentResponse save(DepartmentRequest departmentRequest) {
        if (!departmentRepository.checkDepartmentExists(departmentRequest.getName())) {
            Department department = Department.builder()
                    .id(UUID.randomUUID())
                    .name(departmentRequest.getName())
                    .build();
            departmentRepository.save(department);
            return DepartmentResponse.of(department);
        }
        throw new DepartmentAlreadyExistException(departmentRequest.getName());
    }
}

