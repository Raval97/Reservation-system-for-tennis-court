package tennisCourt.MySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tennisCourt.MySQL.model.MatchCompetitions;


@Repository
public interface MatchCompetitionsRepo extends JpaRepository<MatchCompetitions, Long> {


}
