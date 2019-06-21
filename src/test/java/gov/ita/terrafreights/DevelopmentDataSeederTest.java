package gov.ita.terrafreights;

import gov.ita.terrafreights.country.CountryRepository;
import gov.ita.terrafreights.product.ProductTypeRepository;
import gov.ita.terrafreights.stagingbasket.StagingBasketRepository;
import gov.ita.terrafreights.tariff.RateRepository;
import gov.ita.terrafreights.tariff.Tariff;
import gov.ita.terrafreights.tariff.TariffRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("development")
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

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void database_is_seeded_with_sample_tariff_data() {
    assertEquals(400, tariffRepository.count());
    assertEquals(4, countryRepository.count());
    assertEquals(2, productTypeRepository.count());
    assertEquals(11, stagingBasketRepository.count());
    assertEquals(416, rateRepository.count());
    assertEquals(4, linkRepository.count());

    int linkCount = 0;
    for(Tariff t: tariffRepository.findAll()) {
      linkCount += t.getLinks().size();
    }
    assertEquals(6, linkCount);
  }

  @Test
  public void tariff_api_provides_sample_tariff_data_for_KOREA() throws Exception {
    assertCountry("KR");
  }

  @Test
  public void tariff_api_provides_sample_tariff_data_for_AUSTRALIA() throws Exception {
    assertCountry("AU");
  }

  @Test
  public void tariff_api_provides_sample_tariff_data_for_BAHRAIN() throws Exception {
    assertCountry("BH");
  }

  @Test
  public void tariff_api_provides_sample_tariff_data_for_CANADA_USMCA() throws Exception {
    assertCountry("CA-USMCA");
  }

  private void assertCountry(String countryCode) throws Exception {
    mockMvc.perform(get("/api/tariffs?countryCode={countryCode}&page=5&size=10&sort=id,desc",countryCode))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content", hasSize(10)));
  }
}
