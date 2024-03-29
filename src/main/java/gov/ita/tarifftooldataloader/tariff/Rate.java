package gov.ita.tarifftooldataloader.tariff;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Rate {

  private Integer year;
  private Double value;

  public Rate(Integer year, Double value) {
    this.year = year;
    this.value = value;
  }

}
