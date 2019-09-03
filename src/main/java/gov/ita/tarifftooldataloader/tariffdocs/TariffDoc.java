package gov.ita.tarifftooldataloader.tariffdocs;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffDoc {
  @JsonAlias({"metadata_storage_path"})
  private String metadataStoragePath;
  @JsonAlias({"FTA_Publication_HS_Code"})
  private String ftaPublicationHsCode;

  public List<String> getFtaPublicationHs2Codes() {
    if(ftaPublicationHsCode != null) {
      List<String> hs6s = Arrays.asList(ftaPublicationHsCode.split(";"));
      return hs6s.stream().map(hs6 -> hs6.substring(0, 2)).collect(Collectors.toList());
    }

    return null;
  }
}
