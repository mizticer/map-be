package com.employeedashboard.oirs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address {
    private UUID id;
    private String city;
    private String street;
    private String state;
    private String country;
    private String postcode;
}
