package gov.ita.tarifftooldataloader.opendatacatalog;

import lombok.Data;

import java.util.List;

@Data
public class OpenDataCatalog {
  String conformsTo = "https://project-open-data.cio.gov/v1.1/schema";
  List<Dataset> dataset;
}
