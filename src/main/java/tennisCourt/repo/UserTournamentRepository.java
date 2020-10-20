package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tennisCourt.model.UserTournament;

import java.util.List;

@Repository
public interface UserTournamentRepository extends JpaRepository<UserTournament, Long> {

    @Query(value = "Select * FROM user_tournament WHERE tournament=:id", nativeQuery = true)
    List<UserTournament> findAllByTournamentId(Long id);

    @Query(value = "Select count(tournament) FROM user_tournament WHERE tournament=:id", nativeQuery = true)
    int countElementsByTournamentId(Long id);

    @Modifying
    @Query(value = "Delete FROM user_tournament  " +
            "where tournament = :tournamentId AND users = :userId", nativeQuery = true)
    void deleteByTournamentAndUserId(Long tournamentId, Long userId);

    @Query(value = "SELECT * FROM user_tournament Order by id", nativeQuery = true)
    List<UserTournament> findAllParticipant();
}
