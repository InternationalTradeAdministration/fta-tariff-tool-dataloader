package gov.ita.terrafreights;

import gov.ita.terrafreights.tariff.InvalidCsvFileException;
import gov.ita.terrafreights.tariff.TariffRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.event.ContextRefreshedEvent;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TerraFreightsInitializerTest {

  @Mock
  private DataSeeder dataSeeder;

  @Mock
  private TariffRepository tariffRepository;

  @Mock
  private ContextRefreshedEvent contextRefreshedEvent;

  @Test
  public void database_is_seeded_when_no_tariff_data_exists() throws InvalidCsvFileException {
    TerraFreightsInitializer terraFreightsInitializer = new TerraFreightsInitializer(dataSeeder, tariffRepository);
    terraFreightsInitializer.onApplicationEvent(contextRefreshedEvent);
    verify(dataSeeder, times(1)).seed();
  }

  @Test
  public void database_is_NOT_seeded_when_tariff_data_exists() throws InvalidCsvFileException {
    when(tariffRepository.count()).thenReturn(100L);
    TerraFreightsInitializer terraFreightsInitializer = new TerraFreightsInitializer(dataSeeder, tariffRepository);
    terraFreightsInitializer.onApplicationEvent(contextRefreshedEvent);
    verify(dataSeeder, times(0)).seed();
  }

}
