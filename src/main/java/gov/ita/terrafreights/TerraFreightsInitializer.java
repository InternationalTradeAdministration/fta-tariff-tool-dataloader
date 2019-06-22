package gov.ita.terrafreights;

import gov.ita.terrafreights.tariff.TariffRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class TerraFreightsInitializer implements ApplicationListener<ContextRefreshedEvent> {

  @Value("${terrafreights.version}")
  private String version;

  private DataSeeder dataSeeder;
  private TariffRepository tariffRepository;

  public TerraFreightsInitializer(DataSeeder dataSeeder, TariffRepository tariffRepository) {
    this.dataSeeder = dataSeeder;
    this.tariffRepository = tariffRepository;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    printTerraFreightsAsciiArt();
    if (tariffRepository.count() == 0)
      dataSeeder.seed();
  }

  private void printTerraFreightsAsciiArt() {
    System.out.println("  _______                  ______        _       _     _             ");
    System.out.println(" |__   __|                |  ____|      (_)     | |   | |            ");
    System.out.println("    | | ___ _ __ _ __ __ _| |__ _ __ ___ _  __ _| |__ | |_ ___       ");
    System.out.println("    | |/ _ \\_'__| '__/ _` |  __| '__/ _ \\ |/ _` | '_ \\| __/ __|   ");
    System.out.println("    | |  __/_|  | | | (_| | |  | | |  __/ | (_| | | | | |_\\__ \\    ");
    System.out.println("    |_|\\___|_|  |_|  \\__,_|_|  |_|  \\___|_|\\__, |_| |_|\\__|___/ ");
    System.out.println("                                           __/ |                     ");
    System.out.println("                                          |___/                      ");
    System.out.println("-- TerraFreights v" + version + " --                                 ");
    System.out.println("                                                                     ");
  }

}
