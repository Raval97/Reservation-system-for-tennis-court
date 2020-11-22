package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.UserReservation;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface UserReservationRepository extends JpaRepository<UserReservation, Long> {

    @Query(value = "Select * FROM user_reservation " +
            "WHERE reservation=:id", nativeQuery = true)
    UserReservation findByReservationId(Long id);

    @Modifying
    @Query(value = "DELETE FROM user_reservation WHERE reservation in " +
            "(SELECT id from reservation WHERE id in (SELECT reservation from reservation_services " +
            "WHERE services in (SELECT id from services WHERE date= :date)))", nativeQuery = true)
    void deleteAllByDate(@Temporal Date date);

    @Query(value = "SELECT * FROM user_reservation Order by id", nativeQuery = true)
    List<UserReservation> findAllUserReservation();
}
