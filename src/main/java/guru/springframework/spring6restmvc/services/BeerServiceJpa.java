package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.models.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJpa implements BeerService {

  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;

  @Override
  public Optional<BeerDTO> getBeerById(UUID beerId) {
    return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository
      .findById(beerId).orElse(null)));
  }

  @Override
  public List<BeerDTO> listBeers() {
    return beerRepository.findAll()
      .stream()
      .map(beerMapper::beerToBeerDto)
      .collect(Collectors.toList());
  }

  @Override
  public void deleteBeerById(UUID beerId) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void patchBeerById(UUID beerId, BeerDTO beer) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public BeerDTO saveNewBeer(BeerDTO beer) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updateBeerById(UUID beerId, BeerDTO beer) {
    // TODO Auto-generated method stub
    
  }

}
