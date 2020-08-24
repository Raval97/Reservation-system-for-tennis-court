package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.Reservation;
import tennisCourt.model.Services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {

    @Query(value = "SELECT * FROM services s WHERE s.id= :id", nativeQuery = true)
    Services findByIdCServices(@Param("id") Long id);

    @Query(value = "SELECT s.* FROM services s " +
            "LEFT JOIN reservation_services rs on rs.services_id=s.id " +
            "WHERE rs.reservation_id=:id", nativeQuery = true)
    List<Services> findAllByReservationId(Long id);

    @Modifying
    @Query(value = "DELETE FROM services " +
            "WHERE id in (SELECT services_id from reservation_services rs " +
            "WHERE rs.reservation_id=:id)", nativeQuery = true)
    void deleteByReservationId(Long id);

}
