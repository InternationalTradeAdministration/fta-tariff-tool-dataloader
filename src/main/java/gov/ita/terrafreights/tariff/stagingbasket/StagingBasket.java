package gov.ita.terrafreights.tariff.stagingbasket;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class StagingBasket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String description;

  public StagingBasket(Long id, String description) {
    this.id = id;
    this.description = description;
  }
}
