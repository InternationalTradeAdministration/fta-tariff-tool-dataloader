package gov.ita.tarifftooldataloader.tariffdocs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Profile({"production", "staging"})
public class ProductionTariffDocGateway implements TariffDocGateway {

  @Value("${tarifftooldataloader.tariff-docs-url}")
  private String tariffDocsUrl;

  private RestTemplate restTemplate;

  ProductionTariffDocGateway(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public List<TariffDoc> getTariffDocs() {
    ResponseEntity<List<TariffDoc>> tariffDocResponse =
      restTemplate.exchange(
        tariffDocsUrl,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<TariffDoc>>() {
        }
      );
    return tariffDocResponse.getBody();
  }
}
