package com.employeedashboard.oirs.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder(toBuilder = true)
public class DepartmentRequest {
    @NotBlank(message = "EMPTY NAME DEPARTMENT")
    @Size(max = 100, message = "NAME DEPARTMENT IS TOO LONG")
    private String name;
}
