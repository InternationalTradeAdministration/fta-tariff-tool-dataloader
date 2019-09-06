package gov.ita.tarifftooldataloader.tariff;

import gov.ita.tarifftooldataloader.security.AuthenticationFacade;
import gov.ita.tarifftooldataloader.storage.Storage;
import gov.ita.tarifftooldataloader.tariffdocs.TariffDoc;
import gov.ita.tarifftooldataloader.tariffdocs.TariffDocGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static gov.ita.tarifftooldataloader.initializer.Helpers.getFileAsString;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TariffControllerTest {

  @Mock
  private Storage storage;

  @Mock
  private TariffDocGateway tariffDocGateway;

  @Mock
  private AuthenticationFacade authenticationFacade;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private HttpServletResponse response;

  private TariffController tariffController;

  @Captor
  private ArgumentCaptor<HttpEntity> acHttpEntity;

  TariffRatesMetadata metadata;

  @Before
  public void set_up() {
    metadata = new TariffRatesMetadata("cool", "cool.com", null, null, null);
    tariffController = new TariffController(storage, authenticationFacade, restTemplate, new TariffCsvTranslator(), tariffDocGateway);
  }

  @Test
  public void returns_upload_log_by_country() {
    when(storage.getBlobsMetadata("AU")).thenReturn(Collections.singletonList(metadata));
    List<TariffRatesMetadata> meta = tariffController.getTariffUploadLogByCountry("AU");

    assertEquals(1, meta.size());
    assertEquals("cool", meta.get(0).name);
  }

  @Test
  public void downloads_latest_tariffs_by_country() {
    when(storage.getBlobsMetadata("AU.csv")).thenReturn(Collections.singletonList(metadata));

    tariffController.downloadLatestTariffsCsvByCountry("AU", response);

    verify(storage).getBlobsMetadata("AU.csv");
    verify(restTemplate).exchange(eq("cool.com"), eq(HttpMethod.GET), acHttpEntity.capture(), eq(byte[].class));
    assertTrue(acHttpEntity.getValue().getHeaders().getAccept().contains(MediaType.APPLICATION_OCTET_STREAM));
  }

  @Test
  public void save_tariffs() {
    when(authenticationFacade.getUserName()).thenReturn("TestUser@trade.gov");

    TariffRatesUpload tariffRatesUpload = new TariffRatesUpload();
    tariffRatesUpload.setCsv("some csv");
    String message = tariffController.saveTariffs("AU", tariffRatesUpload);

    verify(storage, times(2)).save(anyString(), eq("some csv"), eq("TestUser@trade.gov"));
    assertEquals("success", message);
  }

  @Test
  public void applies_rules_of_origin() throws InvalidCsvFileException {
    TariffRatesMetadata greeceMetaData = new TariffRatesMetadata(null, "http://greece.csv", null, null, null);

    when(storage.getBlobsMetadata("GR.csv")).thenReturn(Collections.singletonList(greeceMetaData));
    when(restTemplate.exchange(eq("http://greece.csv"), eq(HttpMethod.GET), any(), eq(String.class)))
      .thenReturn(ResponseEntity.of(Optional.ofNullable(getFileAsString("greece.csv"))));

    List<TariffDoc> tariffDocs = new ArrayList<>();
    tariffDocs.add(new TariffDoc("http://kalivakia.pdf", "123456"));
    tariffDocs.add(new TariffDoc("http://kakoperato.pdf", "981111"));
    tariffDocs.add(new TariffDoc("http://kambos.pdf", "982222"));
    when(tariffDocGateway.getTariffDocs()).thenReturn(tariffDocs);

    List<Tariff> tariffs = tariffController.downloadLatestTariffsJsonByCountry("GR", mock(HttpServletResponse.class));

    assertEquals("http://kalivakia.pdf", tariffs.get(0).getLinks().get(0).getLinkUrl());
    assertEquals("http://kakoperato.pdf", tariffs.get(1).getLinks().get(0).getLinkUrl());
    assertEquals("http://kambos.pdf", tariffs.get(1).getLinks().get(1).getLinkUrl());
  }
}