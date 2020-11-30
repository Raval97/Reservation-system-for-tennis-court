package tennisCourt.MySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tennisCourt.MySQL.model.Team;


@Repository
public interface TeamRepo extends JpaRepository<Team, Long> {


}
