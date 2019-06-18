package gov.ita.terrafreights.initializer;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

public class Helpers {
  public static String getFileAsString(String fileName) {
    String path = "fixtures/" + fileName;
    File file = null;
    try {
      file = new ClassPathResource(path).getFile();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String fileString = null;
    try {
      fileString = FileUtils.readFileToString(file, "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }

    return fileString;
  }
}
