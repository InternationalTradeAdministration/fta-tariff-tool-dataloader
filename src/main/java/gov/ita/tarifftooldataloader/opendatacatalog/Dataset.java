package gov.ita.tarifftooldataloader.opendatacatalog;

import lombok.Data;

import java.time.LocalDateTime;

@Data
class Dataset {
  String accessLevel = "public";
  String[] bureauCode = {"006:00"};
  ContactPoint contactPoint = new ContactPoint();
  String description = "Application that allows searching for FTA information based on an HS code.";
  Distribution[] distribution = {new Distribution()};
  String identifier = "DOC-4911";
  String[] keyword = {"tariff", "rates", "hs code", "fta"};
  LocalDateTime modified;
  String[] programCode = {"006:036"};
  Publisher publisher = new Publisher();
  String title = "FTA Tariff Tool";

  Dataset(LocalDateTime modified) {
    this.modified = modified;
  }
}
