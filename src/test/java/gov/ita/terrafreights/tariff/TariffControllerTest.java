package gov.ita.terrafreights.tariff;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TariffControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void tariff_api_provides_paginated_tariff_data_for_KOREA() throws Exception {
    mockMvc.perform(get("/api/tariffs?countryCode={1}&stagingBasketId={2}&tariffLine={3}&page=0&size=10&sort=id,desc",
      "KR", 1, "01011000"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  public void tariff_api_provides_paginated_data_by_country_and_tariff_line() throws Exception {
    mockMvc.perform(get("/api/tariffs?countryCode={1}&stagingBasketId={2}&tariffLine={3}&page=0&size=10&sort=id,desc",
      "AU", -1, "01011000"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content", hasSize(2)));
  }

  @Test
  public void tariff_api_provides_paginated_data_by_country() throws Exception {
    mockMvc.perform(get("/api/tariffs?countryCode={1}&stagingBasketId={2}&page=0&size=10&sort=id,desc",
      "BH", -1))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content", hasSize(10)));
  }

  @Test
  public void tariff_api_provides_paginated_data_by_country_and_staging_basket() throws Exception {
    mockMvc.perform(get("/api/tariffs?countryCode={1}&stagingBasketId={2}&page=0&size=10&sort=id,desc",
      "CA", 1))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content", hasSize(10)));
  }

  @Test
  public void tariff_api_provides_all_data_by_country() throws Exception {
    mockMvc.perform(get("/api/tariffs/all?countryCode={1}", "CA"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("*", hasSize(100)));
  }

}
