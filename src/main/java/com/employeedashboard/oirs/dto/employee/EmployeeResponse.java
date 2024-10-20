package com.employeedashboard.oirs.dto.employee;

import com.employeedashboard.oirs.dto.address.AddressResponse;
import com.employeedashboard.oirs.model.Employee;
import com.employeedashboard.oirs.model.Permission;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;


@NoArgsConstructor
@Getter
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public class EmployeeResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private byte[] photo;
    private String role;
    private String squad;
    private String department;
    private String username;
    private AddressResponse address;
    private Permission permission;

    public static EmployeeResponse of(Employee employee) {

        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .photo(employee.getPhoto())
                .role(employee.getRole())
                .department(employee.getDepartment())
                .squad(employee.getSquad())
                .username(employee.getUsername())
                .permission(employee.getPermission())
                .address(AddressResponse.of(employee.getAddress()))
                .build();
    }

}

