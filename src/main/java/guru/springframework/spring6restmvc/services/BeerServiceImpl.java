package guru.springframework.spring6restmvc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import guru.springframework.spring6restmvc.models.BeerDTO;
import guru.springframework.spring6restmvc.models.BeerStyle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

  private Map<UUID, BeerDTO> beerMap;

  public BeerServiceImpl() {

    this.beerMap = new HashMap<>();

    BeerDTO beer1 = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName("Galaxy Cat")
        .beerStyle(BeerStyle.PALE_ALE)
        .upc("12356")
        .price(new BigDecimal("12.99"))
        .quantityOnHand(122)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    BeerDTO beer2 = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName("Crank")
        .beerStyle(BeerStyle.PALE_ALE)
        .upc("12356222")
        .price(new BigDecimal("11.99"))
        .quantityOnHand(392)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    BeerDTO beer3 = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName("Sunshine City")
        .beerStyle(BeerStyle.IPA)
        .upc("12356")
        .price(new BigDecimal("13.99"))
        .quantityOnHand(144)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    beerMap.put(beer1.getId(), beer1);
    beerMap.put(beer2.getId(), beer2);
    beerMap.put(beer3.getId(), beer3);
  }

  @Override
  public List<BeerDTO> listBeers() {
    return new ArrayList<>(beerMap.values());
  }

  @Override
  public Optional<BeerDTO> getBeerById(UUID beerId) {
    return Optional.of(beerMap.get(beerId));
  }

  @Override
  public BeerDTO saveNewBeer(BeerDTO beer) {
    BeerDTO newBeer = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName(beer.getBeerName())
        .beerStyle(beer.getBeerStyle())
        .upc(beer.getUpc())
        .price(beer.getPrice())
        .quantityOnHand(beer.getQuantityOnHand())
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    beerMap.put(newBeer.getId(), newBeer);
    return newBeer;
  }

  @Override
  public void updateBeerById(UUID beerId, BeerDTO beer) {
    BeerDTO existingBeer = beerMap.get(beerId);
    existingBeer.setBeerName(beer.getBeerName());
    existingBeer.setPrice(beer.getPrice());
    existingBeer.setUpc(beer.getUpc());
    existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
    existingBeer.setUpdateDate(LocalDateTime.now());
    beerMap.put(beerId, existingBeer);
  }

  @Override
  public void deleteBeerById(UUID beerId) {
    beerMap.remove(beerId);
  }

  @Override
  public void patchBeerById(UUID beerId, BeerDTO beer) {
    BeerDTO existing = beerMap.get(beerId);

    if (StringUtils.hasText(beer.getBeerName())) {
      existing.setBeerName(beer.getBeerName());
    }

    if (beer.getBeerStyle() != null) {
      existing.setBeerStyle(beer.getBeerStyle());
    }

    if (beer.getPrice() != null) {
      existing.setPrice(beer.getPrice());
    }

    if (beer.getQuantityOnHand() != null) {
      existing.setQuantityOnHand(beer.getQuantityOnHand());
    }

    if (StringUtils.hasText(beer.getUpc())) {
      existing.setUpc(beer.getUpc());
    }
  }
}
