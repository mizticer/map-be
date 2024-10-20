package com.employeedashboard.oirs.dto.employee;


import com.employeedashboard.oirs.dto.address.AddressRequest;
import com.employeedashboard.oirs.model.Permission;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class EmployeeRequest {
    @NotBlank(message = "EMPTY FIRSTNAME")
    @Size(max = 50, message = "FIRSTNAME NAME TOO LONG")
    private String firstName;
    @NotBlank(message = "EMPTY LASTNAME")
    @Size(max = 50, message = "LASTNAME NAME TOO LONG")
    private String lastName;
    @NotEmpty(message = "EMPTY PHOTO")
    private byte[] photo;
    @NotBlank(message = "EMPTY ROLE")
    @Size(max = 50, message = "ROLE NAME TOO LONG")
    private String role;
    @NotBlank(message = "EMPTY DEPARTMENT")
    @Size(max = 100, message = "DEPARTMENT NAME TOO LONG")
    private String department;
    @Size(max = 100, message = "SQUAD NAME TOO LONG")
    private String squad;
    private Permission permission;
    @Valid
    private AddressRequest address;
}
