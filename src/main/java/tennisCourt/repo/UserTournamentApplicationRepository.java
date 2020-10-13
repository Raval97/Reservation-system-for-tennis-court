package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tennisCourt.model.UserTournamentApplication;
import java.util.List;

@Repository
public interface UserTournamentApplicationRepository extends JpaRepository<UserTournamentApplication, Long> {

    @Query(value = "SELECT IF(status IS Null, 'without_application',status) as status  from tournament " +
            "LEFT JOIN (SELECT utp.* FROM user_tournament_application utp RIGHT JOIN tournament t " +
            "on t.id=utp.tournament_id WHERE user_id = :id )X on tournament.id=X.tournament_id", nativeQuery = true)
    List<String> findAllStatusTournamentToUser(Long id);

    @Query(value = "Select * FROM user_tournament_application WHERE tournament_id=:id", nativeQuery = true)
    List<UserTournamentApplication> findAllByTournamentId(Long id);

    @Query(value = "Select status FROM user_tournament_application utp where utp.tournament_id = :id", nativeQuery = true)
    List<String> findAllStatusByTournamentId(Long id);

    @Query(value = "Select count(tournament_id) FROM user_tournament_application utp where utp.tournament_id = :id", nativeQuery = true)
    int countElementsByTournamentId(Long id);
}
