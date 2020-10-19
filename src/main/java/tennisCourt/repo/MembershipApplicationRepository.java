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
            " WHERE users_id = :id ORDER by id DESC ", nativeQuery = true)
    List<String> getDecisionByUserId(@Param("id") Long id);

    @Query(value = "SELECT * FROM membership_application ma" +
            " WHERE decision = \"waiting_for_decisions\" GROUP by ma.users_id", nativeQuery = true)
    List<MembershipApplication> finAllActive();

    @Query(value = "SELECT * FROM membership_application  WHERE id = :id", nativeQuery = true)
    MembershipApplication findByIdParam(@Param("id") Long id);
}
