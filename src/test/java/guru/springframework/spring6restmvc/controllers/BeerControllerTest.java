package guru.springframework.spring6restmvc.controllers;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BeerControllerTest {

  @Autowired
  BeerController beerController;

  @Test
  void testGetBeerById() {
    System.out.println(beerController.getBeerById(UUID.randomUUID()));
  }
}
