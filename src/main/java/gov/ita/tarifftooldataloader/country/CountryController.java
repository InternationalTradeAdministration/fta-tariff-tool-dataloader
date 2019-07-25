package gov.ita.tarifftooldataloader.country;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.ita.tarifftooldataloader.security.AuthenticationFacade;
import gov.ita.tarifftooldataloader.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CountryController {

  @Autowired
  private Storage storage;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private AuthenticationFacade authenticationFacade;

  @GetMapping("/api/countries")
  public List<Country> countries() {
    return storage.getCountries();
  }

  @PreAuthorize("hasRole('ROLE_EDSP')")
  @PostMapping("/api/countries/save")
  public void save(@RequestBody List<Country> countries) throws JsonProcessingException {
    CountryList countryList = new CountryList(countries);
    storage.save(
      "countries.json",
      objectMapper.writeValueAsString(countryList),
      authenticationFacade.getUserName());
  }

}
