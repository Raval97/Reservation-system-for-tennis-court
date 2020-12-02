package tennisCourt.MySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tennisCourt.MySQL.model.Player;

@Repository
public interface PersonalDataRepo extends JpaRepository<Player, Long> {


}
