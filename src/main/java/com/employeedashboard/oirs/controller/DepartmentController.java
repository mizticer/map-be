package com.employeedashboard.oirs.controller;

import com.employeedashboard.oirs.dto.department.DepartmentRequest;
import com.employeedashboard.oirs.dto.department.DepartmentResponse;
import com.employeedashboard.oirs.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping
    public List<DepartmentResponse> findAll() {
        return departmentService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public DepartmentResponse save(@Valid @RequestBody DepartmentRequest departmentRequest) {
        return departmentService.save(departmentRequest);
    }
}
