package gov.ita.terrafreights.tariff;

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
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;
  private Integer year;
  private Double value;
  private String alt;

  public Rate(Integer year, Double value, String alt) {
    this.year = year;
    this.value = value;
    this.alt = alt;
  }

}
