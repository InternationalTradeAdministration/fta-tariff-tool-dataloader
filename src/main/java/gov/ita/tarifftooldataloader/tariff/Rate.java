package gov.ita.tarifftooldataloader.tariff;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Rate {

  private Integer year;
  private String value;

  public Rate(Integer year, String value) {
    this.year = year;
    this.value = value;
  }

}
