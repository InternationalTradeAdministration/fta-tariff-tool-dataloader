package gov.ita.terrafreights.security;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("development")
@Service
public class DevelopmentAuthenticationFacade implements AuthenticationFacade {
  @Override
  public String getUserName() {
    return "Test User";
  }
}
