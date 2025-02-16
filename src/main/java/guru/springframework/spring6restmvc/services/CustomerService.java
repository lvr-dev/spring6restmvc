package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import guru.springframework.spring6restmvc.models.CustomerDTO;

public interface CustomerService {
  List<CustomerDTO> listCustomers();

  Optional<CustomerDTO> getCustomerById(UUID customerId);

  CustomerDTO saveNewCustomer(CustomerDTO customer);

  Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer);

  Boolean deleteCustomerById(UUID customerId);

  Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer);
}
