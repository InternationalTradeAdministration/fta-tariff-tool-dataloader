package gov.ita.tarifftooldataloader.storage;

import gov.ita.tarifftooldataloader.tariff.TariffRatesMetadata;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
  public String getBlobAsString(String blobName) {
    if (blobName.equals("countries.json"))
      return getResourceAsString("/fixtures/countries.json");
    else
      return getResourceAsString("/fixtures/open-data-catalog.json");
  }

  @Override
  public List<TariffRatesMetadata> getBlobsMetadata(String prefix) {
    if (prefix.equals("AU-")) {
      List<TariffRatesMetadata> blobsList = new ArrayList<>();
      String sampleBaseUrl = "https://storageaccount.blob.core.windows.net/tariff-rates/";
      blobsList.add(buildMeta(sampleBaseUrl, "AU.csv"));
      blobsList.add(buildMeta(sampleBaseUrl, "AU-2019-07-17T18:30:13.863.csv"));
      blobsList.add(buildMeta(sampleBaseUrl, "AU-2019-07-16T18:30:13.863.csv"));

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
      LocalDateTime.now(),
      1L);
  }

  private String getResourceAsString(String resource) {
    InputStream inputStream = DevelopmentStorage.class.getResourceAsStream(resource);
    try {
      return IOUtils.toString(new InputStreamReader(inputStream));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
