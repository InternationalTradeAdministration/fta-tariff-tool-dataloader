package gov.ita.terrafreights;

import gov.ita.terrafreights.country.Country;

import java.util.List;

public interface Storage {
  void save(String fileName, String fileContent, String contentType, String user);
  boolean containerExists();
  void createContainer();
  List<Country> getCountries();
}
