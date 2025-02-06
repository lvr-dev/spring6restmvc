package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import guru.springframework.spring6restmvc.models.Customer;

@Service
public class CustomerServiceImp implements CustomerService {

  private Map<UUID, Customer> customerMap;

  public CustomerServiceImp() {

    this.customerMap = new HashMap<>();

    Customer customer1 = Customer.builder()
        .id(UUID.randomUUID())
        .customerName("John Doe")
        .version("1")
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Customer customer2 = Customer.builder()
        .id(UUID.randomUUID())
        .customerName("Jane Doe")
        .version("1")
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Customer customer3 = Customer.builder()
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
  public List<Customer> listCustomers() {
    return new ArrayList<>(customerMap.values());
  }

  @Override
  public Customer getCustomerById(UUID customerId) {
    return customerMap.get(customerId);

  }

  @Override
  public Customer saveNewCustomer(Customer customer) {
    Customer savedCustomer = Customer.builder()
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
  public void updateCustomer(UUID customerId, Customer customer) {
    Customer updatedCustomer = customerMap.get(customerId);
    updatedCustomer.setCustomerName(customer.getCustomerName());
    updatedCustomer.setUpdateDate(LocalDateTime.now());
    customerMap.put(customerId, updatedCustomer);
  }

  @Override
  public void deleteCustomerById(UUID customerId) {
    customerMap.remove(customerId);
  }

  @Override
  public void patchCustomerById(UUID customerId, Customer customer) {
    Customer existingCustomer = customerMap.get(customerId);

    if (StringUtils.hasText(existingCustomer.getCustomerName())) {
      existingCustomer.setCustomerName(customer.getCustomerName());
    }

    existingCustomer.setUpdateDate(LocalDateTime.now());
  }
}