package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.ClubAssociation;

import java.util.List;

@Repository
public interface ClubAssociationRepository extends JpaRepository<ClubAssociation, Long> {

    @Query(value = "SELECT * FROM club_association ca WHERE ca.users= :id", nativeQuery = true)
    ClubAssociation findByUserId(@Param("id") Long id);

    @Query(value = "SELECT * FROM club_association ca WHERE ca.id= :id", nativeQuery = true)
    ClubAssociation findByIdParam(Long id);

    @Query(value = "SELECT * FROM club_association Order by id", nativeQuery = true)
    List<ClubAssociation> findAllClub();
}
