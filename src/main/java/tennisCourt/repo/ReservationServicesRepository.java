package tennisCourt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.ReservationServices;
import tennisCourt.model.Services;
import tennisCourt.model.UserReservation;

import java.util.List;

@Repository
public interface ReservationServicesRepository extends JpaRepository<ReservationServices, Long> {

    @Query(value = "Select * FROM reservation_services " +
            "WHERE reservation=:id", nativeQuery = true)
    List<ReservationServices>  findAllByReservationId(Long id);
}
