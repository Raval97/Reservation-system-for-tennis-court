package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.Client;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "SELECT * FROM client c WHERE c.users= :id", nativeQuery = true)
    Client findByIdClient(@Param("id") Long id);

    @Modifying
    @Query(value = "Delete FROM client WHERE users= :id", nativeQuery = true)
    void deleteByUserId(@Param("id") Long id);

    @Query(value = "SELECT c.* FROM client c " +
            " RIGHT JOIN membership_application ma on ma.users= c.users" +
            " WHERE ma.decision = \'waiting_for_decisions\' ORDER BY ma.id", nativeQuery = true)
    List<Client> findAllWhoHasActiveApplication();

    @Query(value = "SELECT c.* FROM client c" +
            " RIGHT JOIN club_association ca on ca.users= c.users ORDER BY ca.id", nativeQuery = true)
    List<Client> findAllWhoInClub();

    @Query(value = "SELECT * FROM client WHERE users= :id", nativeQuery = true)
    Client findBuUserId(Long id);

    @Query(value = "SELECT c.* FROM client c " +
            "LEFT JOIN user_tournament_application utp on utp.users=c.users " +
            "WHERE utp.tournament = :id", nativeQuery = true)
    List<Client> findAllByInTournamentApplicationByThemID(Long id);

    @Query(value = "SELECT * FROM client WHERE users in (SELECT users " +
            "from user_tournament WHERE tournament = :id)", nativeQuery = true)
    List<Client> findAllByInTournamentByThemID(Long id);

    @Query(value = "SELECT * FROM client Order by users", nativeQuery = true)
    List<Client> findAllClient();
}
