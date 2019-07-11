package gov.ita.terrafreights;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSecurityContextController {

  @GetMapping("/api/user")
  public SecurityContext currentUser() {
    return SecurityContextHolder.getContext();
  }

}
