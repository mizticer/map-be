package com.employeedashboard.oirs.dto.squad;

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
public class SquadRequest {
    @NotBlank(message = "EMPTY NAME SQUAD")
    @Size(max = 100, message = "NAME SQUAD IS TOO LONG")
    private String name;
}
