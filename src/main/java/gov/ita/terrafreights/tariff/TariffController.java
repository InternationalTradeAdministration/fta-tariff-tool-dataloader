package gov.ita.terrafreights.tariff;

import gov.ita.terrafreights.tariff.stagingbasket.StagingBasket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class TariffController {

  private TariffRepository tariffRepository;

  public TariffController(TariffRepository tariffRepository) {
    this.tariffRepository = tariffRepository;
  }

  @GetMapping("/api/tariffs")
  public Page<Tariff> tariffs(Pageable pageable,
                              @RequestParam("countryCode") String countryCode,
                              @RequestParam("stagingBasketId") Long stagingBasketId,
                              @RequestParam(value = "tariffLine", defaultValue = "") String tariffLine) {
    if (stagingBasketId != -1 && tariffLine.equals(""))
      return tariffRepository.findByCountryCodeAndStagingBasketId(countryCode, stagingBasketId, pageable);

    if (stagingBasketId != -1)
      return tariffRepository.findByCountryCodeAndStagingBasketIdAndTariffLineContaining(countryCode, stagingBasketId, tariffLine, pageable);

    if (!tariffLine.equals(""))
      return tariffRepository.findByCountryCodeAndTariffLineContaining(countryCode, tariffLine, pageable);

    return tariffRepository.findByCountryCode(countryCode, pageable);
  }

  @GetMapping("/api/tariff")
  public Optional<Tariff> tariff(@RequestParam("tariffId") Long tariffId) {
    return tariffRepository.findById(tariffId);
  }

  @GetMapping("/api/staging_baskets")
  public List<StagingBasket> stagingBaskets(@RequestParam("countryCode") String countryCode) {
    return tariffRepository.findAllStagingBasketsByCountry(countryCode);
  }

  @GetMapping("/api/tariff_counts_by_country")
  public List<TariffCount> tariffCountsByCountry() {
    return tariffRepository.tariffCountsByCountry();
  }
}
