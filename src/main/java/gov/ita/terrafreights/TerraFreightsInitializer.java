package gov.ita.terrafreights;

import gov.ita.terrafreights.tariff.TariffRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class TerraFreightsInitializer implements ApplicationListener<ContextRefreshedEvent> {

  private DataSeeder dataSeeder;
  private TariffRepository tariffRepository;

  public TerraFreightsInitializer(DataSeeder dataSeeder, TariffRepository tariffRepository) {
    this.dataSeeder = dataSeeder;
    this.tariffRepository = tariffRepository;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (tariffRepository.count() == 0)
      dataSeeder.seed();
  }
}
