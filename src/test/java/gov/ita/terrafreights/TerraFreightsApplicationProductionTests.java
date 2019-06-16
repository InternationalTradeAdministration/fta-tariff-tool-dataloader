package gov.ita.terrafreights;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("production")
public class TerraFreightsApplicationProductionTests {

  @Autowired
  private ProductionDatabaseSeeder productionDatabaseSeeder;

  @Test
  @Ignore
  public void database_is_seeded_with_sample_tariff_data() {
    productionDatabaseSeeder.seed();
  }

}
