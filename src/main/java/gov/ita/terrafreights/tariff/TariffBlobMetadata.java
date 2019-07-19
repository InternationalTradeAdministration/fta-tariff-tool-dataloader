package gov.ita.terrafreights.tariff;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TariffBlobMetadata {
  String url;
  String uploadedBy;
  LocalDateTime uploadedAt;
  boolean latestUpload;

  public TariffBlobMetadata(String url, String uploadedBy, LocalDateTime uploadedAt) {
    this.url = url;
    this.uploadedBy = uploadedBy;
    this.uploadedAt = uploadedAt;
  }
}
