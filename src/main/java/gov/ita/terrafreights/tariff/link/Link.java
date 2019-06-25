package gov.ita.terrafreights.tariff.link;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Link {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  String linkUrl;
  String linkText;
}