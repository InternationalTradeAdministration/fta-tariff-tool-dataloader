package gov.ita.terrafreights;

import gov.ita.terrafreights.storage.Storage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.event.ContextRefreshedEvent;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TerraFreightsInitializerTest {

  @Mock
  private Storage storage;

  @Mock
  private ContextRefreshedEvent contextRefreshedEvent;

  @Test
  public void initializes_storage_container_on_startup() {
    when(storage.containerExists()).thenReturn(false);
    TerraFreightsInitializer terraFreightsInitializer = new TerraFreightsInitializer(storage);
    terraFreightsInitializer.onApplicationEvent(contextRefreshedEvent);
    verify(storage).createContainer();
  }

  @Test
  public void skips_storage_initialization_when_container_exists() {
    when(storage.containerExists()).thenReturn(true);
    TerraFreightsInitializer terraFreightsInitializer = new TerraFreightsInitializer(storage);
    terraFreightsInitializer.onApplicationEvent(contextRefreshedEvent);
    verify(storage, times(0)).createContainer();
  }
}
