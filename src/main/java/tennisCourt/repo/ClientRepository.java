package tennisCourt.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.Client;

import javax.transaction.Transactional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "SELECT * FROM client c WHERE c.users= :id", nativeQuery = true)
    Client findByIdClient(@Param("id") Long id);

    @Modifying
    @Query(value = "Delete FROM client WHERE users= :id", nativeQuery = true)
    void deleteByUserId(@Param("id") Long id);

}
