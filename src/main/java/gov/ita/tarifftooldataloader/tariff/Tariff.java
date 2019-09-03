package gov.ita.tarifftooldataloader.tariff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tariff {
  private Long id;
  private String tariffLine;
  private String description;
  private String sectorCode;
  private Double baseRate;
  private String baseRateAlt;
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
  private String countryCode;
  private String hs6;
  private String hs6Description;
  private String stagingBasket;
  private String productType;
  private List<Link> links;
  private List<Rate> rates;
  private List<RateAlt> rateAlts;

  @JsonProperty("annualRates")
  public Map<String, Double> getAnnualRates() {
    Map<String, Double> annualRates = new HashMap<>();
    rates.forEach(rate -> annualRates.put("Y" + rate.getYear(), rate.getValue()));
    return annualRates;
  }

  @JsonProperty("annualRateAlts")
  public Map<String, String> getAnnualRateAlts() {
    Map<String, String> annualRateAlts = new HashMap<>();
    rateAlts.forEach(rate -> annualRateAlts.put("Y" + rate.getYear(), rate.getValue()));
    return annualRateAlts;
  }

  @JsonProperty("rulesOfOrigin")
  public Map<String, String> getRulesOfOrigin() {
    Map<String, String> rulesOfOrigin = new HashMap<>();
    for (int i = 1; i < links.size(); i++) {
      rulesOfOrigin.put("link_url_" + i, links.get(i - 1).getLinkUrl());
    }
    return rulesOfOrigin;
  }
}
