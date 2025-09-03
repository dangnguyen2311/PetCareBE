package org.example.petcarebe.service;

import org.example.petcarebe.dto.CustomerDto;
import org.example.petcarebe.model.Customer;
import org.example.petcarebe.model.User;
import org.example.petcarebe.repository.CustomerRepository;
import org.example.petcarebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public CustomerDto createCustomer(CustomerDto customerDto) {
        User user = userRepository.findById(customerDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Customer customer = new Customer();
        customer.setFullname(customerDto.getFullname());
        customer.setPhone(customerDto.getPhone());
        customer.setEmail(customerDto.getEmail());
        customer.setAddress(customerDto.getAddress());
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        customer.setCreatedDate(LocalDate.now());
        customer.setStatus("active");
        customer.setUser(user);

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CustomerDto getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return convertToDto(customer);
    }

    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        User user = userRepository.findById(customerDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        customer.setFullname(customerDto.getFullname());
        customer.setPhone(customerDto.getPhone());
        customer.setEmail(customerDto.getEmail());
        customer.setAddress(customerDto.getAddress());
        customer.setUser(user);

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDto(updatedCustomer);
    }

    public void deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("Customer not found");
        }
        customerRepository.deleteById(customerId);
    }

    private CustomerDto convertToDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getFullname(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getUsername(),
                null, // Do not expose password
                customer.getCreatedDate(),
                customer.getStatus(),
                customer.getUser().getId()
        );
    }
}

