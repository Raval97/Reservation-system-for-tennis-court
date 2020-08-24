package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.ReservationServices;
import tennisCourt.repo.ReservationServicesRepository;

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
        return repo.findAll();
    }

    public void save(ReservationServices reservationServices){
        repo.save(reservationServices);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public void deleteAllByReservationId(Long id) {
        repo.deleteByReservationId(id);
    }

}
