package gov.ita.tarifftooldataloader.tariff;

import gov.ita.tarifftooldataloader.security.AuthenticationFacade;
import gov.ita.tarifftooldataloader.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class TariffController {

  private Storage storage;
  private AuthenticationFacade authenticationFacade;
  private RestTemplate restTemplate;
  private TariffCsvTranslator tariffCsvTranslator;

  public TariffController(Storage storage,
                          AuthenticationFacade authenticationFacade,
                          RestTemplate restTemplate,
                          TariffCsvTranslator tariffCsvTranslator) {
    this.storage = storage;
    this.authenticationFacade = authenticationFacade;
    this.restTemplate = restTemplate;
    this.tariffCsvTranslator = tariffCsvTranslator;
  }

  @GetMapping(value = "/api/tariff/log", produces = "application/json")
  public List<TariffRatesMetadata> getTariffUploadLogByCountry(@RequestParam("countryCode") String countryCode) {
    return storage.getBlobsMetadata(countryCode + "-");
  }

  @GetMapping(value = "/api/tariff/download/csv", produces = "text/csv")
  public ResponseEntity<byte[]> downloadLatestTariffsCsvByCountry(@RequestParam("countryCode") String countryCode,
                                                                  HttpServletResponse response) {
    List<TariffRatesMetadata> blobsMetadata = storage.getBlobsMetadata(countryCode + "-");
    TariffRatesMetadata latest = blobsMetadata.stream().filter(TariffRatesMetadata::isLatestUpload).findFirst().get();
    response.setHeader("Content-Disposition", "attachment; filename=" + countryCode + ".csv");

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
    HttpEntity<String> entity = new HttpEntity<>(headers);

    return restTemplate.exchange(latest.getUrl(), HttpMethod.GET, entity, byte[].class);
  }

  @GetMapping(value = "/api/tariff/download/json", produces = "application/json")
  public List<Tariff> downloadLatestTariffsJsonByCountry(@RequestParam("countryCode") String countryCode,
                                                         HttpServletResponse response) throws InvalidCsvFileException {
    List<TariffRatesMetadata> blobsMetadata = storage.getBlobsMetadata(countryCode + "-");
    TariffRatesMetadata latest = blobsMetadata.stream().filter(TariffRatesMetadata::isLatestUpload).findFirst().get();
    response.setHeader("Content-Disposition", "attachment; filename=" + countryCode + ".json");

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<byte[]> exchange = restTemplate.exchange(latest.getUrl(), HttpMethod.GET, entity, byte[].class);

    return tariffCsvTranslator.translate(countryCode, new StringReader(new String(exchange.getBody())));
  }

  @PreAuthorize("hasRole('ROLE_EDSP')")
  @PutMapping("/api/tariffs/save")
  public String saveTariffs(@RequestParam("countryCode") String countryCode,
                            @RequestBody TariffRatesUpload tariffRatesUpload) {
    try {
      tariffCsvTranslator.isValid(countryCode, new StringReader(tariffRatesUpload.csv));
    } catch (InvalidCsvFileException e) {
      return e.getMessage();
    }

    String timestampedFileName = String.format("%s-%s.csv", countryCode, LocalDateTime.now().toString());
    log.info("Creating file {}", timestampedFileName);
    storage.save(timestampedFileName, tariffRatesUpload.csv, authenticationFacade.getUserName());
    return "success";
  }
}
