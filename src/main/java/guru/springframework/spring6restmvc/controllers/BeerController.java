package guru.springframework.spring6restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.models.BeerDTO;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {
  private final BeerService beerService;

  public static final String BEER_PATH = "/api/v1/beers";
  public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";


  @GetMapping(BEER_PATH)
  public List<BeerDTO> listBeers() {
    return beerService.listBeers();
  }

  @GetMapping(BEER_PATH_ID)
  public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {

    log.debug("get beer by id in controller.");

    return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
  }

  @PostMapping(BEER_PATH)
  public ResponseEntity handlePost(@Validated @RequestBody BeerDTO beer) {
    BeerDTO savedBeer = beerService.saveNewBeer(beer);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Location", "api/v1/beers/" + savedBeer.getId().toString());

    return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
  }

  @PutMapping(BEER_PATH_ID)
  public ResponseEntity handlePut(@PathVariable UUID beerId, @Validated @RequestBody BeerDTO beer) {

    if (beerService.updateBeerById(beerId, beer).isEmpty()) {
      throw new NotFoundException();
    }
   
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(BEER_PATH_ID)
  public ResponseEntity deleteById(@PathVariable UUID beerId) {
    if (!beerService.deleteBeerById(beerId)) {
      throw new NotFoundException();
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PatchMapping(BEER_PATH_ID)
  public ResponseEntity handlePatch(@PathVariable UUID beerId, @RequestBody BeerDTO beer) {
    beerService.patchBeerById(beerId, beer);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }



}
