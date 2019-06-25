package gov.ita.terrafreights.tariff;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
  Page<Tariff> findByCountryCode(String countryCode, Pageable pageable);
  Page<Tariff> findByCountryCodeAndProductTypeId(String countryCode, Long productTypeId, Pageable pageable);
  Page<Tariff> findByCountryCodeAndStagingBasketId(String countryCode, Long stagingBasketId, Pageable pageable);
  Page<Tariff> findByCountryCodeAndProductTypeIdAndStagingBasketId(String countryCode,
                                                                   Long productTypeId,
                                                                   Long stagingBasketId,
                                                                   Pageable pageable);
}
