package gov.ita.terrafreights.initializer;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Helpers {
  public static Reader getFileAsReader(String fileName) {
    String path = "fixtures/" + fileName;
    File file = null;
    try {
      file = new ClassPathResource(path).getFile();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if(file != null)
      try {
        return new StringReader(FileUtils.readFileToString(file, "UTF-8"));
      } catch (IOException e) {
        e.printStackTrace();
      }

    return null;
  }
}
