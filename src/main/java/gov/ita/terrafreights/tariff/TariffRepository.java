package gov.ita.terrafreights.tariff;

import gov.ita.terrafreights.tariff.product.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
  Page<Tariff> findByCountryCode(String countryCode, Pageable pageable);

  Page<Tariff> findByCountryCodeAndProductTypeId(String countryCode, Long productTypeId, Pageable pageable);

  Page<Tariff> findByCountryCodeAndStagingBasketId(String countryCode, Long stagingBasketId, Pageable pageable);

  Page<Tariff> findByCountryCodeAndProductTypeIdAndStagingBasketId(String countryCode,
                                                                   Long productTypeId,
                                                                   Long stagingBasketId,
                                                                   Pageable pageable);

  @Query(value = "select distinct new ProductType(p.id, p.description) " +
    "from Tariff t " +
    "join ProductType p on t.productType.id = p.id " +
    "join Country c on t.country.id = c.id " +
    "where c.code = ?1")
  List<ProductType> findAllProductTypesByCountry(String countryCode);

}
