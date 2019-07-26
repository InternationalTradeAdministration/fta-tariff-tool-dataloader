package gov.ita.tarifftooldataloader;

import gov.ita.tarifftooldataloader.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@Component
public class TariffToolDataloaderInitializer implements ApplicationListener<ContextRefreshedEvent> {

  private Storage storage;

  public TariffToolDataloaderInitializer(Storage storage) {
    this.storage = storage;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (!storage.containerExists()) {
      log.info("Initializing storage");
      storage.createContainer();
      storage.save("countries.json", getResourceAsString("/fixtures/countries.json"), null);
      storage.save("open-data-catalog.json", getResourceAsString("/fixtures/open-data-catalog.json"), null);
    }
  }

  private String getResourceAsString(String resource) {
    InputStream inputStream = TariffToolDataloaderInitializer.class.getResourceAsStream(resource);
    try {
      return IOUtils.toString(new InputStreamReader(inputStream));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
