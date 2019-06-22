package gov.ita.terrafreights;

import gov.ita.terrafreights.tariff.Tariff;
import gov.ita.terrafreights.tariff.TariffCsvTranslator;
import gov.ita.terrafreights.tariff.TariffPersister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.InputStreamReader;
import java.util.Arrays;
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

    RequestCallback requestCallback = request -> request.getHeaders()
      .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));

    for (Csv csv : seedDataProperties.getCsvs()) {
      log.info("Loading tariff data file: {}", csv.url);

      ResponseExtractor<Void> responseExtractor = response -> {
        List<Tariff> tariffs = tariffCsvTranslator.translate(
          csv.getCountryCode(),
          new InputStreamReader(response.getBody())
        );
        tariffPersister.persist(tariffs);
        return null;
      };

      restTemplate.execute(csv.getUrl(), HttpMethod.GET, requestCallback, responseExtractor);
    }
  }
}
