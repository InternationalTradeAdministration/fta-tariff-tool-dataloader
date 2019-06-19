package gov.ita.terrafreights.staging_basket;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StagingBasketController {

  private StagingBasketRepository stagingBasketRepository;

  public StagingBasketController(StagingBasketRepository stagingBasketRepository) {
    this.stagingBasketRepository = stagingBasketRepository;
  }

  @GetMapping("/api/staging_baskets")
  public List<StagingBasket> stagingBaskets() {
    return stagingBasketRepository.findAll();
  }
}
