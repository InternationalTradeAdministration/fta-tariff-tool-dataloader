package gov.ita.terrafreights.initializer;

import gov.ita.terrafreights.tariff.Rate;
import gov.ita.terrafreights.tariff.Tariff;
import gov.ita.terrafreights.tariff.TariffCsvTranslator;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static gov.ita.terrafreights.initializer.Helpers.getFileAsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TariffCsvTranslatorTest {

  private String csv = getFileAsString("korea.csv");
  private List<Tariff> tariffs;

  @Before
  public void set_up() {
    TariffCsvTranslator tariffCsvTranslator = new TariffCsvTranslator();
    tariffs = tariffCsvTranslator.translate("KR", csv);
  }

  @Test
  public void translates_tariff_csv_data() {
    assertEquals(100, tariffs.size());
  }

  @Test
  public void translates_tariff_ID_field() {
    assertEquals(439058L, tariffs.get(0).getLegacyId(), 0);
  }

  @Test
  public void adds_tariff_country_field() {
    assertEquals("KR", tariffs.get(0).getCountry().getCode());
  }

  @Test
  public void translates_tariff_TariffLine_field() {
    assertEquals("01011000", tariffs.get(0).getTariffLine());
  }

  @Test
  public void translates_tariff_Description_field() {
    assertEquals("Live purebred breeding horses and asses", tariffs.get(0).getDescription());
  }

  @Test
  public void translates_tariff_HS6_field() {
    assertEquals("010110", tariffs.get(0).getHs6().getCode());
    assertEquals("PUREBRED BREEDING ANIMAL", tariffs.get(0).getHs6().getDescription());
  }

  @Test
  public void translates_tariff_SectorCode_field() {
    assertEquals("15", tariffs.get(0).getSectorCode());
  }

  @Test
  public void translates_tariff_BaseRate_field() {
    assertEquals("2.2", tariffs.get(0).getBaseRate());
  }

  @Test
  public void translates_tariff_BaseRateAlt_field() {
    assertEquals("2.8 cents/kg", tariffs.get(0).getBaseRateAlt());
  }

  @Test
  public void translates_tariff_FinalYear_field() {
    assertEquals(2012, tariffs.get(0).getFinalYear(), 0);
  }

  @Test
  public void translates_tariff_StagingBasket_field() {
    assertEquals(6, tariffs.get(0).getStagingBasket().getLegacyId(), 0);
    assertEquals("Immediate", tariffs.get(0).getStagingBasket().getDescription());
  }

  @Test
  public void translates_tariff_TariffRateQuota_field() {
    assertEquals(3, tariffs.get(0).getTariffRateQuota(), 0);
  }

  @Test
  public void translates_tariff_TariffRateQuotaNotes_field() {
    assertEquals("For more information, please see the U.S. Department of Agriculture", tariffs.get(0).getTariffRateQuotaNotes());
  }

  @Test
  public void translates_tariff_TariffEliminated_field() {
    assertTrue(tariffs.get(0).getTariffEliminated());
  }

  @Test
  public void translates_tariff_Partner_field() {
    assertEquals("Korea", tariffs.get(0).getPartnerName());
  }

  @Test
  public void translates_tariff_Reporter_field() {
    assertEquals("United States", tariffs.get(0).getReporterName());
  }

  @Test
  public void translates_tariff_PartnerStartYear_field() {
    assertEquals(2012, tariffs.get(0).getPartnerStartYear(), 0);
  }

  @Test
  public void translates_tariff_ReporterStartYear_field() {
    assertEquals(2014, tariffs.get(0).getReporterStartYear(), 0);
  }

  @Test
  public void translates_tariff_PartnerAgreementName_field() {
    assertEquals("Korea", tariffs.get(0).getPartnerAgreementName());
  }

  @Test
  public void translates_tariff_ReporterAgreementName_field() {
    assertEquals("United States", tariffs.get(0).getReporterAgreementName());
  }

  @Test
  public void translates_tariff_QuotaName_field() {
    assertEquals("awesome", tariffs.get(0).getQuotaName());
  }

  @Test
  public void translates_tariff_ProductType_field() {
    assertEquals(2, tariffs.get(0).getProductType().getLegacyId(), 0);
    assertEquals("Agricultural", tariffs.get(0).getProductType().getDescription());
  }

  @Test
  public void translates_tariff_RuleText_field() {
    assertEquals("A change to heading 01.01 through 01.06 from any other chapter.", tariffs.get(0).getRuleText());
  }

  @Test
  public void translates_tariff_LinkText_field() {
    assertEquals("cool", tariffs.get(0).getLinkText());
  }

  @Test
  public void translates_tariff_LinkUrl_field() {
    assertEquals("http://cool.com", tariffs.get(0).getLinkUrl());
  }

  @Test
  public void translates_tarrif_line_Rates_values() {
    assertEquals(38, tariffs.get(0).getRates().size(), 0);

    Rate rate2004 = tariffs.get(0).getRates().stream().filter(r -> r.getYear().equals(2004)).findFirst().get();
    assertEquals(0, rate2004.getValue(), 0);
    assertEquals("$0.724/kg + 10.4%", rate2004.getAlt());

    Rate rate2011 = tariffs.get(0).getRates().stream().filter(r -> r.getYear().equals(2011)).findFirst().get();
    assertEquals(4.4, rate2011.getValue(), 0);
    assertEquals("frog", rate2011.getAlt());
  }
}
