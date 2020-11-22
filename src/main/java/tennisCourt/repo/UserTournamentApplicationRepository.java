package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tennisCourt.model.UserTournamentApplication;

import java.util.List;

@Repository
public interface UserTournamentApplicationRepository extends JpaRepository<UserTournamentApplication, Long> {

    @Query(value = "SELECT CASE WHEN status IS NULL THEN 'without_application' ELSE status END AS status from ( " +
            "SELECT status, tournament from user_tournament_application where id in ( Select X.max from ( " +
            "SELECT tournament, max(utp.id) FROM user_tournament_application utp RIGHT JOIN tournament t " +
            "on t.id = utp.tournament  WHERE users = :id GROUP BY tournament)X ))XX right join tournament utp " +
            "on XX.tournament = utp.id;", nativeQuery = true)
    List<String> findAllStatusTournamentToUser(Long id);

    @Query(value = "Select * FROM user_tournament_application WHERE tournament=:id", nativeQuery = true)
    List<UserTournamentApplication> findAllByTournamentId(Long id);

    @Query(value = "Select status FROM user_tournament_application utp where utp.tournament = :id", nativeQuery = true)
    List<String> findAllStatusByTournamentId(Long id);

    @Query(value = "Select count(tournament) FROM user_tournament_application utp where utp.tournament = :id", nativeQuery = true)
    int countElementsByTournamentId(Long id);

    @Query(value = "Select * FROM user_tournament_application utp " +
            "where utp.tournament = :tournamentId AND utp.users = :userId " +
            "AND status!='Rejected' AND status!='Canceled'", nativeQuery = true)
    UserTournamentApplication findByTournamentAndUserId(Long tournamentId, Long userId);

    @Modifying
    @Query(value = "Delete FROM user_tournament_application utp " +
            "where utp.tournament = :tournamentId AND utp.users = :userId AND status!='Rejected'", nativeQuery = true)
    void deleteByTournamentAndUserId(Long tournamentId, Long userId);

    @Query(value = "SELECT case when count(distinct id) > 0 then 'true' else 'false' end as bool " +
            "FROM user_tournament_application WHERE tournament= :tournamentId AND users= :userId ", nativeQuery = true)
    boolean hasUserApplicationInTournament(Long tournamentId, Long userId);

    @Query(value = "SELECT * FROM user_tournament_application Order by id", nativeQuery = true)
    List<UserTournamentApplication> findAllApplication();
}
