package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import guru.springframework.spring6restmvc.models.BeerDTO;

public interface BeerService {

  List<BeerDTO> listBeers();

  Optional<BeerDTO> getBeerById(UUID beerId);

  BeerDTO saveNewBeer(BeerDTO beer);

  void updateBeerById(UUID beerId, BeerDTO beer);

  void deleteBeerById(UUID beerId);

  void patchBeerById(UUID beerId, BeerDTO beer);
}
