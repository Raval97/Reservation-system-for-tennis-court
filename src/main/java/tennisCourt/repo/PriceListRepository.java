package tennisCourt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tennisCourt.model.PriceList;
import java.util.List;

@Repository
public interface PriceListRepository extends JpaRepository<PriceList, Long> {

    @Query(value = "SELECT * FROM price_list pl WHERE pl.id= :id", nativeQuery = true)
    PriceList findByIdPriceList(@Param("id") Long id);

}
