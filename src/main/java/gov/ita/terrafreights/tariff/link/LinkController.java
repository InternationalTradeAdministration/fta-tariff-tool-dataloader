package gov.ita.terrafreights.tariff.link;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LinkController {

  private LinkRepository linkRepository;

  public LinkController(LinkRepository linkRepository) {
    this.linkRepository = linkRepository;
  }

  @GetMapping("/api/links")
  public List<Link> links() {
    return linkRepository.findAll();
  }

  @PutMapping("/api/link")
  public Link save(@RequestBody Link link) {
    return linkRepository.save(link);
  }
}
