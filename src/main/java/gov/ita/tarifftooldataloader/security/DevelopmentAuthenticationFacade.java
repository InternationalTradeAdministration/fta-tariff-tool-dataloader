package gov.ita.tarifftooldataloader.security;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile({"development", "staging"})
@Service
public class DevelopmentAuthenticationFacade implements AuthenticationFacade {

  @Override
  public String getUserName() {
    return "TestUser@trade.gov";
  }

}
