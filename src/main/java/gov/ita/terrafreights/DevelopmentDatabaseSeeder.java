package gov.ita.terrafreights;

import gov.ita.terrafreights.tariff.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

  public DevelopmentDatabaseSeeder(TariffCsvTranslator tariffCsvTranslator,
                                   TariffRepository tariffRepository,
                                   HS6Repository hs6Repository,
                                   ProductTypeRepository productTypeRepository,
                                   StagingBasketRepository stagingBasketRepository,
                                   RateRepository rateRepository) {
    this.tariffCsvTranslator = tariffCsvTranslator;
    this.tariffRepository = tariffRepository;
    this.hs6Repository = hs6Repository;
    this.productTypeRepository = productTypeRepository;
    this.stagingBasketRepository = stagingBasketRepository;
    this.rateRepository = rateRepository;
  }

  @Override
  public void seed() {
    log.info("Seeding development database with sample tariff data");

    String path = "fixtures/" + "korea.csv";
    File file = null;
    try {
      file = new ClassPathResource(path).getFile();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String fileString = null;
    try {
      fileString = FileUtils.readFileToString(file, "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }

    List<Tariff> tariffs = tariffCsvTranslator.translate("KR", fileString);

    Set<HS6> hs6s = new HashSet<>();
    Set<ProductType> productTypes = new HashSet<>();
    Set<StagingBasket> stagingBaskets = new HashSet<>();
    List<Rate> rates = new ArrayList<>();

    for(Tariff t: tariffs) {
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
