package tennisCourt.MySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tennisCourt.MySQL.model.Competition;
import tennisCourt.MySQL.model.CompetitionTeam;


@Repository
public interface CompetitionTeamRepo extends JpaRepository<CompetitionTeam, Long> {


}
