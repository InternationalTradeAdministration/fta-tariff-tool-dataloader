package gov.ita.terrafreights.country;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CountryController {

  @GetMapping("/api/countries")
  public List<Country> countries() {
    List<Country> countries = new ArrayList<>();
    countries.add(Country.builder().countryCode("AU").countryName("Australia").elasticFreshenUrl("http://something").build());
    countries.add(Country.builder().countryCode("BH").countryName("Bahrain").build());
    countries.add(Country.builder().countryCode("CA").countryName("Canada USMCA").build());
    return countries;
  }

}
