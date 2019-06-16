package gov.ita.terrafreights;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class TerraFreightsInitializer implements ApplicationListener<ContextRefreshedEvent> {

  private DataSeeder dataSeeder;

  public TerraFreightsInitializer(DataSeeder dataSeeder) {
    this.dataSeeder = dataSeeder;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    dataSeeder.seed();
  }
}
