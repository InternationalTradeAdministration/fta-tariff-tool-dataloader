package gov.ita.tarifftooldataloader.opendatacatalog;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OpenDataCatalog {

  String conformsTo = "https://project-open-data.cio.gov/v1.1/schema";
  List<Dataset> dataset = new ArrayList<>();

  OpenDataCatalog(LocalDateTime localDateTime) {
    dataset.add(new Dataset(localDateTime));
  }

}
