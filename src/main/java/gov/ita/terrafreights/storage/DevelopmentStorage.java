package gov.ita.terrafreights.storage;

import gov.ita.terrafreights.country.Country;
import gov.ita.terrafreights.tariff.TariffBlobMetadata;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
      blobsList.add(new TariffBlobMetadata(sampleBaseUrl + "AU-2019-07-18T18:30:13.863.csv", "TestUser@trade.gov", LocalDateTime.now()));
      blobsList.add(new TariffBlobMetadata(sampleBaseUrl + "AU-2019-07-17T18:30:13.863.csv", "TestUser@trade.gov", LocalDateTime.now()));
      blobsList.add(new TariffBlobMetadata(sampleBaseUrl + "AU-2019-07-16T18:30:13.863.csv", "TestUser@trade.gov", LocalDateTime.now()));

      blobsList.get(0).setLatestUpload(true);

      return blobsList;
    }

    return Collections.emptyList();
  }

  @Override
  public ResponseEntity<byte[]> getLatestBlobByCountry(String prefix) {
    return ResponseEntity.of(Optional.of("this should be a csv file".getBytes()));
  }

}
