package gov.ita.tarifftooldataloader.initializer;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Helpers {

  public static String getFileAsString(String fileName) {
    File file = getResourceAsFile("fixtures/" + fileName);

    if (file != null)
      try {
        return FileUtils.readFileToString(file, "UTF-8");
      } catch (IOException e) {
        e.printStackTrace();
      }

    return null;
  }

  public static Reader getFileAsReader(String fileName) {
    File file = getResourceAsFile("fixtures/" + fileName);

    if (file != null)
      try {
        return new StringReader(FileUtils.readFileToString(file, "UTF-8"));
      } catch (IOException e) {
        e.printStackTrace();
      }

    return null;
  }

  private static File getResourceAsFile(String path) {
    try {
      return new ClassPathResource(path).getFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
