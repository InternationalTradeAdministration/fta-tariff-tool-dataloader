package gov.ita.tarifftooldataloader.tariff;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Link {

  String linkUrl;
  String linkText;

  public Link(String linkUrl, String linkText) {
    this.linkUrl = linkUrl;
    this.linkText = linkText;
  }

}
