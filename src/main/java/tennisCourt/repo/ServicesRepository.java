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
import java.sql.Time;
import java.util.List;
import java.util.Map;

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

    @Query(value = "SELECT time FROM services " +
            "WHERE date = :date", nativeQuery = true)
    List<Time> findTimeByDate(String date);

    @Query(value = "SELECT number_of_hours FROM services " +
            "WHERE date = :date", nativeQuery = true)
    List<Float> findNumberOfHoursByDate(String date);

}
