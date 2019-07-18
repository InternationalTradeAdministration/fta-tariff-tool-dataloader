package gov.ita.terrafreights.country;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Country {
  String countryCode;
  String countryName;
  String elasticFreshenUrl;

}
