package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.UserReservation;

@Repository
public interface UserReservationRepository extends JpaRepository<UserReservation, Long> {

    @Query(value = "Select * FROM user_reservation " +
            "WHERE reservation_id=:id", nativeQuery = true)
    UserReservation findByReservationId(Long id);
}
