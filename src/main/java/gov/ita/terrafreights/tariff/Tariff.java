package gov.ita.terrafreights.tariff;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.ita.terrafreights.tariff.country.Country;
import gov.ita.terrafreights.tariff.hs6.HS6;
import gov.ita.terrafreights.tariff.link.Link;
import gov.ita.terrafreights.tariff.product.ProductType;
import gov.ita.terrafreights.tariff.rate.Rate;
import gov.ita.terrafreights.tariff.stagingbasket.StagingBasket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tariff {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long legacyId;
  private String tariffLine;
  private String description;
  private String sectorCode;
  private String baseRate;
  private Integer finalYear;
  private Integer tariffRateQuota;
  private String tariffRateQuotaNote;
  private Boolean tariffEliminated;
  private String partnerName;
  private String reporterName;
  private Integer partnerStartYear;
  private Integer reporterStartYear;
  private String quotaName;
  private String ruleText;

  @ManyToOne
  private Country country;

  @ManyToOne
  private HS6 hs6;

  @ManyToOne
  private StagingBasket stagingBasket;

  @ManyToOne
  private ProductType productType;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "tariff_id")
  private List<Link> links;

  @OneToMany
  @JoinColumn(name = "tariff_id")
  private List<Rate> rates;

  @JsonProperty("annualRates")
  public Map<String, String> getAnnualRates() {
    Map<String, String> annualRates = new HashMap<>();
    rates.forEach(rate -> annualRates.put("Y" + rate.getYear(), rate.getValue()));
    return annualRates;
  }
}
