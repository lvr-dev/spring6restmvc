package guru.springframework.spring6restmvc.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.core.Is.is;

import guru.springframework.spring6restmvc.models.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  CustomerService customerService;

  CustomerServiceImp customerServiceImpl;

  @Captor
  ArgumentCaptor<UUID> uuidArgumentCaptor;

  @Captor
  ArgumentCaptor<Customer> customArgumentCaptor;

  @BeforeEach
  void setUp() {
    customerServiceImpl = new CustomerServiceImp();
  }

  @Test
  void testGetCustomerById() throws Exception {
    Customer customer = customerServiceImpl.listCustomers().get(0);

    given(customerService.getCustomerById(customer.getId())).willReturn(customer);
    
    mockMvc.perform(get(CustomerController.CUSTOMER_PATH + "/" + customer.getId())
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id", is(customer.getId().toString())))
      .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())));
  }

  @Test
  void testListCustomers() throws Exception {
    given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

    mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.length()", is(3)));
  }

  @Test
  void testHandlePost() throws Exception {
      Customer customer = customerServiceImpl.listCustomers().get(0);
      customer.setVersion(null);
      customer.setId(null);

      given(customerService.saveNewCustomer(any(Customer.class))).willReturn(customerServiceImpl.listCustomers().get(1));

      mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
              .accept(MediaType.APPLICATION_JSON)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(customer)))
              .andExpect(status().isCreated())
              .andExpect(header().exists("Location"));
  }

    @Test
    void testHandlePut() throws Exception {
      Customer customer = customerServiceImpl.listCustomers().get(0);

      mockMvc.perform(put(CustomerController.CUSTOMER_PATH + "/" + customer.getId())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(customer)));

      verify(customerService).updateCustomer(uuidArgumentCaptor.capture(), any(Customer.class));
      assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testDeleteById() throws Exception {
      Customer customer = customerServiceImpl.listCustomers().get(0);

      mockMvc.perform(delete(CustomerController.CUSTOMER_PATH + "/" + customer.getId())
          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
        
      verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());
      assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchCustomerById() throws Exception {
      Customer customer = customerServiceImpl.listCustomers().get(0);

      Map<String, Object> customerMap = new HashMap<>();
      customerMap.put("customerName", "Theo Janssen");

      mockMvc.perform(patch(CustomerController.CUSTOMER_PATH + "/" + customer.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerMap)))
            .andExpect(status().isNoContent());

      verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customArgumentCaptor.capture());
      assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
      assertThat(customerMap.get("customerName")).isEqualTo(customArgumentCaptor.getValue().getCustomerName());

    }

}
