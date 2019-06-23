package gov.ita.terrafreights.tariff.country;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CountryController {

  private CountryRepository countryRepository;

  public CountryController(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
  }

  @GetMapping("/api/countries")
  public List<Country> countries() {
    return countryRepository.findAll();
  }

  @PutMapping("/api/country")
  public Country save(@RequestBody Country country) {
    return countryRepository.save(country);
  }
}
