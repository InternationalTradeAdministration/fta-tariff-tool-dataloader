package gov.ita.tarifftooldataloader.tariff;

import gov.ita.tarifftooldataloader.security.AuthenticationFacade;
import gov.ita.tarifftooldataloader.storage.Storage;
import gov.ita.tarifftooldataloader.tariffdocs.TariffDoc;
import gov.ita.tarifftooldataloader.tariffdocs.TariffDocGateway;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
public class TariffController {

  private Storage storage;
  private AuthenticationFacade authenticationFacade;
  private RestTemplate restTemplate;
  private TariffCsvTranslator tariffCsvTranslator;
  private TariffDocGateway tariffDocGateway;

  public TariffController(Storage storage,
                          AuthenticationFacade authenticationFacade,
                          RestTemplate restTemplate,
                          TariffCsvTranslator tariffCsvTranslator,
                          TariffDocGateway tariffDocGateway) {
    this.storage = storage;
    this.authenticationFacade = authenticationFacade;
    this.restTemplate = restTemplate;
    this.tariffCsvTranslator = tariffCsvTranslator;
    this.tariffDocGateway = tariffDocGateway;
  }

  @GetMapping(value = "/api/tariff/log", produces = "application/json")
  public List<TariffRatesMetadata> getTariffUploadLogByCountry(@RequestParam("countryCode") String countryCode) {
    return storage.getBlobsMetadata(countryCode + "-");
  }

  @GetMapping(value = "/api/tariff/download/csv", produces = "text/csv")
  public ResponseEntity<byte[]> downloadLatestTariffsCsvByCountry(@RequestParam("countryCode") String countryCode,
                                                                  HttpServletResponse response) {
    List<TariffRatesMetadata> blobsMetadata = storage.getBlobsMetadata(countryCode + "-");
    TariffRatesMetadata latestTariffRates = blobsMetadata.stream().filter(TariffRatesMetadata::isLatestUpload).findFirst().get();
    response.setHeader("Content-Disposition", "attachment; filename=" + countryCode + ".csv");

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
    HttpEntity<String> entity = new HttpEntity<>(headers);

    return restTemplate.exchange(latestTariffRates.getUrl(), HttpMethod.GET, entity, byte[].class);
  }

  @GetMapping(value = "/api/tariff/download/json", produces = "application/json")
  public List<Tariff> downloadLatestTariffsJsonByCountry(@RequestParam("countryCode") String countryCode,
                                                         HttpServletResponse response) throws InvalidCsvFileException {
    List<TariffRatesMetadata> blobsMetadata = storage.getBlobsMetadata(countryCode + "-");
    TariffRatesMetadata latestTariffRates = blobsMetadata.stream().filter(TariffRatesMetadata::isLatestUpload).findFirst().get();

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> latestTariffRatesCsv = restTemplate.exchange(latestTariffRates.getUrl(), HttpMethod.GET, entity, String.class);

    List<Tariff> tariffs = tariffCsvTranslator.translate(countryCode, new StringReader(latestTariffRatesCsv.getBody()));
    List<TariffDoc> tariffDocs = tariffDocGateway.getTariffDocs();

    for (Tariff t : tariffs) {
      String hs2 = t.getHs6().substring(0, 2);
      List<TariffDoc> filteredTariffDocs =
        tariffDocs.stream().filter(td -> td.getFtaPublicationHs2Codes().contains(hs2)).collect(Collectors.toList());
      List<Link> links =
        filteredTariffDocs.stream().map(td -> new Link(td.getMetadataStoragePath(), null)).collect(Collectors.toList());
      t.setLinks(links);
    }

    response.setHeader("Content-Disposition", "attachment; filename=" + countryCode + ".json");
    return tariffs;
  }

  @PreAuthorize("hasRole('ROLE_EDSP')")
  @PutMapping("/api/tariffs/save")
  public String saveTariffs(@RequestParam("countryCode") String countryCode,
                            @RequestBody TariffRatesUpload tariffRatesUpload) {
    try {
      //use the translator to validate the csv string
      tariffCsvTranslator.translate(countryCode, new StringReader(tariffRatesUpload.csv));
    } catch (InvalidCsvFileException e) {
      return e.getMessage();
    }

    String timestampedFileName = String.format("%s-%s.csv", countryCode, LocalDateTime.now().toString());
    log.info("Creating file {}", timestampedFileName);
    storage.save(timestampedFileName, tariffRatesUpload.csv, authenticationFacade.getUserName());
    return "success";
  }
}
