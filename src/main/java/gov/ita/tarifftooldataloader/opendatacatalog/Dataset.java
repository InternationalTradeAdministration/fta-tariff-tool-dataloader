package gov.ita.tarifftooldataloader.opendatacatalog;

import lombok.Data;

import java.time.LocalDateTime;

@Data
class Dataset {
  String accessLevel;
  String[] bureauCode;
  ContactPoint contactPoint;
  String description;
  Distribution[] distribution;
  String identifier;
  String[] keyword;
  LocalDateTime modified;
  String[] programCode;
  Publisher publisher;
  String title;
}
