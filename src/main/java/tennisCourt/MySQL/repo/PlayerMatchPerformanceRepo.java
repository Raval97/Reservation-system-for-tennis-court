package tennisCourt.MySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tennisCourt.MySQL.model.PlayerTeam;


@Repository
public interface PlayerMatchPerformanceRepo extends JpaRepository<PlayerTeam, Long> {


}
