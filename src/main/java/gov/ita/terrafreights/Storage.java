package gov.ita.terrafreights;

import gov.ita.terrafreights.country.Country;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface Storage {
  void save(String fileName, String fileContent, String contentType, String user);
  boolean containerExists();
  void createContainer();
  List<Country> getCountries();
  Map<String, LocalDateTime> getBlobsWithPrefix(String prefix);
  String buildUrlForBlob(String blobName);
}
