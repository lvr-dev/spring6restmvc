package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import guru.springframework.spring6restmvc.models.CustomerDTO;

public interface CustomerService {
  List<CustomerDTO> listCustomers();

  Optional<CustomerDTO> getCustomerById(UUID customerId);

  CustomerDTO saveNewCustomer(CustomerDTO customer);

  void updateCustomer(UUID customerId, CustomerDTO customer);

  void deleteCustomerById(UUID customerId);

  void patchCustomerById(UUID customerId, CustomerDTO customer);
}
