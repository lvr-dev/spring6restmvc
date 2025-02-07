package guru.springframework.spring6restmvc.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

import guru.springframework.spring6restmvc.models.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImp;

import static org.mockito.BDDMockito.given;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  CustomerService customerSerice;

  CustomerServiceImp customerServiceImpl = new CustomerServiceImp();

  @Test
  void getCustomerById() throws Exception {
    Customer testCustomer = customerServiceImpl.listCustomers().get(0);

    given(customerSerice.getCustomerById(testCustomer.getId())).willReturn(testCustomer);
    
    mockMvc.perform(get("/api/v1/customers/" + testCustomer.getId())
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
      .andExpect(jsonPath("$.customerName", is(testCustomer.getCustomerName())));
  }

  @Test
  void listCustomers() throws Exception {
    given(customerSerice.listCustomers()).willReturn(customerServiceImpl.listCustomers());

    mockMvc.perform(get("/api/v1/customers")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.length()", is(3)));
  }

}
