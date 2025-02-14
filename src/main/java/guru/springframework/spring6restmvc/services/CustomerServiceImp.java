package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import guru.springframework.spring6restmvc.models.CustomerDTO;

@Service
public class CustomerServiceImp implements CustomerService {

  private Map<UUID, CustomerDTO> customerMap;

  public CustomerServiceImp() {

    this.customerMap = new HashMap<>();

    CustomerDTO customer1 = CustomerDTO.builder()
        .id(UUID.randomUUID())
        .customerName("John Doe")
        .version("1")
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    CustomerDTO customer2 = CustomerDTO.builder()
        .id(UUID.randomUUID())
        .customerName("Jane Doe")
        .version("1")
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    CustomerDTO customer3 = CustomerDTO.builder()
        .id(UUID.randomUUID())
        .customerName("Tommy Cooper")
        .version("1")
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    this.customerMap.put(customer1.getId(), customer1);
    this.customerMap.put(customer2.getId(), customer2);
    this.customerMap.put(customer3.getId(), customer3);
  }

  @Override
  public List<CustomerDTO> listCustomers() {
    return new ArrayList<>(customerMap.values());
  }

  @Override
  public Optional<CustomerDTO> getCustomerById(UUID customerId) {
    return Optional.of(customerMap.get(customerId));

  }

  @Override
  public CustomerDTO saveNewCustomer(CustomerDTO customer) {
    CustomerDTO savedCustomer = CustomerDTO.builder()
        .id(UUID.randomUUID())
        .version("1")
        .customerName(customer.getCustomerName())
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    customerMap.put(savedCustomer.getId(), savedCustomer);
    return savedCustomer;
  }

  @Override
  public void updateCustomer(UUID customerId, CustomerDTO customer) {
    CustomerDTO updatedCustomer = customerMap.get(customerId);
    updatedCustomer.setCustomerName(customer.getCustomerName());
    updatedCustomer.setUpdateDate(LocalDateTime.now());
    customerMap.put(customerId, updatedCustomer);
  }

  @Override
  public void deleteCustomerById(UUID customerId) {
    customerMap.remove(customerId);
  }

  @Override
  public void patchCustomerById(UUID customerId, CustomerDTO customer) {
    CustomerDTO existingCustomer = customerMap.get(customerId);

    if (StringUtils.hasText(existingCustomer.getCustomerName())) {
      existingCustomer.setCustomerName(customer.getCustomerName());
    }

    existingCustomer.setUpdateDate(LocalDateTime.now());
  }
}