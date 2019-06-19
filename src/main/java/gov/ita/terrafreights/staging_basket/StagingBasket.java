package gov.ita.terrafreights.staging_basket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class StagingBasket {
  @Id
  private Long id;
  private String description;
}
