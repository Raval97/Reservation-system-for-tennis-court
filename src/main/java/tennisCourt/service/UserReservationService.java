package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.UserReservation;
import tennisCourt.repo.UserReservationRepository;

import java.util.List;

@Service
@Transactional
public class UserReservationService {

    private UserReservationRepository repo;

    @Autowired
    public UserReservationService(UserReservationRepository userReservationRepository) {
        this.repo = userReservationRepository;
    }

    public List<UserReservation> listAll() {
        return repo.findAll();
    }

    public void save(UserReservation userReservation){
        repo.save(userReservation);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public void deleteByReservationId(Long id) {
        repo.deleteByReservationId(id);
    }

}
