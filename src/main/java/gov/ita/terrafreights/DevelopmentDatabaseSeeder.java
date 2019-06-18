package gov.ita.terrafreights;

import gov.ita.terrafreights.country.Country;
import gov.ita.terrafreights.country.CountryRepository;
import gov.ita.terrafreights.tariff.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
@Profile("development")
@Slf4j
public class DevelopmentDatabaseSeeder implements DataSeeder {

  private TariffCsvTranslator tariffCsvTranslator;
  private TariffRepository tariffRepository;
  private HS6Repository hs6Repository;
  private ProductTypeRepository productTypeRepository;
  private StagingBasketRepository stagingBasketRepository;
  private RateRepository rateRepository;
  private CountryRepository countryRepository;

  private static final Map<String, String> countryCsvFilesMap;

  static {
    Map<String, String> map = new HashMap<>();
    map.put("KR", "korea.csv");
    map.put("AU", "australia.csv");
    countryCsvFilesMap = Collections.unmodifiableMap(map);
  }

  public DevelopmentDatabaseSeeder(TariffCsvTranslator tariffCsvTranslator,
                                   TariffRepository tariffRepository,
                                   HS6Repository hs6Repository,
                                   ProductTypeRepository productTypeRepository,
                                   StagingBasketRepository stagingBasketRepository,
                                   RateRepository rateRepository,
                                   CountryRepository countryRepository) {
    this.tariffCsvTranslator = tariffCsvTranslator;
    this.tariffRepository = tariffRepository;
    this.hs6Repository = hs6Repository;
    this.productTypeRepository = productTypeRepository;
    this.stagingBasketRepository = stagingBasketRepository;
    this.rateRepository = rateRepository;
    this.countryRepository = countryRepository;
  }

  @Override
  public void seed() {
    log.info("Seeding development database with sample tariff data");

    for (String countryCode : countryCsvFilesMap.keySet()) {
      String countryCsv = countryCsvFilesMap.get(countryCode);

      log.info("Loading sample {} tariff data", countryCsv);

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
      saveTariffs(tariffs);
    }

    log.info("Loading countries table");
    countryRepository.save(new Country("KR", "South Korea"));
    countryRepository.save(new Country("AU", "Australia"));
  }

  private void saveTariffs(List<Tariff> tariffs) {
    Set<HS6> hs6s = new HashSet<>();
    Set<ProductType> productTypes = new HashSet<>();
    Set<StagingBasket> stagingBaskets = new HashSet<>();
    List<Rate> rates = new ArrayList<>();

    for (Tariff t : tariffs) {
      hs6s.add(t.getHs6());
      productTypes.add(t.getProductType());
      stagingBaskets.add(t.getStagingBasket());
      rates.addAll(t.getRates());
    }

    hs6Repository.saveAll(hs6s);
    productTypeRepository.saveAll(productTypes);
    stagingBasketRepository.saveAll(stagingBaskets);
    rateRepository.saveAll(rates);
    tariffRepository.saveAll(tariffs);
  }
}
