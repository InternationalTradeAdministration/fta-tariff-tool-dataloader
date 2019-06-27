package gov.ita.terrafreights.tariff.stagingbasket;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StagingBasketController {

  private StagingBasketRepository stagingBasketRepository;

  public StagingBasketController(StagingBasketRepository stagingBasketRepository) {

    this.stagingBasketRepository = stagingBasketRepository;
  }

  @GetMapping("/api/staging_baskets/all")
  public List<StagingBasket> stagingBaskets() {
    return this.stagingBasketRepository.findAll();
  }

}
