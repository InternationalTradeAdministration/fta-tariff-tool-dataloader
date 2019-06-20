package gov.ita.terrafreights;

import gov.ita.terrafreights.product.ProductType;
import gov.ita.terrafreights.product.ProductTypeRepository;
import gov.ita.terrafreights.stagingbasket.StagingBasket;
import gov.ita.terrafreights.stagingbasket.StagingBasketRepository;
import gov.ita.terrafreights.tariff.*;
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

  public TariffPersister(TariffRepository tariffRepository,
                         HS6Repository hs6Repository,
                         ProductTypeRepository productTypeRepository,
                         StagingBasketRepository stagingBasketRepository,
                         RateRepository rateRepository) {
    this.tariffRepository = tariffRepository;
    this.hs6Repository = hs6Repository;
    this.productTypeRepository = productTypeRepository;
    this.stagingBasketRepository = stagingBasketRepository;
    this.rateRepository = rateRepository;
  }

  public void persist(List<Tariff> tariffs) {
    Map<String, ProductType> existingProductTypes =
      productTypeRepository
        .findAll()
        .stream()
        .collect(Collectors.toMap(ProductType::getDescription, Function.identity()));
    Map<String, StagingBasket> existingStagingBaskets =
      stagingBasketRepository
        .findAll()
        .stream()
        .collect(Collectors.toMap(StagingBasket::getDescription, Function.identity()));

    Set<HS6> hs6s = new HashSet<>();
    List<Rate> rates = new ArrayList<>();

    for (Tariff t : tariffs) {
      ProductType productType = existingProductTypes.get(t.getProductType().getDescription());
      if (productType != null) {
        t.setProductType(productType);
      } else {
        ProductType persistedProductType = productTypeRepository.save(t.getProductType());
        existingProductTypes.put(persistedProductType.getDescription(), persistedProductType);
      }

      StagingBasket stagingBasket = existingStagingBaskets.get(t.getStagingBasket().getDescription());
      if (stagingBasket != null) {
        t.setStagingBasket(stagingBasket);
      } else {
        StagingBasket persistedStagingBasket = stagingBasketRepository.save(t.getStagingBasket());
        existingStagingBaskets.put(persistedStagingBasket.getDescription(), persistedStagingBasket);
      }

      hs6s.add(t.getHs6());
      rates.addAll(t.getRates());
    }

    hs6Repository.saveAll(hs6s);
    rateRepository.saveAll(rates);
    tariffRepository.saveAll(tariffs);
  }

}
