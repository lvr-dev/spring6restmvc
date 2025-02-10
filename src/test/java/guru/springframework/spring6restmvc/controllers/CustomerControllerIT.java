package guru.springframework.spring6restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.models.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
public class CustomerControllerIT {

  @Autowired
  CustomerController customerController;

  @Autowired
  CustomerRepository customerRepository;

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


}
