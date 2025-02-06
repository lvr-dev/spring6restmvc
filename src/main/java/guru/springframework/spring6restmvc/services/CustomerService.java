package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.UUID;

import guru.springframework.spring6restmvc.models.Customer;

public interface CustomerService {
  List<Customer> listCustomers();

  Customer getCustomerById(UUID customerId);

  Customer saveNewCustomer(Customer customer);

  void updateCustomer(UUID customerId, Customer customer);
}
