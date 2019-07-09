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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TariffCsvTranslator {

  public List<Tariff> translate(String countryCode, Reader csvReader) throws InvalidCsvFileException {
    CSVParser csvParser;
    List<Tariff> tariffs = new ArrayList<>();
    int i = 1;

    try {
      csvParser = new CSVParser(
        csvReader,
        CSVFormat.DEFAULT
          .withFirstRecordAsHeader()
          .withTrim()
          .withNullString("")
      );

      Map<String, Integer> headers = csvParser.getHeaderMap();

      for (CSVRecord csvRecord : csvParser) {
        Tariff tf = Tariff.builder()
          .legacyId(Long.parseLong(csvRecord.get("ID")))
          .tariffLine(csvRecord.get("TL"))
          .description(csvRecord.get("TL_Desc"))
          .sectorCode(csvRecord.get("Sector_Code"))
          .finalYear(intParser(csvRecord.get("Final_Year")))
          .tariffRateQuota(intParser(csvRecord.get("TRQ_Quota")))
          .tariffRateQuotaNote(csvRecord.get("TRQ_Note"))
          .tariffEliminated(Boolean.parseBoolean(csvRecord.get("Tariff_Eliminated")))
          .partnerName(csvRecord.get("PartnerName"))
          .reporterName(csvRecord.get("ReporterName"))
          .partnerStartYear(intParser(csvRecord.get("PartnerStartYear")))
          .reporterStartYear(intParser(csvRecord.get("ReporterStartYear")))
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

        String baseRateAlt = csvRecord.get("Base_Rate_Alt");
        String baseRate = baseRateAlt != null ? baseRateAlt : csvRecord.get("Base_Rate");
        tf.setBaseRate(baseRate);

        List<Rate> rates = new ArrayList<>();
        Map<Integer, String> tempRates = new HashMap<>();
        headers.forEach((header, position) -> {
          if (header.substring(0, 1).equals("Y") || (header.length() > 3 && header.substring(0, 4).equals("Alt_"))) {
            Integer year = Integer.parseInt(header.replaceAll("[^\\d]", ""));
            String value = null;
            String alt = null;

            if (header.contains("_Alt") || header.contains("Alt_")) {
              alt = csvRecord.get(header);
            } else {
              value = csvRecord.get(header);
            }

            if (value != null && doubleParser(value) != 0) tempRates.put(year, value);
            if (alt != null) tempRates.put(year, alt);
          }
        });
        tempRates.forEach((year, value) -> rates.add(new Rate(year, value)));

        List<Link> links = new ArrayList<>();
        if (headers.containsKey("Link_Url") && csvRecord.get("Link_Url") != null)
          links.add(new Link(csvRecord.get("Link_Url"), csvRecord.get("Link_Text")));
        if (headers.containsKey("Link_Url2") && csvRecord.get("Link_Url2") != null)
          links.add(new Link(csvRecord.get("Link_Url2"), csvRecord.get("Link_Text2")));
        if (headers.containsKey("Link_Url3") && csvRecord.get("Link_Url3") != null)
          links.add(new Link(csvRecord.get("Link_Url3"), csvRecord.get("Link_Text3")));

        tf.setRates(rates);
        tf.setLinks(links);

        tariffs.add(tf);
        i++;
      }

    } catch (IllegalArgumentException e) {
      e.printStackTrace();

      if (e instanceof NumberFormatException) {
        throw new InvalidCsvFileException("Invalid number format; see record " + i);
      } else {
        throw new InvalidCsvFileException(e.getMessage() + "; see record " + i);
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
