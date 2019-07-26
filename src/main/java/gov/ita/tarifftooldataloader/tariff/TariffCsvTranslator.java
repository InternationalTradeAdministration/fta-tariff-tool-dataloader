package gov.ita.tarifftooldataloader.tariff;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

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
          .withIgnoreHeaderCase()
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
          if (header.substring(0, 1).equalsIgnoreCase("y") && !header.toLowerCase().contains("alt")) {
            Integer year = Integer.parseInt(removeNonNumericCharacters(header));
            Double value = doubleParser(csvRecord.get(header));
            if (value != null && value != 0) rates.add(new Rate(year, value));
          }
        });
        tf.setRates(rates);

        List<RateAlt> rateAlts = new ArrayList<>();
        headers.forEach((header, position) -> {
          if (header.substring(0, 1).equalsIgnoreCase("y") && header.toLowerCase().contains("alt")) {
            Integer year = Integer.parseInt(removeNonNumericCharacters(header));
            String value = csvRecord.get(header);
            if (value != null) rateAlts.add(new RateAlt(year, value));
          }
        });
        tf.setRateAlts(rateAlts);

        Map<Integer, Link> linkMap = new HashMap<>();
        headers.forEach((header, position) -> {
          if (header.toLowerCase().contains("link")) {
            int linkIndex = 0;
            String linkPosition = removeNonNumericCharacters(header);
            if (!linkPosition.isEmpty()) linkIndex = Integer.parseInt(linkPosition);

            Link link = linkMap.get(linkIndex);
            if (link != null) {
              if (header.toLowerCase().contains("text")) {
                link.setLinkText(csvRecord.get(header));
              } else {
                link.setLinkUrl(csvRecord.get(header));
              }
            } else {
              if (header.toLowerCase().contains("text")) {
                linkMap.put(linkIndex, new Link(null, csvRecord.get(header)));
              } else {
                linkMap.put(linkIndex, new Link(csvRecord.get(header), null));
              }
            }
          }
        });

        List<Link> links = linkMap.values().stream()
          .filter(link -> link.getLinkText() != null || link.getLinkUrl() != null).collect(Collectors.toList());
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

  private String removeNonNumericCharacters(String string) {
    return string.replaceAll("[^\\d]", "");
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
