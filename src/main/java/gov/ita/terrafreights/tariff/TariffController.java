package gov.ita.terrafreights.tariff;

import gov.ita.terrafreights.security.AuthenticationFacade;
import gov.ita.terrafreights.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class TariffController {

  private Storage storage;
  private AuthenticationFacade authenticationFacade;
  private RestTemplate restTemplate;

  public TariffController(Storage storage, AuthenticationFacade authenticationFacade, RestTemplate restTemplate) {
    this.storage = storage;
    this.authenticationFacade = authenticationFacade;
    this.restTemplate = restTemplate;
  }

  @GetMapping(value = "/api/tariff/log", produces = "application/json")
  public List<TariffBlobMetadata> getTariffUploadLogByCountry(@RequestParam("countryCode") String countryCode) {
    return storage.getBlobsMetadata(countryCode + "-");
  }

  @GetMapping(value = "/api/tariff/download", produces = "text/csv")
  public ResponseEntity<byte[]> downloadLatestTariffsByCountry(@RequestParam("countryCode") String countryCode,
                                                               HttpServletResponse response) {

    List<TariffBlobMetadata> blobsMetadata = storage.getBlobsMetadata(countryCode + "-");
    TariffBlobMetadata latest = blobsMetadata.stream().filter(TariffBlobMetadata::isLatestUpload).findFirst().get();
    response.setHeader("Content-Disposition", "attachment; filename=" + latest.getName());

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
    HttpEntity<String> entity = new HttpEntity<>(headers);

    return restTemplate.exchange(latest.getUrl(), HttpMethod.GET, entity, byte[].class);
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
