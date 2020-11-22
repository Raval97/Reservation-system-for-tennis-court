package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.ReservationServices;
import tennisCourt.repo.ReservationServicesRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ReservationServicesService {

    private ReservationServicesRepository repo;

    @Autowired
    public ReservationServicesService(ReservationServicesRepository reservationServicesRepository) {
        this.repo = reservationServicesRepository;
    }

    public List<ReservationServices> listAll() {
        return repo.findAllReservationService();
    }

    public void save(ReservationServices reservationServices){
        repo.save(reservationServices);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public List<ReservationServices> getAllByReservationId(Long id) {
        return repo.findAllByReservationId(id);
    }

    public void deleteAllByReservationId(Long id) {
        List<ReservationServices> reservationServicesList = repo.findAllByReservationId(id);
        reservationServicesList.forEach((x) -> repo.deleteById(x.getId()));
    }

    public void deleteAllByDate(LocalDate date) {
        Date d = java.sql.Date.valueOf(date);
        repo.deleteAllByDate(d);
    }

}
