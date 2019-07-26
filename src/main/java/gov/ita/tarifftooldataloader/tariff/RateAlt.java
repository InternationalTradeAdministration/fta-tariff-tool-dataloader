package gov.ita.tarifftooldataloader.tariff;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RateAlt {

  private Integer year;
  private String value;

  public RateAlt(Integer year, String value) {
    this.year = year;
    this.value = value;
  }

}
