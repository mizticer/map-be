package com.employeedashboard.oirs.dto.address;

import jakarta.validation.constraints.NotBlank;
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
public class AddressRequest {

    @NotBlank(message = "EMPTY CITY")
    @Size(max = 100, message = "CITY NAME TOO LONG")
    private String city;
    @NotBlank(message = "EMPTY STREET")
    @Size(max = 100, message = "STREET NAME TOO LONG")
    private String street;
    @NotBlank(message = "EMPTY STATE")
    @Size(max = 100, message = "STATE NAME TOO LONG")
    private String state;
    @NotBlank(message = "EMPTY COUNTRY")
    @Size(max = 100, message = "COUNTRY NAME TOO LONG")
    private String country;
    @NotBlank(message = "EMPTY POSTCODE")
    @Size(max = 10, message = "POSTCODE NAME TOO LONG")
    private String postcode;

}
