package tennisCourt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.Payment;
import tennisCourt.model.PriceList;
import tennisCourt.model.Reservation;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "SELECT * FROM payment p WHERE p.user_id=:id", nativeQuery = true)
    List<Payment> findAlByUserId(Long id);

    @Query(value = "SELECT * FROM payment  where title = :title AND user_id = :id AND status_paying='To Pay'", nativeQuery = true)
    Payment findByTitleAndUser(String title, Long id);

    @Modifying
    @Query(value = "Delete FROM FROM payment  where title = :title AND user_id = :id AND status_paying='To Pay'", nativeQuery = true)
    void deleteByTitleAndUser(String title, Long id);
}
