package guru.springframework.spring6restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.models.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
public class CustomerController {
  private final CustomerService customerService;

  @RequestMapping("api/v1/customers")
  public List<Customer> getCustomers() {
    return customerService.listCustomers();
  }

  @RequestMapping("api/v1/customers/{customerId}")
  public Customer getCustomer(@PathVariable("customerId") UUID customerId) {
    log.debug("Inside getCustomerId in the CustomerController");
    return customerService.getCustomerById(customerId);
  }

}
