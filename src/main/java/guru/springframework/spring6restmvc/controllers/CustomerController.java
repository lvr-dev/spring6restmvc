package guru.springframework.spring6restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.models.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
  private final CustomerService customerService;

  @RequestMapping(method = RequestMethod.GET)
  public List<Customer> getCustomers() {
    return customerService.listCustomers();
  }

  @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
  public Customer getCustomer(@PathVariable("customerId") UUID customerId) {
    log.debug("Inside getCustomerId in the CustomerController");
    return customerService.getCustomerById(customerId);
  }

  @PostMapping
  public ResponseEntity handlePost(@RequestBody Customer customer) {
    Customer savedCustomer = customerService.saveNewCustomer(customer);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Location", "api/v1/customers/" + savedCustomer.getId().toString());
    return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
  }

  @PutMapping("{customerId}")
  public ResponseEntity handlePut(@PathVariable UUID customerId, @RequestBody Customer customer) {
    customerService.updateCustomer(customerId, customer);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}
