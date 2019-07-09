package gov.ita.terrafreights;

import gov.ita.terrafreights.tariff.InvalidCsvFileException;

interface DataSeeder {
  void seed() throws InvalidCsvFileException;
}
