package gov.ita.tarifftooldataloader.tariffdocs;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("development")
public class DevelopmentTariffDocGateway implements TariffDocGateway {
  @Override
  public List<TariffDoc> getTariffDocs() {
    List<TariffDoc> tariffDocs = new ArrayList<>();
    tariffDocs.add(new TariffDoc("path-a", "10;20;30"));
    tariffDocs.add(new TariffDoc("path-b", "11;22;33"));
    return tariffDocs;
  }
}
