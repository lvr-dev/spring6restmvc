package guru.springframework.spring6restmvc.controllers;

import java.util.UUID;

import org.springframework.stereotype.Controller;

import guru.springframework.spring6restmvc.models.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Controller
public class BeerController {
  private final BeerService beerService;

  public Beer getBeerById(UUID beerId) {

    log.debug("get beer by id in controller");

    return beerService.getBeerById(beerId);
  }
}
