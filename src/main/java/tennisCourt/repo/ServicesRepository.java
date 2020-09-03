package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.Reservation;
import tennisCourt.model.Services;

import javax.transaction.Transactional;
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

    @Query(value = "SELECT time FROM services WHERE date = :date AND id in " +
            "(SELECT rs.services_id FROM reservation_services rs WHERE rs.reservation_id in " +
            "(SELECT r.id FROM reservation r WHERE r.status_of_reservation =\"Reserved\"))", nativeQuery = true)
    List<Time> findReservedTimeByDate(String date);

    @Query(value = "SELECT number_of_hours FROM services WHERE date = :date AND id in " +
            "(SELECT rs.services_id FROM reservation_services rs WHERE rs.reservation_id in " +
            "(SELECT r.id FROM reservation r WHERE r.status_of_reservation =\"Reserved\"))", nativeQuery = true)
    List<Float> findReservedNumberOfHoursByDate(String date);

    @Query(value = "SELECT court_id FROM services WHERE date = :date AND id in " +
            "(SELECT rs.services_id FROM reservation_services rs WHERE rs.reservation_id in " +
            "(SELECT r.id FROM reservation r WHERE r.status_of_reservation =\"Reserved\"))", nativeQuery = true)
    List<Long> findReservedCourtIdByDate(String date);

    @Query(value = "SELECT time FROM services WHERE date = :date AND id in " +
            "(SELECT rs.services_id FROM reservation_services rs WHERE rs.reservation_id in " +
            "(SELECT r.id FROM reservation r WHERE r.status_of_reservation =\"Started\" " +
            "AND r.id in (SELECT ur.reservation_id FROM user_reservation ur WHERE ur.user_id=:id)))", nativeQuery = true)
    List<Time> findStartedTimeByDate(String date, Long id);

    @Query(value = "SELECT number_of_hours FROM services WHERE date = :date AND id in " +
            "(SELECT rs.services_id FROM reservation_services rs WHERE rs.reservation_id in " +
            "(SELECT r.id FROM reservation r WHERE r.status_of_reservation =\"Started\" " +
            "AND r.id in (SELECT ur.reservation_id FROM user_reservation ur WHERE ur.user_id=:id)))", nativeQuery = true)
    List<Float> findStartedNumberOfHoursByDate(String date, Long id);

    @Query(value = "SELECT court_id FROM services WHERE date = :date AND id in " +
            "(SELECT rs.services_id FROM reservation_services rs WHERE rs.reservation_id in " +
            "(SELECT r.id FROM reservation r WHERE r.status_of_reservation =\"Started\" " +
            "AND r.id in (SELECT ur.reservation_id FROM user_reservation ur WHERE ur.user_id=:id)))", nativeQuery = true)
    List<Long> findStartedCourtIdByDate(String date, Long id);

    @Query(value = "SELECT * FROM services WHERE id in " +
            "(SELECT rs.services_id FROM reservation_services rs WHERE rs.reservation_id in " +
            "(SELECT r.id FROM reservation r WHERE r.status_of_reservation =\"Started\" " +
            "AND r.id in (SELECT ur.reservation_id FROM user_reservation ur WHERE ur.user_id=:id)))", nativeQuery = true)
    List<Services> findInStartedReservationByUserId(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE services SET if_balls = :if_balls WHERE services.id = :id", nativeQuery = true)
    void updateIfBalls (@Param("id")Long id, @Param("if_balls")Boolean if_balls);

    @Transactional
    @Modifying
    @Query(value = "UPDATE services SET if_rocket = :if_rocket WHERE services.id = :id", nativeQuery = true)
    void updateIfRocket (@Param("id")Long id, @Param("if_rocket")Boolean if_balls);

    @Transactional
    @Modifying
    @Query(value = "UPDATE services SET if_shoes = :if_shoes WHERE services.id = :id", nativeQuery = true)
    void updateIfShoes (@Param("id")Long id, @Param("if_shoes")Boolean if_balls);

    @Transactional
    @Modifying
    @Query(value = "UPDATE services SET price = :price WHERE services.id = :id", nativeQuery = true)
    void updatePrice (@Param("id")Long id, @Param("price")float price);
}
