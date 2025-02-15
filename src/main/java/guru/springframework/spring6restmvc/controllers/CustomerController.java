package guru.springframework.spring6restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.models.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
public class CustomerController {
  private final CustomerService customerService;

  public static final String CUSTOMER_PATH = "/api/v1/customers";
  public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

  @GetMapping(CUSTOMER_PATH)
  public List<CustomerDTO> getCustomers() {
    return customerService.listCustomers();
  }

  @GetMapping(CUSTOMER_PATH_ID)
  public CustomerDTO getCustomer(@PathVariable("customerId") UUID customerId) {
    log.debug("Inside getCustomerId in the CustomerController");
    return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
  }

  @PostMapping(CUSTOMER_PATH)
  public ResponseEntity handlePost(@RequestBody CustomerDTO customer) {
    CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Location", "api/v1/customers/" + savedCustomer.getId().toString());
    return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
  }

  @PutMapping(CUSTOMER_PATH_ID)
  public ResponseEntity handlePut(@PathVariable UUID customerId, @RequestBody CustomerDTO customer) {
    if (customerService.updateCustomerById(customerId, customer).isEmpty()) {
      throw new NotFoundException();
    }
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(CUSTOMER_PATH_ID)
  public ResponseEntity deleteCustomerById(@PathVariable UUID customerId) {
    if (!customerService.deleteCustomerById(customerId)) {
      throw new NotFoundException();
    }
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PatchMapping(CUSTOMER_PATH_ID)
  public ResponseEntity patchCustomerById(@PathVariable UUID customerId, @RequestBody CustomerDTO customer) {
    customerService.patchCustomerById(customerId, customer);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}
