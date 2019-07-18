package gov.ita.terrafreights.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Profile("production")
public class ProductionCountryRespository implements CountryRepository {

  @Value("${system-metadata.countries-url}")
  private String countriesUrl;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public List<Country> allCountries() {
    return restTemplate.getForObject(countriesUrl, CountryList.class).getCountries();
  }
}
