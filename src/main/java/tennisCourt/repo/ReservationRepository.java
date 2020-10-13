package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.Reservation;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query(value = "SELECT * FROM reservation r WHERE r.id= :id", nativeQuery = true)
    Reservation findByIdReservation(@Param("id") Long id);

    @Query(value = "SELECT r.* FROM reservation r " +
            "LEFT JOIN user_reservation ur on ur.reservation_id=r.id " +
            "LEFT JOIN user u on ur.user_id=u.id WHERE u.id=:id AND r.status_of_reservation = \"Reserved\" " +
            "AND r.by_admin = false", nativeQuery = true)
    List<Reservation> findAllByIdUser(Long id);

    @Query(value = "SELECT case when count(distinct id) > 0 then 'true' else 'false' end as bool\n" +
            "FROM `reservation` WHERE status_of_reservation=\"Started\" " +
            "AND id in (SELECT ur.reservation_id from user_reservation ur WHERE ur.user_id = :id)", nativeQuery = true)
    Boolean findIfUserHasStartedReservation(Long id);

    @Query(value = "SELECT * FROM reservation r WHERE r.status_of_reservation=\"Started\" AND r.id in " +
            "(SELECT ur.reservation_id FROM user_reservation ur WHERE ur.user_id=:id) ", nativeQuery = true)
    Reservation findStartedReservationByUserId(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE reservation SET status_of_reservation = :status_of_reservation, " +
            "status_paying = :status_paying, type_of_paying = :type_of_paying, " +
            "date_of_reservation= :date_of_reservation WHERE reservation.id = :id", nativeQuery = true)
    void update (@Param("id")Long id, @Param("status_of_reservation")String status_of_reservation,
                 @Param("status_paying")String status_paying, @Param("type_of_paying")String typeOfPaying,
                 @Param("date_of_reservation") LocalDate date_of_reservation);

    @Transactional
    @Modifying
    @Query(value = "UPDATE reservation SET price = :price WHERE reservation.id = :id", nativeQuery = true)
    void updatePrice (@Param("id")Long id, @Param("price")float price);


}
