package gov.ita.terrafreights.tariff;

import gov.ita.terrafreights.security.AuthenticationFacade;
import gov.ita.terrafreights.storage.Storage;
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
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TariffControllerTest {

  @Mock
  private Storage storage;

  @Mock
  private AuthenticationFacade authenticationFacade;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private HttpServletResponse response;

  @Mock
  private TariffCsvTranslator tariffCsvTranslator;

  private TariffController tariffController;

  @Captor
  private ArgumentCaptor<HttpEntity> acHttpEntity;

  @Before
  public void set_up() {
    TariffRatesMetadata cool = new TariffRatesMetadata("cool", "cool.com", null, null);
    cool.setLatestUpload(true);
    when(storage.getBlobsMetadata("AU-")).thenReturn(Collections.singletonList(cool));
    tariffController = new TariffController(storage, authenticationFacade, restTemplate, tariffCsvTranslator);
  }

  @Test
  public void returns_upload_log_by_country() {
    List<TariffRatesMetadata> meta = tariffController.getTariffUploadLogByCountry("AU");

    assertEquals(1, meta.size());
    assertEquals("cool", meta.get(0).name);
  }

  @Test
  public void downloads_latest_tariffs_by_country() {
    tariffController.downloadLatestTariffsCsvByCountry("AU", response);

    verify(storage).getBlobsMetadata("AU-");
    verify(restTemplate).exchange(eq("cool.com"), eq(HttpMethod.GET), acHttpEntity.capture(), eq(byte[].class));
    assertTrue(acHttpEntity.getValue().getHeaders().getAccept().contains(MediaType.APPLICATION_OCTET_STREAM));
  }

  @Test
  public void save_tariffs() {
    when(authenticationFacade.getUserName()).thenReturn("TestUser@trade.gov");

    TariffRatesUpload tariffRatesUpload = new TariffRatesUpload();
    tariffRatesUpload.setCsv("some csv");
    String message = tariffController.saveTariffs("AU", tariffRatesUpload);

    verify(storage).save(anyString(),eq("some csv"), eq("text/csv"), eq("TestUser@trade.gov"));
    assertEquals("success", message);
  }
}