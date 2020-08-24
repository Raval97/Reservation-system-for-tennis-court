package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.Reservation;
import tennisCourt.repo.ReservationRepository;

import java.util.List;

@Service
@Transactional
public class ReservationService {

    private ReservationRepository repo;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.repo = reservationRepository;
    }

    public List<Reservation> listAll() {
        return repo.findAll();
    }

    public Reservation get(long id) {
        return repo.findByIdReservation(id);
    }

    public void save(Reservation reservation){
        repo.save(reservation);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public List<Reservation> listAllByUserId(Long id){
        return repo.findAllByIdUser(id);
    }

}
