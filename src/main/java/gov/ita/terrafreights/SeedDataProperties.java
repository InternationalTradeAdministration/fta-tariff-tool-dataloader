package gov.ita.terrafreights;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(value = "seed")
public class SeedDataProperties {
  private String csvs;

  public List<Csv> getCsvs() {
    List<Csv> csvList = new ArrayList<>();
    String[] csvElements = csvs.split(";");

    for (String csvElement : csvElements) {
      String[] csvProperties = csvElement.split(",");
      String countryCode = csvProperties[0].trim();
      String url = csvProperties[1].trim();
      csvList.add(new Csv(countryCode, url));
    }
    return csvList;
  }
}
