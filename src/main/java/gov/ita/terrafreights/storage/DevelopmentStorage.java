package gov.ita.terrafreights.storage;

import gov.ita.terrafreights.country.Country;
import gov.ita.terrafreights.tariff.TariffBlobMetadata;
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
  public List<TariffBlobMetadata> getBlobsMetadata(String prefix) {
    if (prefix.equals("AU-")) {
      List<TariffBlobMetadata> blobsList = new ArrayList<>();
      String sampleBaseUrl = "https://tarifftoolaccount.blob.core.windows.net/tariff-rates/";
      blobsList.add(buildMeta(sampleBaseUrl, "AU-2019-07-18T18:30:13.863.csv"));
      blobsList.add(buildMeta(sampleBaseUrl, "AU-2019-07-17T18:30:13.863.csv"));
      blobsList.add(buildMeta(sampleBaseUrl, "AU-2019-07-16T18:30:13.863.csv"));

      blobsList.get(0).setLatestUpload(true);

      return blobsList;
    }

    return Collections.emptyList();
  }

  private TariffBlobMetadata buildMeta(String sampleBaseUrl, String fileName) {
    return new TariffBlobMetadata(
      fileName,
      sampleBaseUrl + fileName,
      "TestUser@trade.gov",
      LocalDateTime.now());
  }

}
