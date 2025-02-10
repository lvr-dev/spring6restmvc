package guru.springframework.spring6restmvc.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.models.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
public class BeerControllerIT {
  @Autowired
  BeerController beerController;

  @Autowired
  BeerRepository beerRepository;

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

  
}
