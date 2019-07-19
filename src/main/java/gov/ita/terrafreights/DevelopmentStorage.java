package gov.ita.terrafreights;

import gov.ita.terrafreights.country.Country;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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

  @Override
  public Map<String, LocalDateTime> getBlobsWithPrefix(String prefix) {
    Map<String, LocalDateTime> blobsMap = new HashMap<>();
    blobsMap.put("https://tarifftoolaccount.blob.core.windows.net/tariff-rates/AU-2019-07-18T18:30:13.863.csv", LocalDateTime.now());
    blobsMap.put("https://tarifftoolaccount.blob.core.windows.net/tariff-rates/AU-2019-07-17T18:30:13.863.csv", LocalDateTime.now());
    blobsMap.put("https://tarifftoolaccount.blob.core.windows.net/tariff-rates/AU-2019-07-16T18:30:13.863.csv", LocalDateTime.now());
    return blobsMap;
  }

  @Override
  public String buildUrlForBlob(String blobName) {
    return "https://tarifftoolaccount.blob.core.windows.net/tariff-rates/" + blobName;
  }
}
