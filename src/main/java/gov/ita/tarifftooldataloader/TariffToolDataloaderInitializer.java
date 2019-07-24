package gov.ita.tarifftooldataloader;

import gov.ita.tarifftooldataloader.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

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
    }
  }
}
