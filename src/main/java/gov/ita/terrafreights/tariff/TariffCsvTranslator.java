package gov.ita.terrafreights.tariff;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class TariffCsvTranslator {

  public List<Tariff> translate(String country, String csv) {
    CSVParser csvParser;
    Reader reader = new StringReader(csv);
    List<Tariff> tariffs = new ArrayList<>();

    try {
      csvParser = new CSVParser(
        reader,
        CSVFormat.DEFAULT
          .withFirstRecordAsHeader()
          .withTrim()
          .withNullString("")
      );

      for (CSVRecord csvRecord : csvParser) {
        Tariff tf = new Tariff();
        tf.setCountry(country);
        tf.setId(Long.parseLong(csvRecord.get("ID")));
        tf.setTariffLine(csvRecord.get("TL"));
        tf.setDescription(csvRecord.get("TL_Desc"));
        tf.setSectorCode(csvRecord.get("Sector_Code"));
        tf.setBaseRate(csvRecord.get("Base_Rate"));
        tf.setBaseRateAlt(csvRecord.get("Base_Rate_Alt"));
        tf.setFinalYear(intParser(csvRecord.get("Final_Year")));
        tf.setTariffRateQuota(intParser(csvRecord.get("TRQ_Quota")));
        tf.setTariffRateQuotaNotes(csvRecord.get("TRQ_Note"));
        tf.setTariffEliminated(Boolean.parseBoolean(csvRecord.get("Tariff_Eliminated")));
        tf.setPartnerName(csvRecord.get("PartnerName"));
        tf.setReporterName(csvRecord.get("ReporterName"));
        tf.setPartnerStartYear(intParser(csvRecord.get("PartnerStartYear")));
        tf.setReporterStartYear(intParser(csvRecord.get("ReporterStartYear")));
        tf.setPartnerAgreementName(csvRecord.get("PartnerAgreementName"));
        tf.setReporterAgreementName(csvRecord.get("ReporterAgreementName"));
        tf.setQuotaName(csvRecord.get("QuotaName"));
        tf.setRuleText(csvRecord.get("Rule_Text"));
        tf.setLinkText(csvRecord.get("Link_Text"));
        tf.setLinkUrl(csvRecord.get("Link_Url"));

        HS6 hs6 = new HS6(
          csvRecord.get("HS6"),
          csvRecord.get("HS6_Desc")
        );
        tf.setHs6(hs6);

        StagingBasket stagingBasket = new StagingBasket(
          Long.parseLong(csvRecord.get("StagingBasketId")),
          csvRecord.get("StagingBasket")
        );
        tf.setStagingBasket(stagingBasket);


        ProductType productType = new ProductType(
          Long.parseLong(csvRecord.get("Product_Type")),
          csvRecord.get("ProductType")
        );
        tf.setProductType(productType);

        List<Rate> rates = new ArrayList<>();
        for (int i = 2004; i <= 2041; i++) {
          Double value = doubleParser(csvRecord.get("Y".concat(String.valueOf(i))));
          String alt = csvRecord.get("Alt_".concat(String.valueOf(i)));
          rates.add(new Rate(i, value, alt));
        }
        tf.setRates(rates);

        tariffs.add(tf);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return tariffs;
  }

  private Integer intParser(String potentialInteger) {
    if (potentialInteger == null) return null;
    return Integer.parseInt(potentialInteger);
  }

  private Double doubleParser(String potentialDouble) {
    if (potentialDouble == null) return null;
    return Double.parseDouble(potentialDouble);
  }

}
