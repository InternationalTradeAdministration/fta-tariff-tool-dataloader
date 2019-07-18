package gov.ita.terrafreights;

import gov.ita.terrafreights.country.Country;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("development")
public class DevelopmentStorage implements Storage {
  @Override
  public void save(String fileName, String fileContent, String contentType, String user) {

  }

  @Override
  public boolean containerExists() {
    return false;
  }

  @Override
  public void createContainer() {

  }

  @Override
  public List<Country> getCountries() {
    List<Country> countries = new ArrayList<>();
    countries.add(Country.builder().countryCode("AU").countryName("Australia").build());
    countries.add(Country.builder().countryCode("BH").countryName("Bahrain").build());
    countries.add(Country.builder().countryCode("CA").countryName("Canada USMCA").build());
    return countries;
  }
}
