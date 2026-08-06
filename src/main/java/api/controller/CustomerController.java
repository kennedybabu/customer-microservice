package api.controller;


import api.dto.CustomerDTO;
import api.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> findAllCustomers() {
        List<CustomerDTO> customers = customerService.findAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO newCustomer = customerService.addCustomer(customerDTO);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{accountNumber}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable String accountNumber,
                                                      @RequestBody CustomerDTO customerDTO) throws Exception {
        CustomerDTO updatedCustomer = customerService.updateCustomer(accountNumber,customerDTO);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String accountNumber) throws Exception {
        customerService.deleteCustomer(accountNumber);
        return new ResponseEntity<>("Record deleted", HttpStatus.OK);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<CustomerDTO> findByCustomerAccountNumber(@PathVariable String accountNumber) {
        CustomerDTO customer = customerService.findCustomerByAccountNumber(accountNumber);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.findCustomerId(id);

        if(customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/count")
    public ResponseEntity<String> countCustomers() {
        long count = customerService.findAllCustomers().size();
        String message = "The number of customer(s) is " + count;
        return ResponseEntity.ok(message);
    }
}
