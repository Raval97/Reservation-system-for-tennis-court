package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.MembershipApplication;

import java.util.List;

@Repository
public interface MembershipApplicationRepository extends JpaRepository<MembershipApplication, Long> {

    @Query(value = "SELECT decision FROM membership_application" +
            " WHERE users = :id ORDER by id DESC ", nativeQuery = true)
    List<String> getDecisionByUserId(@Param("id") Long id);

    @Query(value = "SELECT * FROM membership_application " +
            " WHERE decision = 'waiting_for_decisions' GROUP by users, id", nativeQuery = true)
    List<MembershipApplication> finAllActive();

    @Query(value = "SELECT * FROM membership_application  WHERE id = :id", nativeQuery = true)
    MembershipApplication findByIdParam(@Param("id") Long id);

    @Query(value = "SELECT * FROM membership_application Order by id", nativeQuery = true)
    List<MembershipApplication> findallMemebrship();
}
