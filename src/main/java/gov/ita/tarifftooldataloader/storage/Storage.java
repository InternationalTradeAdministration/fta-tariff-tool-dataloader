package gov.ita.tarifftooldataloader.storage;

import gov.ita.tarifftooldataloader.tariff.TariffRatesMetadata;

import java.time.LocalDateTime;
import java.util.List;

public interface Storage {
  void save(String fileName, String fileContent, String user);

  boolean containerExists();

  void createContainer();

  String getBlobAsString(String blobName);

  List<TariffRatesMetadata> getBlobsMetadata(String prefix);

  LocalDateTime getLastModifiedAt();
}
