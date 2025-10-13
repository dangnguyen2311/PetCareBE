package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.customer.CreateCustomerRequest;
import org.example.petcarebe.dto.request.customer.UpdateCustomerRequest;
import org.example.petcarebe.dto.response.customer.CustomerRespone;
import org.example.petcarebe.model.Customer;
import org.example.petcarebe.repository.CustomerRepository;
import org.example.petcarebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public CustomerRespone createCustomer(CreateCustomerRequest request) {
        Boolean isEmailExists = customerRepository.existsCustomerByEmail(request.getEmail());
        Boolean isPhoneExists = customerRepository.existsCustomerByPhone(request.getPhone());
        if (isEmailExists || isPhoneExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }


        Customer customer = Customer.builder()
                .fullname(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .createdDate(LocalDate.now())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .status("ACTIVE")
                .build();


        Customer savedCustomer = customerRepository.save(customer);
        return convertToResponse(savedCustomer,  "Customer created successfully");
    }

    public List<CustomerRespone> getAllCustomers() {
        return convertToListResponse(customerRepository.findAllByStatus(("ACTIVE")));
    }

    public CustomerRespone getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return convertToResponse(customer, "Customer found successfully");
    }

    public CustomerRespone getCustomerByClientId(String clientId) {
        System.out.println("ClientId dang nhap dang ky kham: " + clientId);
        Customer customer = customerRepository.findByClientId(clientId)
                .orElse(null);
        if (customer == null) {
            customer = new Customer();
            customer.setClientId(clientId);
            Customer savedCustomer = customerRepository.save(customer);
            return convertToResponse(savedCustomer, "Customer found successfully");
        }
        else return convertToResponse(customer, "Customer found successfully");
    }

    public CustomerRespone updateCustomer(Long customerId, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setFullname(request.getFullName());
        customer.setAddress(request.getAddress());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setClientId(request.getClientId());
        customer.setStatus("ACTIVE");

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToResponse(updatedCustomer,  "Customer created successfully");
    }

    public CustomerRespone updateCustomerByClientId(String clientId, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setFullname(request.getFullName());
        customer.setAddress(request.getAddress());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setClientId(request.getClientId());
        customer.setStatus("ACTIVE");

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToResponse(updatedCustomer,  "Customer created successfully");
    }

    public void deleteCustomer(Long customerId) {
        Optional<Customer> customerToDelete = customerRepository.findById(customerId);
        if (customerToDelete.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Customer customer = customerToDelete.get();
        customer.setStatus("INACTIVE");
        customerRepository.save(customer);
    }

//    private CustomerRespone convertToResponse(Customer customer, String message) {
//        return new CustomerRespone(
//                customer.getId(),
//                customer.getClientId(),
//                customer.getFullname() != null ?  customer.getFullname() : "",
//                customer.getEmail() != null ? customer.getEmail() : "",
//                customer.getPhone() != null ? customer.getPhone() : "",
//                customer.getGender() != null ? customer.getGender() : "",
//                customer.getAddress()  != null ? customer.getAddress() : "",
//                customer.getDateOfBirth() != null ? customer.getDateOfBirth() : LocalDate.of(2020, 10, 10),
//                customer.getStatus(),
//                message
//        );
//    }
    private CustomerRespone convertToResponse(Customer customer, String message) {
        return new CustomerRespone(
                customer.getId(),
                customer.getClientId(),
                customer.getFullname(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getGender(),
                customer.getAddress(),
                customer.getDateOfBirth(),
                customer.getStatus(),
                message
        );
    }

    private CustomerRespone convertToResponse(Customer customer) {
        return new CustomerRespone(
                customer.getId(),
                customer.getClientId(),
                customer.getFullname(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getGender(),
                customer.getAddress(),
                customer.getDateOfBirth(),
                customer.getStatus(), "");
    }

    private List<CustomerRespone> convertToListResponse(List<Customer> customerList) {
        return customerList.stream().map(this::convertToResponse).collect(Collectors.toList());
    }



}

