package gov.ita.tarifftooldataloader.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("production")
public class ProductionSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
      .authorizeRequests()
      .anyRequest().authenticated()
      .and()
      .oauth2Login()
      .userInfoEndpoint()
      .oidcUserService(oidcUserService);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(
      "/api/tariff/log",
      "/api/tariff/download/csv",
      "/api/tariff/download/json",
      "/api/open-data-catalog");
  }
}
