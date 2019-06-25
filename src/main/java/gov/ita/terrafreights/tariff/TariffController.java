package gov.ita.terrafreights.tariff;

import gov.ita.terrafreights.tariff.product.ProductType;
import gov.ita.terrafreights.tariff.stagingbasket.StagingBasket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TariffController {

  private TariffRepository tariffRepository;

  public TariffController(TariffRepository tariffRepository) {
    this.tariffRepository = tariffRepository;
  }

  @GetMapping("/api/tariffs")
  public Page<Tariff> tariffs(Pageable pageable,
                              @RequestParam("countryCode") String countryCode,
                              @RequestParam("productTypeId") Long productTypeId,
                              @RequestParam("stagingBasketId") Long stagingBasketId) {


    if (productTypeId == -1 && stagingBasketId != -1)
      return tariffRepository.findByCountryCodeAndStagingBasketId(countryCode, stagingBasketId, pageable);

    if (productTypeId == -1)
      return tariffRepository.findByCountryCode(countryCode, pageable);

    if (stagingBasketId == -1)
      return tariffRepository.findByCountryCodeAndProductTypeId(countryCode, productTypeId, pageable);

    return tariffRepository.findByCountryCodeAndProductTypeIdAndStagingBasketId(countryCode, productTypeId, stagingBasketId, pageable);
  }

  @GetMapping("/api/product_types")
  public List<ProductType> productTypes(@RequestParam("countryCode") String countryCode) {
    return tariffRepository.findAllProductTypesByCountry(countryCode);
  }

  @GetMapping("/api/staging_baskets")
  public List<StagingBasket> stagingBaskets(@RequestParam("countryCode") String countryCode,
                                            @RequestParam("productTypeId") Long productTypeId) {
    if (productTypeId == -1)
      return tariffRepository.findAllStagingBasketsByCountry(countryCode);


    return tariffRepository.findAllStagingBasketsByCountryAndProductType(countryCode, productTypeId);
  }
}
