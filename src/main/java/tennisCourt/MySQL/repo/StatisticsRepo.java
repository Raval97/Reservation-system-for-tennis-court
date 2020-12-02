package tennisCourt.MySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tennisCourt.MySQL.model.Statistics;
import tennisCourt.MySQL.model.Team;


@Repository
public interface StatisticsRepo extends JpaRepository<Statistics, Long> {


}
