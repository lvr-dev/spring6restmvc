package guru.springframework.spring6restmvc.mappers;

import org.mapstruct.Mapper;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.models.BeerDTO;

@Mapper
public interface BeerMapper {

  Beer beerDtotoBeer(BeerDTO dto);

  BeerDTO beerToBeerDto(Beer beer);
  

}
