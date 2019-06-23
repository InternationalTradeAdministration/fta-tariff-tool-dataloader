package gov.ita.terrafreights.tariff.hs6;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HS6Controller {

  private HS6Repository hs6Repository;

  public HS6Controller(HS6Repository hs6Repository) {
    this.hs6Repository = hs6Repository;
  }

  @GetMapping("/api/hs6s")
  public List<HS6> hs6s() {
    return hs6Repository.findAll();
  }

  @PutMapping("/api/hs6")
  public HS6 save(@RequestBody HS6 hs6) {
    return hs6Repository.save(hs6);
  }

}
