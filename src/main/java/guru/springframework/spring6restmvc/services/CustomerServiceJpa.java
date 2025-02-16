package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
  public Boolean deleteCustomerById(UUID customerId) {
    if (customerRepository.existsById(customerId)) {
      customerRepository.deleteById(customerId);
      return true;
    }
    return false;
  }

  @Override
  public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
    AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

    customerRepository.findById(customerId).ifPresentOrElse(foundCustomer -> {
      if (StringUtils.hasText(customer.getCustomerName())) {
        foundCustomer.setName(customer.getCustomerName());
      }
      atomicReference.set(Optional.of(customerMapper
          .customerToCustomerDto(customerRepository.save(foundCustomer))));
    }, () -> {
      atomicReference.set(Optional.empty());
    });

    return atomicReference.get();
  }

  @Override
  public CustomerDTO saveNewCustomer(CustomerDTO customer) {
    return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customer)));
  }

  @Override
  public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
    AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
    customerRepository.findById(customerId).ifPresentOrElse(
      foundCustomer -> {
        foundCustomer.setName(customer.getCustomerName());
        atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(foundCustomer))));
      }, () -> {
        atomicReference.set(Optional.empty());
      });
    return atomicReference.get();
  }

}
