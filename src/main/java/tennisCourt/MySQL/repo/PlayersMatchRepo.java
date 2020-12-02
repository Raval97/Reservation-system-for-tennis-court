package tennisCourt.MySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tennisCourt.MySQL.model.Performance;


@Repository
public interface PlayersMatchRepo extends JpaRepository<Performance, Long> {


}
