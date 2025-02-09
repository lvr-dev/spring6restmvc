package guru.springframework.spring6restmvc.mappers;

import org.mapstruct.Mapper;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.models.CustomerDTO;

@Mapper
public interface CustomerMapper {

  Customer customerDtoToCustomer(CustomerDTO dto);

  CustomerDTO customerToCustomerDto(Customer customer);
  
}
