package gov.ita.terrafreights;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "seed")
public class ProductionSeedDataConfiguration {
  private List<Csv> csvs = new ArrayList<>();

  public List<Csv> getCsvs() {
    return this.csvs;
  }
}
