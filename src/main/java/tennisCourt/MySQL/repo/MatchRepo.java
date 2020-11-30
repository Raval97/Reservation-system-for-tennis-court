package tennisCourt.MySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tennisCourt.MySQL.model.Matches;


@Repository
public interface MatchRepo extends JpaRepository<Matches, Long> {


}
