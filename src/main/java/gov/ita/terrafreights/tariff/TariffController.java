package gov.ita.terrafreights.tariff;

import gov.ita.terrafreights.tariff.stagingbasket.StagingBasket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;

@RestController
public class TariffController {

  private final TariffRepository tariffRepository;
  private TariffCsvTranslator tariffCsvTranslator;
  private final TariffPersister tariffPersister;

  public TariffController(TariffRepository tariffRepository,
                          TariffCsvTranslator tariffCsvTranslator,
                          TariffPersister tariffPersister) {
    this.tariffRepository = tariffRepository;
    this.tariffCsvTranslator = tariffCsvTranslator;
    this.tariffPersister = tariffPersister;
  }

  @GetMapping("/api/tariffs")
  public Page<Tariff> tariffs(Pageable pageable,
                              @RequestParam("countryCode") String countryCode,
                              @RequestParam("stagingBasketId") Long stagingBasketId,
                              @RequestParam(value = "tariffLine", defaultValue = "") String tariffLine) {
    if (stagingBasketId != -1 && tariffLine.equals(""))
      return tariffRepository.findByCountryCodeAndStagingBasketId(countryCode, stagingBasketId, pageable);

    if (stagingBasketId != -1)
      return tariffRepository.findByCountryCodeAndStagingBasketIdAndTariffLineContaining(
        countryCode, stagingBasketId, tariffLine, pageable);

    if (!tariffLine.equals(""))
      return tariffRepository.findByCountryCodeAndTariffLineContaining(countryCode, tariffLine, pageable);

    return tariffRepository.findByCountryCode(countryCode, pageable);
  }

  @GetMapping("/api/tariffs/all")
  public List<Tariff> tariffs(Pageable pageable, @RequestParam("countryCode") String countryCode) {
    return tariffRepository.findByCountryCode(countryCode);
  }

  @GetMapping("/api/tariff")
  public Optional<Tariff> tariff(@RequestParam("tariffId") Long tariffId) {
    return tariffRepository.findById(tariffId);
  }

  @GetMapping("/api/tariff/staging_baskets")
  public List<StagingBasket> stagingBaskets(@RequestParam("countryCode") String countryCode) {
    return tariffRepository.findAllStagingBasketsByCountry(countryCode);
  }

  @GetMapping("/api/tariff/counts_by_country")
  public List<TariffCount> tariffCountsByCountry() {
    return tariffRepository.tariffCountsByCountry();
  }

  @PutMapping("/api/tariffs/save")
  public void saveTariffs(@RequestParam("countryCode") String countryCode,
                          @RequestBody TariffUpload tariffUpload) {
    tariffRepository.deleteByCountry(countryCode);
    List<Tariff> tariffs = tariffCsvTranslator.translate(countryCode, new StringReader(tariffUpload.csv));
    tariffPersister.persist(tariffs);
  }
}
