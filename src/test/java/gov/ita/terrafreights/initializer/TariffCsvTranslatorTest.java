package gov.ita.terrafreights.initializer;

import gov.ita.terrafreights.tariff.Tariff;
import gov.ita.terrafreights.tariff.TariffCsvTranslator;
import gov.ita.terrafreights.tariff.rate.Rate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static gov.ita.terrafreights.initializer.Helpers.getFileAsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TariffCsvTranslatorTest {

  private List<Tariff> tariffs;
  private TariffCsvTranslator tariffCsvTranslator;

  @Before
  public void set_up() {
    tariffCsvTranslator = new TariffCsvTranslator();
  }

  @Test
  public void translates_tariff_csv_data() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals(100, tariffs.size());
  }

  @Test
  public void translates_tariff_ID_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals(439058L, tariffs.get(0).getLegacyId(), 0);
  }

  @Test
  public void adds_tariff_country_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("KR", tariffs.get(0).getCountry().getCode());
  }

  @Test
  public void translates_tariff_TariffLine_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("01011000", tariffs.get(0).getTariffLine());
  }

  @Test
  public void translates_tariff_Description_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("Live purebred breeding horses and asses", tariffs.get(0).getDescription());
  }

  @Test
  public void translates_tariff_HS6_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("010110", tariffs.get(0).getHs6().getCode());
    assertEquals("PUREBRED BREEDING ANIMAL", tariffs.get(0).getHs6().getDescription());
  }

  @Test
  public void translates_tariff_SectorCode_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("15", tariffs.get(0).getSectorCode());
  }

  @Test
  public void translates_tariff_BaseRate_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("2.2", tariffs.get(0).getBaseRate());
  }

  @Test
  public void translates_tariff_BaseRateAlt_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("2.8 cents/kg", tariffs.get(0).getBaseRateAlt());
  }

  @Test
  public void translates_tariff_FinalYear_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals(2012, tariffs.get(0).getFinalYear(), 0);
  }

  @Test
  public void translates_tariff_StagingBasket_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals(6, tariffs.get(0).getStagingBasket().getLegacyId(), 0);
    assertEquals("Immediate", tariffs.get(0).getStagingBasket().getDescription());
  }

  @Test
  public void translates_tariff_TariffRateQuota_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals(3, tariffs.get(0).getTariffRateQuota(), 0);
  }

  @Test
  public void translates_tariff_TariffRateQuotaNotes_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("For more information, please see the U.S. Department of Agriculture", tariffs.get(0).getTariffRateQuotaNotes());
  }

  @Test
  public void translates_tariff_TariffEliminated_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertTrue(tariffs.get(0).getTariffEliminated());
  }

  @Test
  public void translates_tariff_Partner_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("Korea", tariffs.get(0).getPartnerName());
  }

  @Test
  public void translates_tariff_Reporter_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("United States", tariffs.get(0).getReporterName());
  }

  @Test
  public void translates_tariff_PartnerStartYear_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals(2012, tariffs.get(0).getPartnerStartYear(), 0);
  }

  @Test
  public void translates_tariff_ReporterStartYear_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals(2014, tariffs.get(0).getReporterStartYear(), 0);
  }

  @Test
  public void translates_tariff_PartnerAgreementName_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("Korea", tariffs.get(0).getPartnerAgreementName());
  }

  @Test
  public void translates_tariff_ReporterAgreementName_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("United States", tariffs.get(0).getReporterAgreementName());
  }

  @Test
  public void translates_tariff_QuotaName_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("awesome", tariffs.get(0).getQuotaName());
  }

  @Test
  public void translates_tariff_ProductType_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals(2, tariffs.get(0).getProductType().getLegacyId(), 0);
    assertEquals("Agricultural", tariffs.get(0).getProductType().getDescription());
  }

  @Test
  public void translates_tariff_RuleText_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("A change to heading 01.01 through 01.06 from any other chapter.", tariffs.get(0).getRuleText());
  }

  @Test
  public void translates_tariff_Link_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
    assertEquals("cool", tariffs.get(0).getLinks().get(0).getLinkText());
    assertEquals("http://cool.com", tariffs.get(0).getLinks().get(0).getLinkUrl());
  }

  @Test
  public void translates_tariff_LinkUrl_field() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));
  }

  @Test
  public void translates_tarrif_line_Rates_values_for_year_2004_to_2041() {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsString("korea.csv"));

    assertEquals(12, tariffs.get(0).getRates().size(), 0);

    Rate rate2004 = tariffs.get(0).getRates().stream().filter(r -> r.getYear().equals(2004)).findFirst().get();
    assertEquals("$0.724/kg + 10.4%", rate2004.getValue());

    Rate rate2011 = tariffs.get(0).getRates().stream().filter(r -> r.getYear().equals(2011)).findFirst().get();
    assertEquals("4.4", rate2011.getValue());
  }

  @Test
  public void translates_tarrif_line_Rates_values_for_year_1_to_x() {
    tariffs = tariffCsvTranslator.translate("CA-USMCA", getFileAsString("canada-usmca.csv"));
    Tariff tariff = tariffs.get(26);
    assertEquals(5, tariff.getRates().size(), 0);
    Rate rate2004 = tariff.getRates().stream().filter(r -> r.getYear().equals(1)).findFirst().get();
    assertEquals("249% but not less than $3.78/kg", rate2004.getValue());

    Rate rate2011 = tariff.getRates().stream().filter(r -> r.getYear().equals(3)).findFirst().get();
    assertEquals("249% but not less than $3.78/kg", rate2011.getValue());
  }

  @Test
  public void translates_tariff_line_Link_Urls_values() {
    tariffs = tariffCsvTranslator.translate("CA-USMCA", getFileAsString("canada-usmca.csv"));
    Tariff tariff = tariffs.get(0);
    assertEquals(3, tariff.getLinks().size(), 0);
    assertEquals("such a cool link", tariff.getLinks().get(0).getLinkText());
    assertEquals("https://cool.com", tariff.getLinks().get(0).getLinkUrl());
    assertEquals("another cool link", tariff.getLinks().get(1).getLinkText());
    assertEquals("http://what.ever", tariff.getLinks().get(1).getLinkUrl());
    assertEquals("plenty links", tariff.getLinks().get(2).getLinkText());
    assertEquals("https://plenty.com", tariff.getLinks().get(2).getLinkUrl());
  }
}
