package gov.ita.terrafreights.tariff;

import gov.ita.terrafreights.tariff.stagingbasket.StagingBasket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
  List<Tariff> findByCountryCode(String countryCode);

  Page<Tariff> findByCountryCode(String countryCode, Pageable pageable);

  Page<Tariff> findByCountryCodeAndStagingBasketId(String countryCode, Long stagingBasketId, Pageable pageable);

  Page<Tariff> findByCountryCodeAndStagingBasketIdAndTariffLineContaining(String countryCode,
                                                                          Long stagingBasketId,
                                                                          String tariffLine,
                                                                          Pageable pageable);

  Page<Tariff> findByCountryCodeAndTariffLineContaining(String countryCode, String tariffLine, Pageable pageable);

  @Query("select distinct new StagingBasket(s.id, s.description) " +
    "from Tariff t " +
    "join StagingBasket s on t.stagingBasket.id = s.id " +
    "join Country c on t.country.id = c.id " +
    "where c.code = ?1")
  List<StagingBasket> findAllStagingBasketsByCountry(String countryCode);

  @Query("select new gov.ita.terrafreights.tariff.TariffCount(c.code, COUNT(t) as total) " +
    "from Tariff t join Country c on t.country.id = c.id group by c.id")
  List<TariffCount> tariffCountsByCountry();

  @Modifying
  @Transactional
  @Query("delete from Tariff t where t.country.id = (select id from Country c where c.code = ?1)")
  void deleteByCountry(String countryCode);

  @Modifying
  @Transactional
  @Query(value = "delete from rate where id not in (select rates_id from tariff_rates);", nativeQuery = true)
  void deleteOrphanedRates();

  @Modifying
  @Transactional
  @Query(value = "delete from link where id not in (select links_id from tariff_links);", nativeQuery = true)
  void deleteOrphanedLinks();

  default void deleteOrphans() {
    deleteOrphanedRates();
    deleteOrphanedLinks();
  }
}
