package gov.ita.terrafreights.staging_basket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StagingBasketRepository extends JpaRepository<StagingBasket, Long> {
}
