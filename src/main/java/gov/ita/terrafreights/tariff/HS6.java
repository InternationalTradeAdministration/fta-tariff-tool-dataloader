package gov.ita.terrafreights.tariff;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
public class HS6 {
  @Id
  private String code;
  private String description;
}
