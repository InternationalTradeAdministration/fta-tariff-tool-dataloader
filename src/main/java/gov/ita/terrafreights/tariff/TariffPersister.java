package gov.ita.terrafreights.tariff;

import gov.ita.terrafreights.tariff.country.Country;
import gov.ita.terrafreights.tariff.country.CountryRepository;
import gov.ita.terrafreights.tariff.hs6.HS6;
import gov.ita.terrafreights.tariff.hs6.HS6Repository;
import gov.ita.terrafreights.tariff.link.Link;
import gov.ita.terrafreights.tariff.link.LinkRepository;
import gov.ita.terrafreights.tariff.product.ProductType;
import gov.ita.terrafreights.tariff.product.ProductTypeRepository;
import gov.ita.terrafreights.tariff.rate.Rate;
import gov.ita.terrafreights.tariff.rate.RateRepository;
import gov.ita.terrafreights.tariff.stagingbasket.StagingBasket;
import gov.ita.terrafreights.tariff.stagingbasket.StagingBasketRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TariffPersister {
  private final TariffRepository tariffRepository;
  private final HS6Repository hs6Repository;
  private final ProductTypeRepository productTypeRepository;
  private final StagingBasketRepository stagingBasketRepository;
  private final RateRepository rateRepository;
  private final CountryRepository countryRepository;
  private final LinkRepository linkRepository;

  public TariffPersister(TariffRepository tariffRepository,
                         HS6Repository hs6Repository,
                         ProductTypeRepository productTypeRepository,
                         StagingBasketRepository stagingBasketRepository,
                         RateRepository rateRepository,
                         CountryRepository countryRepository,
                         LinkRepository linkRepository) {
    this.tariffRepository = tariffRepository;
    this.hs6Repository = hs6Repository;
    this.productTypeRepository = productTypeRepository;
    this.stagingBasketRepository = stagingBasketRepository;
    this.rateRepository = rateRepository;
    this.countryRepository = countryRepository;
    this.linkRepository = linkRepository;
  }

  public void persist(List<Tariff> tariffs) {
    Map<String, ProductType> existingProductTypes = getExistingProductTypes();
    Map<String, StagingBasket> existingStagingBaskets = getExistingStagingBaskets();
    Map<String, Country> existingCountries = getExistingCountries();

    Set<HS6> hs6s = new HashSet<>();
    List<Rate> rates = new ArrayList<>();
    List<Link> links = new ArrayList<>();

    for (Tariff t : tariffs) {
      setProductType(existingProductTypes, t);
      setStagingBasket(existingStagingBaskets, t);
      t.setCountry(existingCountries.get(t.getCountry().getCode()));
      hs6s.add(t.getHs6());
      rates.addAll(t.getRates());
      links.addAll(t.getLinks());
    }

    hs6Repository.saveAll(hs6s);
    rateRepository.saveAll(rates);
    linkRepository.saveAll(links);
    tariffRepository.saveAll(tariffs);
  }

  private void setStagingBasket(Map<String, StagingBasket> existingStagingBaskets, Tariff t) {
    StagingBasket stagingBasket = existingStagingBaskets.get(t.getStagingBasket().getDescription());
    if (stagingBasket != null) {
      t.setStagingBasket(stagingBasket);
    } else {
      StagingBasket persistedStagingBasket = stagingBasketRepository.save(t.getStagingBasket());
      existingStagingBaskets.put(persistedStagingBasket.getDescription(), persistedStagingBasket);
    }
  }

  private void setProductType(Map<String, ProductType> existingProductTypes, Tariff t) {
    ProductType productType = existingProductTypes.get(t.getProductType().getDescription());
    if (productType != null) {
      t.setProductType(productType);
    } else {
      ProductType persistedProductType = productTypeRepository.save(t.getProductType());
      existingProductTypes.put(persistedProductType.getDescription(), persistedProductType);
    }
  }

  private Map<String, Country> getExistingCountries() {
    return countryRepository
      .findAll()
      .stream()
      .collect(Collectors.toMap(Country::getCode, Function.identity()));
  }

  private Map<String, StagingBasket> getExistingStagingBaskets() {
    return stagingBasketRepository
      .findAll()
      .stream()
      .collect(Collectors.toMap(StagingBasket::getDescription, Function.identity()));
  }

  private Map<String, ProductType> getExistingProductTypes() {
    return productTypeRepository
      .findAll()
      .stream()
      .collect(Collectors.toMap(ProductType::getDescription, Function.identity()));
  }

}
