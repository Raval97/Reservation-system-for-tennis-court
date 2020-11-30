package tennisCourt.MySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tennisCourt.MySQL.model.PlayersMatch;


@Repository
public interface PlayersMatchRepo extends JpaRepository<PlayersMatch, Long> {


}
