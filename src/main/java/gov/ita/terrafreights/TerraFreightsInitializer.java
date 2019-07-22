package gov.ita.terrafreights;

import gov.ita.terrafreights.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TerraFreightsInitializer implements ApplicationListener<ContextRefreshedEvent> {

  @Value("${terrafreights.version}")
  private String version;

  private Storage storage;

  public TerraFreightsInitializer(Storage storage) {
    this.storage = storage;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    printTerraFreightsAsciiArt();
    if (!storage.containerExists()) {
      log.info("Initializing storage");
      storage.createContainer();
    }
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
    System.out.println("-- TariffTool v" + version + " --                                 ");
    System.out.println("                                                                     ");
  }

}
