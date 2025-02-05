package guru.springframework.spring6restmvc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.models.Beer;
import guru.springframework.spring6restmvc.models.BeerStyle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

  @Override
  public Beer getBeerById(UUID beerId) {

    log.debug("getBeerById in beer service was called");
    return Beer.builder()
        .id(beerId)
        .version(1)
        .beerName("Galaxy Cat")
        .beerStyle(BeerStyle.PALE_ALE)
        .upc("33701")
        .price(new BigDecimal(10.35))
        .quantityOnHand(120)
        .createdDate(LocalDateTime.now())
        .lastModifiedDate(LocalDateTime.now())
        .build();
  }
}
