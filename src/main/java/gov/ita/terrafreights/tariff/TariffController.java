package gov.ita.terrafreights.tariff;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TariffController {

  private TariffRepository tariffRepository;

  public TariffController(TariffRepository tariffRepository) {
    this.tariffRepository = tariffRepository;
  }

  @GetMapping("/api/tariffs")
  public Page<Tariff> tariffs(Pageable pageable, @RequestParam("gov/ita/terrafreights/country") String country) {
    return tariffRepository.findByCountry(country, pageable);
  }
}
