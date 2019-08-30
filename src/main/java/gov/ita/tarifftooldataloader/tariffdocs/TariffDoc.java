package gov.ita.tarifftooldataloader.tariffdocs;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffDoc {
  @JsonAlias({"metadata_storage_path"})
  private String metadataStoragePath;
  @JsonAlias({"FTA_Publication_HS_Code"})
  private String ftaPublicationHsCode;

  public String[] getFtaPublicationHsCode() {
    if(ftaPublicationHsCode != null)
      return ftaPublicationHsCode.split(";");
    return null;
  }
}
