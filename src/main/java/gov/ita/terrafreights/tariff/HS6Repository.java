package gov.ita.terrafreights.tariff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HS6Repository extends JpaRepository<HS6, Long> {
}
