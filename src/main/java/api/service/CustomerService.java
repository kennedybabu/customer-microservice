package api.service;

import api.dto.CustomerDTO;
import api.exceptions.CustomerNotFoundException;
import api.exceptions.InvalidCustomerException;
import api.model.Customer;
import api.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerDTO> findAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CustomerDTO addCustomer(CustomerDTO customerDTO) {
        Customer entity = toEntity(customerDTO);
        Customer saved = customerRepository.save(entity);
        return toDto(saved);
    }

    public CustomerDTO updateCustomer(String accountNumber, CustomerDTO customerDTO) throws Exception {
        Customer existing = findCustomerByAccountNumberEntity(accountNumber);

        if(!customerDTO.getAccountNumber().equals(existing.getAccountNumber())) {
            throw new InvalidCustomerException("Invalid - Cannot change account number");
        }

        if(customerDTO.getName() == null) {
            throw new InvalidCustomerException("Invalid Customer: name must not be null");
        }

        existing.setName(customerDTO.getName());
        existing.setEmail(customerDTO.getEmail());
        Customer updated = customerRepository.save(existing);
        return toDto(updated);
    }

    @Transactional
    public void deleteCustomer(String accountNumber) throws Exception {
        customerRepository.deleteByAccountNumber(accountNumber);
    }

    public CustomerDTO findCustomerId(Long id) {
        Customer entity = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("No customer found with id: " + id));
        return toDto(entity);
    }

    public CustomerDTO findCustomerByAccountNumber(String accountNumber) {
        Customer entity = findCustomerByAccountNumberEntity(accountNumber);
        return toDto(entity);
    }

    private Customer findCustomerByAccountNumberEntity(String accountNumber) {
        return customerRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with account number : " + accountNumber));
    }

    public CustomerDTO toDto(Customer entity) {
        return new CustomerDTO(entity.getAccountNumber(),entity.getName(), entity.getEmail());
    }

    public Customer toEntity(CustomerDTO dto) {
        return new Customer(dto.getAccountNumber(), dto.getName(),dto.getEmail());
    }
}
