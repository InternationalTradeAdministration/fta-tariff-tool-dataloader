package gov.ita.terrafreights;

import gov.ita.terrafreights.tariff.Tariff;
import gov.ita.terrafreights.tariff.TariffCsvTranslator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@Profile("production")
public class ProductionDataSeeder implements DataSeeder {
  private SeedDataProperties seedDataProperties;
  private RestTemplate restTemplate;
  private TariffCsvTranslator tariffCsvTranslator;
  private TariffPersister tariffPersister;

  public ProductionDataSeeder(SeedDataProperties seedDataProperties,
                              RestTemplate restTemplate,
                              TariffCsvTranslator tariffCsvTranslator,
                              TariffPersister tariffPersister) {
    this.seedDataProperties = seedDataProperties;
    this.restTemplate = restTemplate;
    this.tariffCsvTranslator = tariffCsvTranslator;
    this.tariffPersister = tariffPersister;
  }

  @Override
  public void seed() {
    log.info("Seeding production database with tariff data");

    for (Csv csv : seedDataProperties.getCsvs()) {
      ResponseEntity<String> csvEntity = restTemplate.getForEntity(csv.url, String.class);
      if (csvEntity.getStatusCode().equals(HttpStatus.OK)) {
        log.info("Loading tariff data file: {}", csv.url);
        List<Tariff> tariffs = tariffCsvTranslator.translate(csv.countryCode, csvEntity.getBody());
        tariffPersister.persist(tariffs);
      } else {
        log.error("Error: Couldn't load tariff data file: {}", csv.url);
      }
    }
  }
}
