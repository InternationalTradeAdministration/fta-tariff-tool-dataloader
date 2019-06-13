package gov.ita.terrafreights.tariff;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
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
  private String tariffRateQuotaNotes;
  private Boolean tariffEliminated;
  private String partnerName;
  private String reporterName;
  private Integer partnerStartYear;
  private Integer reporterStartYear;
  private String partnerAgreementName;
  private String reporterAgreementName;
  private String quotaName;
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
    return tariffLine.substring(0, 1);
  }

  public String getHsPrefix2() {
    return tariffLine.substring(1, 2);
  }

  public String getHsPrefix3() {
    return tariffLine.substring(2, 3);
  }

  public String getHsPrefix4() {
    return tariffLine.substring(3, 4);
  }

  public String getHsPrefix5() {
    return tariffLine.substring(4, 5);
  }

  public String getHsPrefix6() {
    return tariffLine.substring(5, 6);
  }

  public String getHsPrefix7() {
    return tariffLine.substring(6, 7);
  }

  public String getHsPrefix8() {
    return tariffLine.substring(7, 8);
  }

  public String getHsPrefix9() {
    return tariffLine.substring(8, 9);
  }
}
