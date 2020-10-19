package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tennisCourt.model.UserTournamentApplication;

import java.util.List;

@Repository
public interface UserTournamentApplicationRepository extends JpaRepository<UserTournamentApplication, Long> {

    @Query(value = "SELECT IF(status IS Null, 'without_application',status) as status FROM tournament LEFT JOIN " +
            "(SELECT * FROM (SELECT DISTINCT tournament_id, status FROM user_tournament_application utp " +
            "RIGHT JOIN tournament t on t.id=utp.tournament_id WHERE users_id = :id ORDER By utp.id DESC )X " +
            "GROUP By tournament_id)XX on tournament.id = XX.tournament_id", nativeQuery = true)
    List<String> findAllStatusTournamentToUser(Long id);

    @Query(value = "Select * FROM user_tournament_application WHERE tournament_id=:id", nativeQuery = true)
    List<UserTournamentApplication> findAllByTournamentId(Long id);

    @Query(value = "Select status FROM user_tournament_application utp where utp.tournament_id = :id", nativeQuery = true)
    List<String> findAllStatusByTournamentId(Long id);

    @Query(value = "Select count(tournament_id) FROM user_tournament_application utp where utp.tournament_id = :id", nativeQuery = true)
    int countElementsByTournamentId(Long id);

    @Query(value = "Select * FROM user_tournament_application utp " +
            "where utp.tournament_id = :tournamentId AND utp.users_id = :userId " +
            "AND status!='Rejected' AND status!='Canceled'", nativeQuery = true)
    UserTournamentApplication findByTournamentAndUserId(Long tournamentId, Long userId);

    @Modifying
    @Query(value = "Delete FROM user_tournament_application utp " +
            "where utp.tournament_id = :tournamentId AND utp.users_id = :userId AND status!='Rejected'", nativeQuery = true)
    void deleteByTournamentAndUserId(Long tournamentId, Long userId);

    @Query(value = "SELECT case when count(distinct id) > 0 then 'true' else 'false' end as bool " +
            "FROM user_tournament_application WHERE tournament_id= :tournamentId AND users_id= :userId ", nativeQuery = true)
    boolean hasUserApplicationInTournament(Long tournamentId, Long userId);
}
