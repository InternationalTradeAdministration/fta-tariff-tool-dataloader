package gov.ita.terrafreights.storage;

import gov.ita.terrafreights.country.Country;
import gov.ita.terrafreights.tariff.TariffBlobMetadata;

import java.util.List;

public interface Storage {
  void save(String fileName, String fileContent, String contentType, String user);

  boolean containerExists();

  void createContainer();

  List<Country> getCountries();

  List<TariffBlobMetadata> getBlobsMetadata(String prefix);

  String getBlobsListUrl();
}
