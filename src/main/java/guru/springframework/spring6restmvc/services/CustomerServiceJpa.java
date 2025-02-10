package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.models.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJpa implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;


  @Override
  public Optional<CustomerDTO> getCustomerById(UUID customerId) {
    return Optional.ofNullable(customerMapper.customerToCustomerDto(customerRepository
        .findById(customerId).orElse(null)));
  }

  @Override
  public List<CustomerDTO> listCustomers() {
    return customerRepository.findAll()
      .stream()
      .map(customerMapper::customerToCustomerDto)
      .collect(Collectors.toList());
  }

  @Override
  public void deleteCustomerById(UUID customerId) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void patchCustomerById(UUID customerId, CustomerDTO customer) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public CustomerDTO saveNewCustomer(CustomerDTO customer) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updateCustomer(UUID customerId, CustomerDTO customer) {
    // TODO Auto-generated method stub
    
  }

}
