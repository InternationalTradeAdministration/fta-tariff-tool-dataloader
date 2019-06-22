package gov.ita.terrafreights;

import gov.ita.terrafreights.tariff.Tariff;
import gov.ita.terrafreights.tariff.TariffCsvTranslator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Slf4j
@Component
@Profile("development")
public class DevelopmentDataSeeder implements DataSeeder {

  private SeedDataProperties seedDataProperties;
  private TariffCsvTranslator tariffCsvTranslator;
  private TariffPersister tariffPersister;

  public DevelopmentDataSeeder(SeedDataProperties seedDataProperties,
                               TariffCsvTranslator tariffCsvTranslator,
                               TariffPersister tariffPersister) {
    this.seedDataProperties = seedDataProperties;
    this.tariffCsvTranslator = tariffCsvTranslator;
    this.tariffPersister = tariffPersister;
  }

  @Override
  public void seed() {
    log.info("Seeding development database with sample tariff data");

    for (Csv csv : seedDataProperties.getCsvs()) {
      log.info("Loading sample tariff data file: {}", csv.getUrl());
      String path = "/fixtures/" + csv.getUrl();
      InputStream in = DevelopmentDataSeeder.class.getResourceAsStream(path);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String fileString = null;
      try {
        fileString = IOUtils.toString(reader);
      } catch (IOException e) {
        e.printStackTrace();
      }

      List<Tariff> tariffs = tariffCsvTranslator.translate(csv.getCountryCode(), fileString);
      tariffPersister.persist(tariffs);
    }
  }
}
