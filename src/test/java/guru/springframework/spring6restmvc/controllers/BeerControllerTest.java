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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;


import guru.springframework.spring6restmvc.models.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;



@WebMvcTest(BeerController.class)
public class BeerControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  BeerService beerService;

  BeerServiceImpl beerServiceImpl;

  @Captor
  ArgumentCaptor<UUID> uuidArgumentCaptor;

  @Captor
  ArgumentCaptor<Beer> beerArgumentCaptor;

  @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
  }

  @Test
  void testGetBeerById() throws Exception {
    Beer beer = beerServiceImpl.listBeers().get(0);

    given(beerService.getBeerById(beer.getId())).willReturn(beer);
    
    mockMvc.perform(get(BeerController.BEER_PATH + "/" + beer.getId())
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id", is(beer.getId().toString())))
      .andExpect(jsonPath("$.beerName", is(beer.getBeerName())));
  }

  @Test
  void testListBeers() throws Exception {
    given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

    mockMvc.perform(get(BeerController.BEER_PATH)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.length()", is(3)));
  }

  @Test
  void testCreateNewBeer() throws Exception {
      Beer beer = beerServiceImpl.listBeers().get(0);
      beer.setVersion(null);
      beer.setId(null);

      given(beerService.saveNewBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().get(1));

      mockMvc.perform(post(BeerController.BEER_PATH)
              .accept(MediaType.APPLICATION_JSON)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(beer)))
              .andExpect(status().isCreated())
              .andExpect(header().exists("Location"));
    }

  @Test
  void testUpdateBeer() throws Exception {
    Beer beer = beerServiceImpl.listBeers().get(0);

    mockMvc.perform(put(BeerController.BEER_PATH + "/" + beer.getId())
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(beer)));

    verify(beerService).updateBeerById(any(UUID.class), any(Beer.class));
  }

  @Test
  void testDeleteById() throws Exception {
    Beer beer = beerServiceImpl.listBeers().get(0);

    mockMvc.perform(delete(BeerController.BEER_PATH + "/" + beer.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
       
    verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
    assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
  }

  @Test
  void testPatchBeer() throws Exception {
    Beer beer = beerServiceImpl.listBeers().get(0);

    Map<String, Object> beerMap = new HashMap<>();
    beerMap.put("beerName", "Grolsch");

    mockMvc.perform(patch(BeerController.BEER_PATH + "/" + beer.getId())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(beerMap)))
      .andExpect(status().isNoContent());

    verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

    assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
  }
}
