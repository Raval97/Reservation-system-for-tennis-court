package tennisCourt.MySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tennisCourt.MySQL.model.Competition;


@Repository
public interface CompetitionRepo extends JpaRepository<Competition, Long> {


}
