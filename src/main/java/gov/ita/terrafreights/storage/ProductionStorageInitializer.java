package gov.ita.terrafreights.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile({"production", "staging"})
public class ProductionStorageInitializer implements StorageInitializer {

  private Storage storage;

  public ProductionStorageInitializer(Storage storage) {
    this.storage = storage;
  }

  @Override
  public void init() {
    if (!storage.containerExists()) {
      log.info("Initializing blob container");
      storage.createContainer();
    }
  }
}
