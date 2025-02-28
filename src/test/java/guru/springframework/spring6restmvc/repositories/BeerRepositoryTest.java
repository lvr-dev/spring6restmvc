package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.models.BeerStyle;
import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

@DataJpaTest
class BeerRepositoryTest {

  @Autowired
  BeerRepository beerRepository;

  @Test
  void testSaveBeer() {
    Beer savedBeer = beerRepository.save(Beer.builder()
        .beerName("My Beer")
        .beerStyle(BeerStyle.SAISON)
        .upc("99f9fgfdgfdg")
        .price(new BigDecimal("2.49"))
        .build());

    beerRepository.flush();
    assertThat(savedBeer).isNotNull();
    assertThat(savedBeer.getId()).isNotNull();
  }

  @Test
  void testSaveBeerNameTooLong() {

    assertThrows(ConstraintViolationException.class, () -> {
        beerRepository.save(Beer.builder()
        .beerName("My Beer vsgofiugofugofdgodgofjgodfugofduogudobjodfuofugfugdfg")
        .beerStyle(BeerStyle.SAISON)
        .upc("99f9fgfdgfdg")
        .price(new BigDecimal("2.49"))
        .build());

        beerRepository.flush();
    });
  }
}