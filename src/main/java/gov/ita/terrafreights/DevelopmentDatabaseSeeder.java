package gov.ita.terrafreights;

import gov.ita.terrafreights.country.Country;
import gov.ita.terrafreights.country.CountryRepository;
import gov.ita.terrafreights.tariff.Tariff;
import gov.ita.terrafreights.tariff.TariffCsvTranslator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Profile("development")
@Slf4j
public class DevelopmentDatabaseSeeder implements DataSeeder {

  private TariffCsvTranslator tariffCsvTranslator;
  private TariffPersister tariffPersister;

  private static final Map<String, String> countryCsvFilesMap;

  static {
    Map<String, String> map = new HashMap<>();
    map.put("KR", "korea.csv");
    map.put("AU", "australia.csv");
    countryCsvFilesMap = Collections.unmodifiableMap(map);
  }

  public DevelopmentDatabaseSeeder(TariffCsvTranslator tariffCsvTranslator,
                                   TariffPersister tariffPersister) {
    this.tariffCsvTranslator = tariffCsvTranslator;
    this.tariffPersister = tariffPersister;
  }

  @Override
  public void seed() {
    log.info("Seeding development database with sample tariff data");

    for (String countryCode : countryCsvFilesMap.keySet()) {
      String countryCsv = countryCsvFilesMap.get(countryCode);

      log.info("Loading sample tariff data file: {}", countryCsv);

      String path = "fixtures/" + countryCsv;
      File file;
      String fileString = null;
      try {
        file = new ClassPathResource(path).getFile();
        fileString = FileUtils.readFileToString(file, "UTF-8");
      } catch (IOException e) {
        e.printStackTrace();
      }

      List<Tariff> tariffs = tariffCsvTranslator.translate(countryCode, fileString);
      tariffPersister.persist(tariffs);
    }
  }
}
