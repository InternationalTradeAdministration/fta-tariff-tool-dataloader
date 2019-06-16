package gov.ita.terrafreights;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("production")
@Slf4j
public class ProductionDatabaseSeeder implements DataSeeder {

  @Value("${terrafreights.properties.korea_csv}")
  private String koreaCsv;

  private RestTemplate restTemplate;

  public ProductionDatabaseSeeder(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public void seed() {
    log.info("Seeding production database with tariff data");
    ResponseEntity<String> koreaCsvEntity = restTemplate.getForEntity(koreaCsv, String.class);
    if (koreaCsvEntity.getStatusCode().equals(HttpStatus.OK)) {
      koreaCsvEntity.getBody();
    }
  }
}
