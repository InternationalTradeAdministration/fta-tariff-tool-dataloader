package gov.ita.terrafreights.tariff;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class TariffController {

  private final RestTemplate restTemplate;

  public TariffController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping("/api/tariffs/all")
  public String tariffs(Pageable pageable, @RequestParam("countryCode") String countryCode) {
    return "future csv string";
  }

  @PreAuthorize("hasRole('ROLE_EDSP')")
  @PutMapping("/api/tariffs/save")
  public String saveTariffs(@RequestParam("countryCode") String countryCode,
                            @RequestBody TariffUpload tariffUpload) {
      return "success";
  }
}
