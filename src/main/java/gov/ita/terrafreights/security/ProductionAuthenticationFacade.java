package gov.ita.terrafreights.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Profile("production")
public class ProductionAuthenticationFacade implements AuthenticationFacade {
  @Override
  public String getUserName() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}

