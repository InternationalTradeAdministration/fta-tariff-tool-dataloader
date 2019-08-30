package gov.ita.tarifftooldataloader.tariffdocs;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Profile("development")
public class DevelopmentTariffDocGateway implements TariffDocGateway {
  @Override
  public List<TariffDoc> getTariffDocs() {
    return Collections.emptyList();
  }
}
