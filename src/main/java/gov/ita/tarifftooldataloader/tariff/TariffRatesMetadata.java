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
  Long length;
}
