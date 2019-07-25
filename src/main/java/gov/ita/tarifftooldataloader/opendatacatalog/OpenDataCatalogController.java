package gov.ita.tarifftooldataloader.opendatacatalog;

import gov.ita.tarifftooldataloader.storage.Storage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class OpenDataCatalogController {

  private Storage storage;

  public OpenDataCatalogController(Storage storage) {
    this.storage = storage;
  }

  @GetMapping(value = "/api/open-data-catalog", produces = "application/json")
  public OpenDataCatalog getOpenDataContent() {
    LocalDateTime lastModifiedAt = storage.getLastModifiedAt();
    return new OpenDataCatalog(lastModifiedAt);
  }

}
