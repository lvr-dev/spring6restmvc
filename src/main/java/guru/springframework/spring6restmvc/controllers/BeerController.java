package guru.springframework.spring6restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.models.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {
  private final BeerService beerService;

  @RequestMapping(method = RequestMethod.GET)
  public List<Beer> listBeers() {
    return beerService.listBeers();
  }

  @RequestMapping(value = "{beerId}", method = RequestMethod.GET)
  public Beer getBeerById(@PathVariable("beerId") UUID beerId) {

    log.debug("get beer by id in controller.");

    return beerService.getBeerById(beerId);
  }

  @PostMapping
  public ResponseEntity handlePost(@RequestBody Beer beer) {
    Beer savedBeer = beerService.saveNewBeer(beer);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Location", "api/v1/beers/" + savedBeer.getId().toString());

    return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
  }
}
