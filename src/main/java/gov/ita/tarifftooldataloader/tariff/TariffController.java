package gov.ita.tarifftooldataloader.tariff;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    TariffRatesMetadata latest = blobsMetadata.stream().filter(TariffRatesMetadata::isLatestUpload).findFirst().get();
    response.setHeader("Content-Disposition", "attachment; filename=" + countryCode + ".csv");

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
    HttpEntity<String> entity = new HttpEntity<>(headers);

    return restTemplate.exchange(latest.getUrl(), HttpMethod.GET, entity, byte[].class);
  }

  @GetMapping(value = "/api/tariff/download/json", produces = "application/json")
  public ResponseEntity<byte[]> downloadLatestTariffsJsonByCountry(@RequestParam("countryCode") String countryCode,
                                                         HttpServletResponse response) throws InvalidCsvFileException {
    List<TariffRatesMetadata> blobsMetadata = storage.getBlobsMetadata(countryCode + ".json");
    TariffRatesMetadata latest = blobsMetadata.get(0);
    response.setHeader("Content-Disposition", "attachment; filename=" + countryCode + ".json");

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
    HttpEntity<String> entity = new HttpEntity<>(headers);
    return restTemplate.exchange(latest.getUrl(), HttpMethod.GET, entity, byte[].class);
  }

  @PreAuthorize("hasRole('ROLE_EDSP')")
  @PutMapping("/api/tariffs/save")
  public String saveTariffs(@RequestParam("countryCode") String countryCode,
                            @RequestBody TariffRatesUpload tariffRatesUpload) throws JsonProcessingException {
    List<Tariff> tariffs;
    try {
      tariffs = tariffCsvTranslator.translate(countryCode, new StringReader(tariffRatesUpload.csv));
    } catch (InvalidCsvFileException e) {
      return e.getMessage();
    }

    List<TariffDoc> tariffDocs = tariffDocGateway.getTariffDocs();
    for (Tariff t: tariffs) {
      String hs2 = t.getHs6().substring(0, 2);
      List<TariffDoc> filteredTariffDocs =
        tariffDocs.stream().filter(td -> td.getFtaPublicationHs2Codes().contains(hs2)).collect(Collectors.toList());
      List<Link> links =
        filteredTariffDocs.stream().map(td -> new Link(td.getMetadataStoragePath(), null)).collect(Collectors.toList());
      t.setLinks(links);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    String tariffsJson = objectMapper.writeValueAsString(tariffs);
    storage.save(countryCode + ".json", tariffsJson, authenticationFacade.getUserName());

    String timestampedFileName = String.format("%s-%s.csv", countryCode, LocalDateTime.now().toString());
    log.info("Creating file {}", timestampedFileName);
    storage.save(timestampedFileName, tariffRatesUpload.csv, authenticationFacade.getUserName());
    return "success";
  }
}
