package gov.ita.terrafreights.tariff;

import gov.ita.terrafreights.product.ProductType;
import gov.ita.terrafreights.staging_basket.StagingBasket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tariff {

  @Id
  private Long id;
  private String country;
  private String tariffLine;
  private String description;
  private String sectorCode;
  private String baseRate;
  private String baseRateAlt;
  private Integer finalYear;
  private Integer tariffRateQuota;
  @Lob
  private String tariffRateQuotaNotes;
  private Boolean tariffEliminated;
  private String partnerName;
  private String reporterName;
  private Integer partnerStartYear;
  private Integer reporterStartYear;
  private String partnerAgreementName;
  private String reporterAgreementName;
  private String quotaName;
  @Lob
  private String ruleText;
  private String linkText;
  private String linkUrl;

  @ManyToOne
  private HS6 hs6;

  @ManyToOne
  private StagingBasket stagingBasket;

  @ManyToOne
  private ProductType productType;

  @OneToMany
  private List<Rate> rates;

  public String getHsPrefix1() {
    return deriveHsPrefix(1);
  }

  public String getHsPrefix2() {
    return deriveHsPrefix(2);
  }

  public String getHsPrefix3() {
    return deriveHsPrefix(3);
  }

  public String getHsPrefix4() {
    return deriveHsPrefix(4);
  }

  public String getHsPrefix5() {
    return deriveHsPrefix(5);
  }

  public String getHsPrefix6() {
    return deriveHsPrefix(6);
  }

  public String getHsPrefix7() {
    return deriveHsPrefix(7);
  }

  public String getHsPrefix8() {
    return deriveHsPrefix(8);
  }

  public String getHsPrefix9() {
    return deriveHsPrefix(9);
  }

  private String deriveHsPrefix(int position) {
    if (tariffLine == null || tariffLine.length() < position) return null;
    return tariffLine.substring(0, position);
  }
}
