package gov.ita.tarifftooldataloader.opendatacatalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.ita.tarifftooldataloader.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class OpenDataCatalogController {

  @Autowired
  private Storage storage;

  @Autowired
  private ObjectMapper objectMapper;

  @GetMapping(value = "/api/open-data-catalog", produces = "application/json")
  public OpenDataCatalog getOpenDataContent() throws IOException {
    String openDataCatalogJson = storage.getBlobAsString("open-data-catalog.json");
    OpenDataCatalog openDataCatalog = objectMapper.readValue(openDataCatalogJson, OpenDataCatalog.class);
    LocalDateTime lastModifiedAt = storage.getLastModifiedAt();
    openDataCatalog.getDataset().forEach(dataset -> dataset.setModified(lastModifiedAt));
    return openDataCatalog;
  }

}
