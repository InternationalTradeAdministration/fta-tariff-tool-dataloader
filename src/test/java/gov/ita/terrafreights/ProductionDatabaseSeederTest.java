package gov.ita.terrafreights;

import gov.ita.terrafreights.tariff.Tariff;
import gov.ita.terrafreights.tariff.TariffCsvTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductionDatabaseSeederTest {

  @Mock
  private ProductionSeedDataConfiguration productionSeedDataConfiguration;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private TariffCsvTranslator tariffCsvTranslator;

  @Mock
  private TariffPersister tariffPersister;

  private List<Tariff> mockUsTariffs;
  private List<Tariff> mockGrTariffs;

  @Before
  public void set_up() {
    ProductionDatabaseSeeder productionDatabaseSeeder = new ProductionDatabaseSeeder(
      productionSeedDataConfiguration,
      restTemplate,
      tariffCsvTranslator,
      tariffPersister);

    when(productionSeedDataConfiguration.getCsvs())
      .thenReturn(Arrays.asList(
        new Csv("US", "http://us.csv"),
        new Csv("GR", "http://gr.csv")
      ));

    ResponseEntity<String> fake_us_csv_data = ResponseEntity.of(Optional.of("fake us csv data"));
    when(restTemplate.getForEntity("http://us.csv", String.class))
      .thenReturn(fake_us_csv_data);
    ResponseEntity<String> fake_gr_csv_data = ResponseEntity.of(Optional.of("fake gr csv data"));
    when(restTemplate.getForEntity("http://gr.csv", String.class))
      .thenReturn(fake_gr_csv_data);

    mockUsTariffs = Collections.singletonList(Tariff.builder().country("US").build());
    mockGrTariffs = Collections.singletonList(Tariff.builder().country("GR").build());

    when(tariffCsvTranslator.translate("US", "fake us csv data"))
      .thenReturn(mockUsTariffs);
    when(tariffCsvTranslator.translate("GR", "fake gr csv data"))
      .thenReturn(mockGrTariffs);

    productionDatabaseSeeder.seed();
  }

  @Test
  public void retrieves_csv_data_from_urls() {
    verify(restTemplate).getForEntity("http://us.csv", String.class);
    verify(restTemplate).getForEntity("http://gr.csv", String.class);
  }

  @Test
  public void translates_csv_data_into_tariffs() {
    verify(tariffCsvTranslator, times(1)).translate("US", "fake us csv data");
    verify(tariffCsvTranslator, times(1)).translate("GR", "fake gr csv data");
  }

  @Test
  public void persists_tariffs() {
    verify(tariffPersister, times(1)).persist(mockUsTariffs);
    verify(tariffPersister, times(1)).persist(mockGrTariffs);
  }

}
