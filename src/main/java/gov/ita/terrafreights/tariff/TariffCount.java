package gov.ita.terrafreights.tariff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffCount {
  String countryCode;
  Long count;
}
