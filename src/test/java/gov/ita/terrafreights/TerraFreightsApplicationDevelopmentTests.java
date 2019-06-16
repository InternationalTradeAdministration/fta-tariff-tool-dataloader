package gov.ita.terrafreights;

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
public class TerraFreightsApplicationDevelopmentTests {

  @Autowired
  private TariffRepository tariffRepository;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void database_is_seeded_with_sample_tariff_data() {
    assertEquals(100, tariffRepository.findAll().size());
  }

  @Test
  public void tariff_api_provides_sample_tariff_data() throws Exception {
    mockMvc.perform(get("/api/tariffs?country=KR&page=5&size=10&sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content", hasSize(10)));
  }
}
