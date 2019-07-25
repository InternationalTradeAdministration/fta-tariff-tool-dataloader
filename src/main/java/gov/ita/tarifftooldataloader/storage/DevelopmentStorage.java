package gov.ita.tarifftooldataloader.storage;

import gov.ita.tarifftooldataloader.country.Country;
import gov.ita.tarifftooldataloader.tariff.TariffRatesMetadata;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Profile("development")
public class DevelopmentStorage implements Storage {

  @Override
  public void save(String fileName, String fileContent, String user) {
    System.out.println(user);
    System.out.println(fileName);
    System.out.println(fileContent);
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
    countries.add(Country.builder().visible(true).code("AU").name("Australia").build());
    countries.add(Country.builder().visible(true).code("BH").name("Bahrain").build());
    countries.add(Country.builder().visible(true).code("CA").name("Canada USMCA").build());
    return countries;
  }

  @Override
  public List<TariffRatesMetadata> getBlobsMetadata(String prefix) {
    if (prefix.equals("AU-")) {
      List<TariffRatesMetadata> blobsList = new ArrayList<>();
      String sampleBaseUrl = "https://tarifftoolaccount.blob.core.windows.net/tariff-rates/";
      blobsList.add(buildMeta(sampleBaseUrl, "AU-2019-07-18T18:30:13.863.csv"));
      blobsList.add(buildMeta(sampleBaseUrl, "AU-2019-07-17T18:30:13.863.csv"));
      blobsList.add(buildMeta(sampleBaseUrl, "AU-2019-07-16T18:30:13.863.csv"));

      blobsList.get(0).setLatestUpload(true);

      return blobsList;
    }

    return Collections.emptyList();
  }

  @Override
  public LocalDateTime getLastModifiedAt() {
    return LocalDateTime.now();
  }

  private TariffRatesMetadata buildMeta(String sampleBaseUrl, String fileName) {
    return new TariffRatesMetadata(
      fileName,
      sampleBaseUrl + fileName,
      "TestUser@trade.gov",
      LocalDateTime.now());
  }

}
