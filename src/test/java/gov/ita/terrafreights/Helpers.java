package gov.ita.terrafreights;

import gov.ita.terrafreights.tariff.TariffRepository;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class Helpers {
  public static String getFileAsString(String fileName) {
    String path = "fixtures/" + fileName;
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

    return fileString;
  }

  @RunWith(SpringRunner.class)
  @SpringBootTest
  @AutoConfigureMockMvc
  @ActiveProfiles("development")
  public static class TerraFreightsApplicationProductionTests {

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
      mockMvc.perform(get("/api/tariffs"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(100)));
    }
  }
}
