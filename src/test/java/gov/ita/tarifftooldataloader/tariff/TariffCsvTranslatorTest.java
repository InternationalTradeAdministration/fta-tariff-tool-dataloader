package gov.ita.tarifftooldataloader.tariff;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static gov.ita.tarifftooldataloader.initializer.Helpers.getFileAsReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TariffCsvTranslatorTest {

  private List<Tariff> tariffs;
  private TariffCsvTranslator tariffCsvTranslator;

  @Before
  public void set_up() throws InvalidCsvFileException {
    tariffCsvTranslator = new TariffCsvTranslator();
  }

  @Test
  public void translates_csv_data() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals(100, tariffs.size());
  }

  @Test
  public void translates_ID_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals(439058L, tariffs.get(0).getId(), 0);
  }

  @Test
  public void adds_country_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("KR", tariffs.get(0).getCountryCode());
  }

  @Test
  public void translates_TariffLine_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("01011000", tariffs.get(0).getTariffLine());
  }

  @Test
  public void translates_Description_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("Live purebred breeding horses and asses", tariffs.get(0).getDescription());
  }

  @Test
  public void translates_HS6_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("010110", tariffs.get(0).getHs6());
    assertEquals("PUREBRED BREEDING ANIMAL", tariffs.get(0).getHs6Description());
  }

  @Test
  public void translates_SectorCode_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("15", tariffs.get(0).getSectorCode());
  }

  @Test
  public void translates_BaseRate_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("2.8 cents/kg", tariffs.get(0).getBaseRate());
  }

  @Test
  public void translates_BaseRateAlt_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("8", tariffs.get(1).getBaseRate());
  }

  @Test
  public void translates_FinalYear_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals(2012, tariffs.get(0).getFinalYear(), 0);
  }

  @Test
  public void translates_StagingBasket_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("Immediate", tariffs.get(0).getStagingBasket());
  }

  @Test
  public void translates_TariffRateQuota_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals(3, tariffs.get(0).getTariffRateQuota(), 0);
  }

  @Test
  public void translates_TariffRateQuotaNotes_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("For more information, please see the U.S. Department of Agriculture", tariffs.get(0).getTariffRateQuotaNote());
  }

  @Test
  public void translates_TariffEliminated_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertTrue(tariffs.get(0).getTariffEliminated());
  }

  @Test
  public void translates_Partner_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("Korea", tariffs.get(0).getPartnerName());
  }

  @Test
  public void translates_Reporter_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("United States", tariffs.get(0).getReporterName());
  }

  @Test
  public void translates_PartnerStartYear_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals(2012, tariffs.get(0).getPartnerStartYear(), 0);
  }

  @Test
  public void translates_ReporterStartYear_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals(2014, tariffs.get(0).getReporterStartYear(), 0);
  }

  @Test
  public void translates_QuotaName_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("awesome", tariffs.get(0).getQuotaName());
  }

  @Test
  public void translates_ProductType_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("Agricultural", tariffs.get(0).getProductType());
  }

  @Test
  public void translates_RuleText_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("A change to heading 01.01 through 01.06 from any other chapter.", tariffs.get(0).getRuleText());
  }

  @Test
  public void translates_Link_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
    assertEquals("cool", tariffs.get(0).getLinks().get(0).getLinkText());
    assertEquals("http://cool.com", tariffs.get(0).getLinks().get(0).getLinkUrl());
  }

  @Test
  public void translates_LinkUrl_field() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));
  }

  @Test
  public void translates_line_Rates_values_for_year_2004_to_2041() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("KR", getFileAsReader("korea.csv"));

    assertEquals(12, tariffs.get(0).getRates().size(), 0);

    Rate rate2004 = tariffs.get(0).getRates().stream().filter(r -> r.getYear().equals(2004)).findFirst().get();
    assertEquals("$0.724/kg + 10.4%", rate2004.getValue());

    Rate rate2011 = tariffs.get(0).getRates().stream().filter(r -> r.getYear().equals(2011)).findFirst().get();
    assertEquals("4.4", rate2011.getValue());
  }

  @Test
  public void translates_line_Rates_values_from_year_1_to_x() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("CA-USMCA", getFileAsReader("canada-usmca.csv"));
    Tariff tariff = tariffs.get(26);
    assertEquals(5, tariff.getRates().size(), 0);
    Rate rate2004 = tariff.getRates().stream().filter(r -> r.getYear().equals(1)).findFirst().get();
    assertEquals("249% but not less than $3.78/kg", rate2004.getValue());

    Rate rate2011 = tariff.getRates().stream().filter(r -> r.getYear().equals(3)).findFirst().get();
    assertEquals("249% but not less than $3.78/kg", rate2011.getValue());
  }

  @Test
  public void translates_line_Link_Urls_values() throws InvalidCsvFileException {
    tariffs = tariffCsvTranslator.translate("CA-USMCA", getFileAsReader("canada-usmca.csv"));
    Tariff tariff = tariffs.get(0);
    assertEquals(3, tariff.getLinks().size(), 0);
    assertEquals("such a cool link", tariff.getLinks().get(0).getLinkText());
    assertEquals("https://cool.com", tariff.getLinks().get(0).getLinkUrl());
    assertEquals("another cool link", tariff.getLinks().get(1).getLinkText());
    assertEquals("http://what.ever", tariff.getLinks().get(1).getLinkUrl());
    assertEquals("plenty links", tariff.getLinks().get(2).getLinkText());
    assertEquals("https://plenty.com", tariff.getLinks().get(2).getLinkUrl());
  }

  @Test(expected = InvalidCsvFileException.class)
  public void throws_error_when_missing_required_header_field() throws InvalidCsvFileException {
    tariffCsvTranslator.translate("KR", getFileAsReader("korea_missing_header.csv"));
  }

  @Test(expected = InvalidCsvFileException.class)
  public void throws_error_when_number_field_is_invalid() throws InvalidCsvFileException {
    tariffCsvTranslator.translate("KR", getFileAsReader("korea_invalid_number.csv"));
  }
}
