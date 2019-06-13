package gov.ita.terrafreights.tariff;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
public class StagingBasket {
  @Id
  private Long id;
  private String description;
}
