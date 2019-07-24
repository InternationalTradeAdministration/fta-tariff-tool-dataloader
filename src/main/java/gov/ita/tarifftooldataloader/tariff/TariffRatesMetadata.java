package gov.ita.tarifftooldataloader.tariff;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TariffRatesMetadata {
  String name;
  String url;
  String uploadedBy;
  LocalDateTime uploadedAt;
  boolean latestUpload;

  public TariffRatesMetadata(String name, String url, String uploadedBy, LocalDateTime uploadedAt) {
    this.name = name;
    this.url = url;
    this.uploadedBy = uploadedBy;
    this.uploadedAt = uploadedAt;
  }
}
