package gov.ita.terrafreights.country;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {
  String countryCode;
  String countryName;
  String elasticFreshenUrl;
}
