package gov.ita.terrafreights.country;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("development")
public class DevelopmentCountryRespository implements CountryRepository {
  @Override
  public List<Country> allCountries() {
    List<Country> countries = new ArrayList<>();
    countries.add(Country.builder().countryCode("AU").countryName("Australia").elasticFreshenUrl("http://something").build());
    countries.add(Country.builder().countryCode("BH").countryName("Bahrain").build());
    countries.add(Country.builder().countryCode("CA").countryName("Canada USMCA").build());
    return countries;  }
}
