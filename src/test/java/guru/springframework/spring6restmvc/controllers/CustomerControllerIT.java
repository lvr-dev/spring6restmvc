package guru.springframework.spring6restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.models.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
public class CustomerControllerIT {

  @Autowired
  CustomerController customerController;

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  CustomerMapper customerMapper;

  @Test
  void testListCustomers() {
    List<CustomerDTO> dtos = customerController.getCustomers();

    assertThat(dtos.size()).isEqualTo(3);
  }

  @Rollback
  @Transactional
  @Test
  void testEmptyListCustomers() {
    customerRepository.deleteAll();
    List<CustomerDTO> dtos = customerController.getCustomers();

    assertThat(dtos.size()).isEqualTo(0);
  }

  @Test
  void testGetCustomerById() {
    Customer customer = customerRepository.findAll().get(0);
    CustomerDTO customerDto = customerController.getCustomer(customer.getId());

    assertThat(customerDto).isNotNull();
  }

  @Test
  void testGetCustomerByIdNotFound() {
    assertThrows(NotFoundException.class, () -> {
      customerController.getCustomer(UUID.randomUUID());
    });
  }

  @Rollback
  @Transactional
  @Test
  void testSaveNewCustomer() {
    CustomerDTO customerDto = CustomerDTO.builder()
                                .customerName("Tina")
                                .build();
    ResponseEntity responseEntity = customerController.handlePost(customerDto);
    
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

    String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
    UUID savedUUID = UUID.fromString(locationUUID[3]);

    Customer customer = customerRepository.findById(savedUUID).get();
    assertThat(customer).isNotNull();
  }

  @Test
  void testUpdateCustomerById() {
    Customer customer = customerRepository.findAll().get(0);
    CustomerDTO customerDto = customerMapper.customerToCustomerDto(customer);
    customerDto.setId(null);
    customerDto.setVersion(null);
    final String customerName = "Michael";
    customerDto.setCustomerName(customerName);

    ResponseEntity responseEntity = customerController.handlePut(customer.getId(), customerDto);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
    assertThat(updatedCustomer.getName()).isEqualTo(customerName);
  }

  @Test
  void testUpdateCustomerNotFound() {
    assertThrows(NotFoundException.class, () -> {
      customerController.handlePut(UUID.randomUUID(), CustomerDTO.builder().build());
    });
  }

  @Rollback
  @Transactional
  @Test
  void testDeleteCustomerById() {
    Customer customer = customerRepository.findAll().get(0);
    ResponseEntity responseEntity = customerController.deleteCustomerById(customer.getId());

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
    assertThat(customerRepository.findById(customer.getId())).isEmpty();
  }


  @Test
  void testDeleteCustomerByIdNotFound() {
    assertThrows(NotFoundException.class, () -> {
      customerController.deleteCustomerById(UUID.randomUUID());
    });
  }
}
