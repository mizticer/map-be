package com.employeedashboard.oirs.repository;

import com.employeedashboard.oirs.config.BaseIntegrationTest;
import com.employeedashboard.oirs.model.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private AddressRepository addressRepository;

    @Test
    @DisplayName("Inserting an address should increase the address count")
    void save_shouldInsertNewAddress_whenSavingValidAddress() {
        //Given
        UUID adressId = UUID.randomUUID();
        Address address = Address.builder()
                .id(adressId)
                .street("testStreet")
                .postcode("123")
                .city("testCity")
                .country("testCountry")
                .state("testState")
                .build();
        //When
        addressRepository.save(address);
        //Then
        List<Address> addressList = addressRepository.findAll();
        assertEquals(4, addressList.size());
        assertEquals(addressList.get(3).getId(), adressId);
    }

    @Test
    @DisplayName("Finding all addresses should return a list with addresses count")
    void findAll_shouldReturnListOfAddresses() {
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).isNotNull();
        assertEquals(3, addressList.size());
    }

    @Test
    @DisplayName("Checking whether an address exists")
    void checkAddressExists_shouldReturnTrue_whenCheckingExistingAddress() {
        String city = "Warsaw";
        String street = "Street1";
        String country = "Poland";
        String state = "State1";
        String postcode = "12345";
        //When
        boolean addressExists = addressRepository.checkAddressExists(city, street, country, state, postcode);
        //Then
        assertTrue(addressExists);
    }

    @Test
    @DisplayName("It should return an address based on the values of the fields")
    void getAddressByDetails_shouldReturnAddress_whenAddressExistsWithSuchValues() {
        // Given
        String city = "Warsaw";
        String street = "Street1";
        String country = "Poland";
        String state = "State1";
        String postcode = "12345";
        // When
        Address address = addressRepository.getAddressByDetails(city, street, country, state, postcode);
        // Then
        assertEquals(city, address.getCity());
        assertEquals(street, address.getStreet());
        assertEquals(country, address.getCountry());
        assertEquals(state, address.getState());
        assertEquals(postcode, address.getPostcode());
    }
    @Test
    @DisplayName("It should return edited address")
    void testEditAddress() {
        // Given
        String city = "Warsaw";
        String street = "Street1";
        String country = "Poland";
        String state = "State1";
        String postcode = "12345";
        Address address = addressRepository.getAddressByDetails(city, street, country, state, postcode);

        // When
        address.setCity("EditedCity");
        address.setStreet("EditedStreet");
        address.setCountry("EditedCountry");
        address.setState("EditedState");
        address.setPostcode("88888");
        addressRepository.edit(address);
        Address editedAddress = addressRepository.getAddressByDetails("EditedCity", "EditedStreet", "EditedCountry", "EditedState", "88888");

        // Then
        assertEquals("EditedCity", editedAddress.getCity());
        assertEquals("EditedStreet", editedAddress.getStreet());
        assertEquals("EditedCountry", editedAddress.getCountry());
        assertEquals("EditedState", editedAddress.getState());
        assertEquals("88888", editedAddress.getPostcode());

    }
}