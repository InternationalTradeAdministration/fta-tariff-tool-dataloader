package gov.ita.terrafreights.tariff;

public class InvalidCsvFileException extends Exception {
  InvalidCsvFileException(String message) {
    super(message);
  }
}
