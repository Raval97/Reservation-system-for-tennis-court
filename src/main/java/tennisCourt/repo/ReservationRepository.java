package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.Reservation;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query(value = "SELECT * FROM reservation r WHERE r.id= :id", nativeQuery = true)
    Reservation findByIdReservation(@Param("id") Long id);

    @Query(value = "SELECT r.* FROM reservation r " +
            "LEFT JOIN user_reservation ur on ur.reservation_id=r.id " +
            "LEFT JOIN user u on ur.user_id=u.id WHERE u.id=:id", nativeQuery = true)
    List<Reservation> findAllByIdUser(Long id);

    @Query(value = "SELECT case when count(distinct id) > 0 then 'true' else 'false' end as bool\n" +
            "FROM `reservation` WHERE status_of_reservation=\"Started\" " +
            "AND id in (SELECT ur.reservation_id from user_reservation ur WHERE ur.user_id = :id)", nativeQuery = true)
    Boolean findIfUserHasStartedReservation(Long id);

    @Query(value = "SELECT * FROM reservation r WHERE r.status_of_reservation=\"Started\" AND r.id in " +
            "(SELECT ur.reservation_id FROM user_reservation ur WHERE ur.user_id=:id) ", nativeQuery = true)
    Reservation findStartedReservationByUserId(Long id);

}
