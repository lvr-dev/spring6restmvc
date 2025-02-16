package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import guru.springframework.spring6restmvc.models.BeerDTO;

public interface BeerService {

  List<BeerDTO> listBeers();

  Optional<BeerDTO> getBeerById(UUID beerId);

  BeerDTO saveNewBeer(BeerDTO beer);

  Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer);

  Boolean deleteBeerById(UUID beerId);

  Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer);
}
