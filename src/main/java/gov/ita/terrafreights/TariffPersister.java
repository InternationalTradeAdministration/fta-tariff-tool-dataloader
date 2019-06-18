package gov.ita.terrafreights;

import gov.ita.terrafreights.country.CountryRepository;
import gov.ita.terrafreights.tariff.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TariffPersister {

  private final TariffRepository tariffRepository;
  private final HS6Repository hs6Repository;
  private final ProductTypeRepository productTypeRepository;
  private final StagingBasketRepository stagingBasketRepository;
  private final RateRepository rateRepository;
  private final CountryRepository countryRepository;

  public TariffPersister(
    TariffRepository tariffRepository,
    HS6Repository hs6Repository,
    ProductTypeRepository productTypeRepository,
    StagingBasketRepository stagingBasketRepository,
    RateRepository rateRepository,
    CountryRepository countryRepository) {
    this.tariffRepository = tariffRepository;
    this.hs6Repository = hs6Repository;
    this.productTypeRepository = productTypeRepository;
    this.stagingBasketRepository = stagingBasketRepository;
    this.rateRepository = rateRepository;
    this.countryRepository = countryRepository;
  }

  public void persist(List<Tariff> tariffs) {
    Set<HS6> hs6s = new HashSet<>();
    Set<ProductType> productTypes = new HashSet<>();
    Set<StagingBasket> stagingBaskets = new HashSet<>();
    List<Rate> rates = new ArrayList<>();

    for (Tariff t : tariffs) {
      hs6s.add(t.getHs6());
      productTypes.add(t.getProductType());
      stagingBaskets.add(t.getStagingBasket());
      rates.addAll(t.getRates());
    }

    hs6Repository.saveAll(hs6s);
    productTypeRepository.saveAll(productTypes);
    stagingBasketRepository.saveAll(stagingBaskets);
    rateRepository.saveAll(rates);
    tariffRepository.saveAll(tariffs);
  }
}
