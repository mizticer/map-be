package com.employeedashboard.oirs.dto.department;

import com.employeedashboard.oirs.model.Department;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public class DepartmentResponse {
    private UUID id;
    private String name;

    public static DepartmentResponse of(Department department) {

        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }
}
