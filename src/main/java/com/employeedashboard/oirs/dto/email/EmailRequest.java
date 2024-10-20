package com.employeedashboard.oirs.dto.email;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class EmailRequest {
    @NotEmpty(message = "RECIPIENT LIST CANNOT BE EMPTY")
    private List<String> to;
    private String subject;
    private String text;
}
