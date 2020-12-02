package tennisCourt.MySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tennisCourt.MySQL.model.EffectiveStatistics;
import tennisCourt.MySQL.model.Statistics;


@Repository
public interface EffectiveStatisticsRepo extends JpaRepository<EffectiveStatistics, Long> {


}
