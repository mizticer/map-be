package com.employeedashboard.oirs.dto.address;

import com.employeedashboard.oirs.model.Address;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public class AddressResponse {
    private UUID id;
    private String city;
    private String street;
    private String state;
    private String country;
    private String postcode;

    public static AddressResponse of(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .city(address.getCity())
                .street(address.getStreet())
                .state(address.getState())
                .country(address.getCountry())
                .postcode(address.getPostcode())
                .build();
    }
}
