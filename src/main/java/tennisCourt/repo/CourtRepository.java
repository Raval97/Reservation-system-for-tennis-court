package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.Court;

import java.util.List;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    @Query(value = "SELECT * FROM court c WHERE c.id= :id", nativeQuery = true)
    Court findByIdCourt(@Param("id") Long id);

    @Query(value = "SELECT * FROM court c Order by id", nativeQuery = true)
    List<Court> findAllCourt();
}
