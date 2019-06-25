package gov.ita.terrafreights.tariff;

import gov.ita.terrafreights.tariff.country.Country;
import gov.ita.terrafreights.tariff.hs6.HS6;
import gov.ita.terrafreights.tariff.link.Link;
import gov.ita.terrafreights.tariff.product.ProductType;
import gov.ita.terrafreights.tariff.rate.Rate;
import gov.ita.terrafreights.tariff.stagingbasket.StagingBasket;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class TariffCsvTranslator {

  public List<Tariff> translate(String countryCode, Reader csvReader) {
    CSVParser csvParser;
    List<Tariff> tariffs = new ArrayList<>();

    try {
      csvParser = new CSVParser(
        csvReader,
        CSVFormat.DEFAULT
          .withFirstRecordAsHeader()
          .withTrim()
          .withNullString("")
      );

      for (CSVRecord csvRecord : csvParser) {
        Tariff tf = Tariff.builder()
          .legacyId(Long.parseLong(csvRecord.get("ID")))
          .tariffLine(csvRecord.get("TL"))
          .description(csvRecord.get("TL_Desc"))
          .sectorCode(csvRecord.get("Sector_Code"))
          .finalYear(intParser(csvRecord.get("Final_Year")))
          .tariffRateQuota(intParser(csvRecord.get("TRQ_Quota")))
          .tariffRateQuotaNotes(csvRecord.get("TRQ_Note"))
          .tariffEliminated(Boolean.parseBoolean(csvRecord.get("Tariff_Eliminated")))
          .partnerName(csvRecord.get("PartnerName"))
          .reporterName(csvRecord.get("ReporterName"))
          .partnerStartYear(intParser(csvRecord.get("PartnerStartYear")))
          .reporterStartYear(intParser(csvRecord.get("ReporterStartYear")))
          .partnerAgreementName(csvRecord.get("PartnerAgreementName"))
          .reporterAgreementName(csvRecord.get("ReporterAgreementName"))
          .quotaName(csvRecord.get("QuotaName"))
          .ruleText(csvRecord.get("Rule_Text"))
          .country(new Country(null, countryCode, null))
          .hs6(new HS6(
            csvRecord.get("HS6"),
            csvRecord.get("HS6_Desc")
          ))
          .stagingBasket(new StagingBasket(
            null,
            Long.parseLong(csvRecord.get("StagingBasketId")),
            csvRecord.get("StagingBasket")
          ))
          .productType(new ProductType(
            null,
            Long.parseLong(csvRecord.get("Product_Type")),
            csvRecord.get("ProductType")
          )).build();


        String baseRate = csvRecord.get("Base_Rate_Alt") != null ?
          csvRecord.get("Base_Rate_Alt") :
          csvRecord.get("Base_Rate");
        tf.setBaseRate(baseRate);

        List<Rate> rates = new ArrayList<>();
        List<Link> links = new ArrayList<>();
        if (countryCode.contains("USMCA")) {
          for (int i = 1; i <= 30; i++) {
            String value = csvRecord.get("YEAR".concat(String.valueOf(i)));
            String alt = csvRecord.get("YEAR".concat(String.valueOf(i).concat("_Alt")));
            if (alt != null) {
              rates.add(new Rate(i, alt));
            } else if (value != null && doubleParser(value) != 0) {
              rates.add(new Rate(i, value));
            }
          }

          if (csvRecord.get("Link_Url") != null)
            links.add(new Link(null, csvRecord.get("Link_Url"), csvRecord.get("Link_Text")));
          if (csvRecord.get("Link_Url2") != null)
            links.add(new Link(null, csvRecord.get("Link_Url2"), csvRecord.get("Link_Text2")));
          if (csvRecord.get("Link_Url3") != null)
            links.add(new Link(null, csvRecord.get("Link_Url3"), csvRecord.get("Link_Text3")));

        } else {
          for (int i = 2004; i <= 2041; i++) {
            String value = csvRecord.get("Y".concat(String.valueOf(i)));
            String alt = csvRecord.get("Alt_".concat(String.valueOf(i)));
            if (alt != null) {
              rates.add(new Rate(i, alt));
            } else if (value != null && doubleParser(value) != 0) {
              rates.add(new Rate(i, value));
            }
          }

          if (csvRecord.get("Link_Url") != null)
            links.add(new Link(null, csvRecord.get("Link_Url"), csvRecord.get("Link_Text")));
        }

        tf.setRates(rates);
        tf.setLinks(links);

        tariffs.add(tf);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return tariffs;
  }

  private Integer intParser(String potentialInteger) {
    if (potentialInteger == null) return null;
    try {
      return Integer.parseInt(potentialInteger);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private Double doubleParser(String potentialDouble) {
    if (potentialDouble == null) return null;
    return Double.parseDouble(potentialDouble);
  }

}
