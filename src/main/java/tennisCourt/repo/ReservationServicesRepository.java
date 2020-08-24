package tennisCourt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.ReservationServices;
import tennisCourt.model.Services;

import java.util.List;

@Repository
public interface ReservationServicesRepository extends JpaRepository<ReservationServices, Long> {

    @Modifying
    @Query(value = "DELETE FROM reservation_services " +
            "WHERE reservation_id=:id", nativeQuery = true)
    void deleteByReservationId(Long id);
}
