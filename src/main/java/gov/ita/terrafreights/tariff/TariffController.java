package gov.ita.terrafreights.tariff;

import gov.ita.terrafreights.security.AuthenticationFacade;
import gov.ita.terrafreights.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
public class TariffController {

  private Storage storage;
  private AuthenticationFacade authenticationFacade;

  public TariffController(Storage storage, AuthenticationFacade authenticationFacade) {
    this.storage = storage;
    this.authenticationFacade = authenticationFacade;
  }

  @GetMapping(value = "/api/tariffs", produces = "application/json")
  public List<TariffBlobMetadata> getTariffUploadLogByCountry(@RequestParam("countryCode") String countryCode) {
    return storage.getBlobsMetadata(countryCode + "-");
  }

  @GetMapping(value = "/api/tariff/download", produces = "text/csv")
  public ResponseEntity<byte[]> downloadLatestTariffsByCountry(@RequestParam("countryCode") String countryCode) {
    return storage.getLatestBlobByCountry(countryCode + "-");
  }

  @PreAuthorize("hasRole('ROLE_EDSP')")
  @PutMapping("/api/tariffs/save")
  public String saveTariffs(@RequestParam("countryCode") String countryCode,
                            @RequestBody TariffUpload tariffUpload) {
    String timestampedFileName = String.format("%s-%s.csv", countryCode, LocalDateTime.now().toString());
    log.info("Creating file {}", timestampedFileName);
    storage.save(timestampedFileName, tariffUpload.csv, "text/csv", authenticationFacade.getUserName());
    return "success";
  }
}
