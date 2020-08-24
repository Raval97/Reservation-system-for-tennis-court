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

}
