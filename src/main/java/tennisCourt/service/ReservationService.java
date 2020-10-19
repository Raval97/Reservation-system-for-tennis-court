package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.Reservation;
import tennisCourt.repo.ReservationRepository;

import java.time.LocalDate;
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

    public Boolean checkIfUserHasStartedReservation(Long id){
        return repo.findIfUserHasStartedReservation(id);
    }

    public Reservation getStartedReservationByUserId(Long id) {
        return  repo.findStartedReservationByUserId(id);
    }

    public void update(Long id, String status_of_reservation, String status_of_paying, String typeOfPaying, LocalDate date) {
        repo.update(id, status_of_reservation,  status_of_paying, typeOfPaying, date);
    }

    public void updatePrice(Long id, float price) {
        repo.updatePrice(id, price);
    }

    public void deleteAllByDate(LocalDate date) {
        repo.deleteAllByDate(date);
    }
}
