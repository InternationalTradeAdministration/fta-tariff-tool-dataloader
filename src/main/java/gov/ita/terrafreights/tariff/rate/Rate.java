package gov.ita.terrafreights.tariff.rate;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Rate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Integer year;
  private String value;

  public Rate(Integer year, String value) {
    this.year = year;
    this.value = value;
  }

}
