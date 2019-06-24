package gov.ita.terrafreights.tariff;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
  Page<Tariff> findByCountryCode(String country, Pageable pageable);
  Page<Tariff> findByCountryCodeAndProductTypeId(String country, Long ProductTypeId, Pageable pageable);
}
