package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.Tournament;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    @Query(value = "SELECT * FROM tournament  WHERE title= :title", nativeQuery = true)
    Tournament findByTitle(@Param("title") String title);
}
