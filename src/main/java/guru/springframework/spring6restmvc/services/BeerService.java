package guru.springframework.spring6restmvc.services;

import java.util.UUID;

import guru.springframework.spring6restmvc.models.Beer;

public interface BeerService {
  Beer getBeerById(UUID beerId);
}
