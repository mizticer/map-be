package com.employeedashboard.oirs.controller;

import com.employeedashboard.oirs.dto.employee.EmployeeRequest;
import com.employeedashboard.oirs.dto.employee.EmployeeRequestEdit;
import com.employeedashboard.oirs.dto.employee.EmployeeResponse;
import com.employeedashboard.oirs.service.EmployeeService;
import com.employeedashboard.oirs.utils.UserRequestUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public List<EmployeeResponse> findAll(@RequestParam(name = "cities", required = false) List<String> cities,
                                          @RequestParam(name = "squads", required = false) List<String> squads) {
        return employeeService.findAll(cities, squads);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse getEmployee(@PathVariable(value = "id") UUID id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public EmployeeResponse save(@Valid @RequestBody EmployeeRequest employeeRequest) {
        return employeeService.save(employeeRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse editEmployee(@PathVariable(value = "id") UUID id, @Valid @RequestBody EmployeeRequestEdit employeeRequestEdit) {
        boolean isAdmin = UserRequestUtils.checkIfCurrentUserHasAdminPermission();
        UUID currentUserId = UserRequestUtils.getCurrentUserID();
        return employeeService.edit(id, employeeRequestEdit, isAdmin, currentUserId);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteEmployee(@PathVariable("id") UUID id) {
        employeeService.deleteEmployee(id);
    }

    @GetMapping("/cities")
    public List<String> getAllCities() {
        return employeeService.getAllCities();
    }
}


