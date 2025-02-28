package guru.springframework.spring6restmvc.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.core.Is.is;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.models.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
public class BeerControllerIT {
  @Autowired
  BeerController beerController;

  @Autowired
  BeerRepository beerRepository;

  @Autowired
  BeerMapper beerMapper;

  @Autowired
  WebApplicationContext wac;

  @Autowired
  ObjectMapper objectMapper;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  void testPatchBeerBadName() throws Exception {
    Beer beer = beerRepository.findAll().get(0);

    Map<String, Object> beerMap = new HashMap<>();
    beerMap.put("beerName", "Grolsch dksjgsdjoffobouiuiufogdofugdfougofgdfdgfgbooo");

    mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(beerMap)))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()", is(1)));
  }


  @Test
  void testListBeers() {
    List<BeerDTO> dtos = beerController.listBeers();

    assertThat(dtos.size()).isEqualTo(3);
  }

  @Rollback
  @Transactional
  @Test
  void testEmptyList() {
    beerRepository.deleteAll();

    List<BeerDTO> dtos = beerController.listBeers();

    assertThat(dtos.size()).isEqualTo(0);
  }

  @Test
  void testGetBeerById() {
    Beer beer = beerRepository.findAll().get(0);

    BeerDTO beerDto = beerController.getBeerById(beer.getId());

    assertThat(beerDto).isNotNull();
  }

  @Test
  void testGetBeerByIdNotFound() {
    assertThrows(NotFoundException.class, () -> {
      beerController.getBeerById(UUID.randomUUID());
    });
  }

  @Rollback
  @Transactional
  @Test
  void testSaveNewBeer() {
    BeerDTO beerDto = BeerDTO.builder()
                        .beerName("Jopen")
                        .build();
    ResponseEntity responseEntity = beerController.handlePost(beerDto);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

    String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
    UUID savedUUID = UUID.fromString(locationUUID[3]);

    Beer beer = beerRepository.findById(savedUUID).get();
    assertThat(beer).isNotNull();
  }

  @Test
  void testUpdateBeer() {
    Beer beer = beerRepository.findAll().get(0);

    BeerDTO beerDto = beerMapper.beerToBeerDto(beer);
    beerDto.setId(null);
    beerDto.setVersion(null);
    final String beerName = "Updated beer";
    beerDto.setBeerName(beerName);

    ResponseEntity responseEntity = beerController.handlePut(beer.getId(), beerDto);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    Beer updatedBeer = beerRepository.findById(beer.getId()).get();
    assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
  }

  @Test
  void testUpdateBeerNotFound() {
    assertThrows(NotFoundException.class, () -> {
      beerController.handlePut(UUID.randomUUID(), BeerDTO.builder().build());
    });
  }

  @Rollback
  @Transactional
  @Test
  void testDeleteById() {
    Beer beer = beerRepository.findAll().get(0);
    ResponseEntity responseEntity = beerController.deleteById(beer.getId());

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
    assertThat(beerRepository.findById(beer.getId())).isEmpty();
  }

  @Test
  void testDeleteBeerByIdNotFound() {
    assertThrows(NotFoundException.class, () -> {
      beerController.deleteById(UUID.randomUUID());
    });
  }
  
}
