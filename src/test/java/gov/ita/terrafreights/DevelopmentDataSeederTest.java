package gov.ita.terrafreights;

import gov.ita.terrafreights.tariff.Tariff;
import gov.ita.terrafreights.tariff.TariffRepository;
import gov.ita.terrafreights.tariff.country.CountryRepository;
import gov.ita.terrafreights.tariff.link.LinkRepository;
import gov.ita.terrafreights.tariff.product.ProductTypeRepository;
import gov.ita.terrafreights.tariff.rate.RateRepository;
import gov.ita.terrafreights.tariff.stagingbasket.StagingBasketRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DevelopmentDataSeederTest {

  @Autowired
  private TariffRepository tariffRepository;

  @Autowired
  private ProductTypeRepository productTypeRepository;

  @Autowired
  private StagingBasketRepository stagingBasketRepository;

  @Autowired
  private CountryRepository countryRepository;

  @Autowired
  private RateRepository rateRepository;

  @Autowired
  private LinkRepository linkRepository;

  @Test
  public void database_is_seeded_with_sample_tariff_data() {
    assertEquals(400, tariffRepository.count());
    assertEquals(18, countryRepository.count());
    assertEquals(2, productTypeRepository.count());
    assertEquals(11, stagingBasketRepository.count());
    assertEquals(416, rateRepository.count());
    assertEquals(6, linkRepository.count());

    int linkCount = 0;
    for (Tariff t : tariffRepository.findAll()) {
      linkCount += t.getLinks().size();
    }
    assertEquals(6, linkCount);
  }

}
