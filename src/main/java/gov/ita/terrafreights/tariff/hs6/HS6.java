package gov.ita.terrafreights.tariff.hs6;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class HS6 {
  @Id
  private String code;
  private String description;
}
