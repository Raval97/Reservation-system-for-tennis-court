package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.UserReservation;

import java.time.LocalDate;

@Repository
public interface UserReservationRepository extends JpaRepository<UserReservation, Long> {

    @Query(value = "Select * FROM user_reservation " +
            "WHERE reservation=:id", nativeQuery = true)
    UserReservation findByReservationId(Long id);

    @Modifying
    @Query(value = "DELETE FROM user_reservation WHERE reservation_id in " +
            "(SELECT id from reservation WHERE id in (SELECT reservation_id from reservation_services " +
            "WHERE services_id in (SELECT id from services WHERE date= :date)))", nativeQuery = true)
    void deleteAllByDate(@Param("date") LocalDate date);
}
