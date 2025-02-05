package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.UUID;

import guru.springframework.spring6restmvc.models.Beer;

public interface BeerService {

  List<Beer> listBeers();

  Beer getBeerById(UUID beerId);
}
