package gov.ita.tarifftooldataloader.tariff;

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

  public void isValid(String countryCode, Reader csvReader) throws InvalidCsvFileException {
    translate(countryCode, csvReader);
  }

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
          .id(Long.parseLong(csvRecord.get("ID")))
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
          .countryCode(countryCode)
          .hs6(csvRecord.get("HS6"))
          .hs6Description(csvRecord.get("HS6_Desc"))
          .stagingBasket(csvRecord.get("StagingBasket"))
          .productType(csvRecord.get("ProductType"))
          .baseRate(doubleParser(csvRecord.get("Base_Rate")))
          .baseRateAlt(csvRecord.get("Base_Rate_Alt"))
          .build();

        List<Rate> rates = new ArrayList<>();
        headers.forEach((header, position) -> {
          if (header.substring(0, 1).equals("Y") && !header.contains("Alt")) {
            Integer year = Integer.parseInt(header.replaceAll("[^\\d]", ""));
            Double value = doubleParser(csvRecord.get(header));
            if (value != null && value != 0) rates.add(new Rate(year, value));
          }
        });
        tf.setRates(rates);

        List<RateAlt> rateAlts = new ArrayList<>();
        headers.forEach((header, position) -> {
          if (header.substring(0, 1).equals("Y") && header.contains("Alt")) {
            Integer year = Integer.parseInt(header.replaceAll("[^\\d]", ""));
            String value = csvRecord.get(header);
            if (value != null) rateAlts.add(new RateAlt(year, value));
          }
        });
        tf.setRateAlts(rateAlts);

        List<Link> links = new ArrayList<>();
        if (headers.containsKey("Link_Url") && csvRecord.get("Link_Url") != null)
          links.add(new Link(csvRecord.get("Link_Url"), csvRecord.get("Link_Text")));
        if (headers.containsKey("Link_Url2") && csvRecord.get("Link_Url2") != null)
          links.add(new Link(csvRecord.get("Link_Url2"), csvRecord.get("Link_Text2")));
        if (headers.containsKey("Link_Url3") && csvRecord.get("Link_Url3") != null)
          links.add(new Link(csvRecord.get("Link_Url3"), csvRecord.get("Link_Text3")));
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
