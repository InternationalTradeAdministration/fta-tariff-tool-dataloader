package gov.ita.terrafreights;

import gov.ita.terrafreights.country.CountryRepository;
import gov.ita.terrafreights.product.ProductTypeRepository;
import gov.ita.terrafreights.stagingbasket.StagingBasketRepository;
import gov.ita.terrafreights.tariff.RateRepository;
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
  private MockMvc mockMvc;

  @Test
  public void database_is_seeded_with_sample_tariff_data() {
    assertEquals(400, tariffRepository.count());
    assertEquals(4, countryRepository.count());
    assertEquals(2, productTypeRepository.count());
    assertEquals(11, stagingBasketRepository.count());
    assertEquals(416, rateRepository.count());
  }

  @Test
  public void tariff_api_provides_sample_tariff_data() throws Exception {
    mockMvc.perform(get("/api/tariffs?countryCode=KR&page=5&size=10&sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content", hasSize(10)));

    mockMvc.perform(get("/api/tariffs?countryCode=AU&page=5&size=10&sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content", hasSize(10)));

    mockMvc.perform(get("/api/tariffs?countryCode=BH&page=5&size=10&sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content", hasSize(10)));

    mockMvc.perform(get("/api/tariffs?countryCode=CA-USMCA&page=5&size=10&sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content", hasSize(10)));
  }
}
