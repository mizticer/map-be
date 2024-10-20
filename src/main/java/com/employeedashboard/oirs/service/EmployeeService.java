package com.employeedashboard.oirs.service;

import com.employeedashboard.oirs.dto.address.AddressRequest;
import com.employeedashboard.oirs.dto.employee.EmployeeRequest;
import com.employeedashboard.oirs.dto.employee.EmployeeRequestEdit;
import com.employeedashboard.oirs.dto.employee.EmployeeResponse;
import com.employeedashboard.oirs.exception.EmployeeAlreadyExistException;
import com.employeedashboard.oirs.exception.EmployeeNotFoundException;
import com.employeedashboard.oirs.exception.EmployeeUnauthorizedChangeException;
import com.employeedashboard.oirs.model.Address;
import com.employeedashboard.oirs.model.Employee;
import com.employeedashboard.oirs.repository.AddressRepository;
import com.employeedashboard.oirs.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private static final String EMPLOYEE_NOT_FOUND = "Employee with ID '%s' not found";
    private static final String EMPLOYEE_UNAUTHORIZED_CHANGE = "Employee with ID '%s' cannot perform that operation";
    private final EmployeeRepository employeeRepository;
    private final AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll(List<String> cities, List<String> squads) {
        return employeeRepository.findAll(cities, squads)
                .stream()
                .map(EmployeeResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmployeeResponse save(EmployeeRequest employeeRequest) {
        if ((!employeeRepository.checkEmployeeExists(employeeRequest.getFirstName(), employeeRequest.getLastName(), employeeRequest.getRole()
                , employeeRequest.getDepartment()))) {
            Address address;
            if ((!addressRepository.checkAddressExists(employeeRequest.getAddress().getCity(), employeeRequest.getAddress().getStreet()
                    , employeeRequest.getAddress().getCountry(), employeeRequest.getAddress().getState(), employeeRequest.getAddress().getPostcode()))) {
                UUID addressId = UUID.randomUUID();
                address = Address.builder()
                        .id(addressId)
                        .city(employeeRequest.getAddress().getCity())
                        .street(employeeRequest.getAddress().getStreet())
                        .country(employeeRequest.getAddress().getCountry())
                        .state(employeeRequest.getAddress().getState())
                        .postcode(employeeRequest.getAddress().getPostcode()).build();
                addressRepository.save(address);
            } else {
                address = addressRepository.getAddressByDetails(employeeRequest.getAddress().getCity(), employeeRequest.getAddress().getStreet()
                        , employeeRequest.getAddress().getCountry(), employeeRequest.getAddress().getState(), employeeRequest.getAddress().getPostcode());
            }
            Employee employee = Employee.builder()
                    .id(UUID.randomUUID())
                    .firstName(employeeRequest.getFirstName())
                    .lastName(employeeRequest.getLastName())
                    .department(employeeRequest.getDepartment())
                    .squad(employeeRequest.getSquad())
                    .role(employeeRequest.getRole())
                    .photo(employeeRequest.getPhoto())
                    .username(generateUniqueUsername(employeeRequest.getFirstName(), employeeRequest.getLastName()))
                    .password("")
                    .address(address)
                    .permission(employeeRequest.getPermission())
                    .build();
            employeeRepository.save(employee);
            return EmployeeResponse.of(employee);
        }
        throw new EmployeeAlreadyExistException(employeeRequest.getFirstName(), employeeRequest.getLastName());
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(UUID id) {
        Employee employee = findById(id);
        return EmployeeResponse.of(employee);
    }

    @Transactional
    public void deleteEmployee(UUID id) {
        findById(id);
        employeeRepository.delete(id);
    }

    @Transactional
    public EmployeeResponse edit(UUID id, EmployeeRequestEdit employeeRequestEdit, boolean isAdmin, UUID currentUserId) {
        Employee employee = findById(id);

        employee.setFirstName(employeeRequestEdit.getFirstName());
        employee.setLastName(employeeRequestEdit.getLastName());
        employee.setDepartment(employeeRequestEdit.getDepartment());
        employee.setSquad(employeeRequestEdit.getSquad());
        employee.setRole(employeeRequestEdit.getRole());
        employee.setPhoto(employeeRequestEdit.getPhoto());
        employee.setPermission(employeeRequestEdit.getPermission());

        Address address = employee.getAddress();
        AddressRequest editedAddressRequest = employeeRequestEdit.getAddress();

        if (!editedAddressRequest.getCity().equals(address.getCity())
                || !editedAddressRequest.getStreet().equals(address.getStreet())
                || !editedAddressRequest.getCountry().equals(address.getCountry())
                || !editedAddressRequest.getState().equals(address.getState())
                || !editedAddressRequest.getPostcode().equals(address.getPostcode())) {

            Address newAddress = Address.builder()
                    .id(UUID.randomUUID())
                    .city(editedAddressRequest.getCity())
                    .street(editedAddressRequest.getStreet())
                    .country(editedAddressRequest.getCountry())
                    .state(editedAddressRequest.getState())
                    .postcode(editedAddressRequest.getPostcode())
                    .build();

            addressRepository.save(newAddress);
            employee.setAddress(newAddress);
        } else {
            employee.setAddress(address);
        }

        if (isAdmin) {
            employeeRepository.editByAdminUser(employee);
            return EmployeeResponse.of(employee);
        } else if (currentUserId.equals(id)) {
            employeeRepository.editByRegularUser(employee);
            Employee editedEmployee = findById(id);
            return EmployeeResponse.of(editedEmployee);
        } else {
            throw new EmployeeUnauthorizedChangeException(String.format(EMPLOYEE_UNAUTHORIZED_CHANGE, currentUserId));
        }
    }

    private Employee findById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(String.format(EMPLOYEE_NOT_FOUND, id)));
    }

    @Transactional(readOnly = true)
    public List<String> getAllCities() {
        return addressRepository.findAllCities();
    }

    private String generateUniqueUsername(String firstName, String lastName) {
        String normalizedFirstName = Normalizer.normalize(firstName.toLowerCase().replaceAll("ł", "l"), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        String normalizedLastName = Normalizer.normalize(lastName.toLowerCase().replaceAll("ł", "l"), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        long numberOfUsernamesStartingWithSameFirstAndLastName = employeeRepository.findAll(Collections.emptyList(), Collections.emptyList()).stream().filter(employee -> employee.getUsername().split("@")[0].replaceAll("\\d", "").equals(String.format("%s.%s", normalizedFirstName, normalizedLastName))).count();
        if (numberOfUsernamesStartingWithSameFirstAndLastName == 0) {
            return String.format("%s.%s@devbridge.com", normalizedFirstName, normalizedLastName);
        } else {
            return String.format("%s.%s%d@devbridge.com", normalizedFirstName, normalizedLastName, numberOfUsernamesStartingWithSameFirstAndLastName);
        }
    }

}
