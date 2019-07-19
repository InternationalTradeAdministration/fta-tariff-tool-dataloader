package gov.ita.terrafreights.country;

import gov.ita.terrafreights.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CountryController {

  @Autowired
  private Storage storage;

  @GetMapping("/api/countries")
  public List<Country> countries() {
    return storage.getCountries();
  }

}
