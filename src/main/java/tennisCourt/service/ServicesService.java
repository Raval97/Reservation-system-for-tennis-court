package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.Services;
import tennisCourt.repo.ServicesRepository;

import java.sql.Time;
import java.util.List;

@Service
@Transactional
public class ServicesService {

    private ServicesRepository repo;

    @Autowired
    public ServicesService(ServicesRepository servicesRepository) {
        this.repo = servicesRepository;
    }

    public List<Services> listAll() {
        return repo.findAll();
    }

    public Services get(long id) {
        return repo.findByIdCServices(id);
    }

    public void save(Services services){
        repo.save(services);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public void deleteAllByReservationId(Long id) {
        repo.deleteByReservationId(id);
    }

    public List<Services> listAllByReservationId(Long id){
        return repo.findAllByReservationId(id);
    }

    public List<Time> getReservedTimeByDate(String date){
        return repo.findReservedTimeByDate(date);
    };

    public List<Float> getReservedNumberOfHoursByDate(String date){
        return repo.findReservedNumberOfHoursByDate(date);
    };

    public List<Long> getReservedCourtIdByDate(String date){
        return repo.findReservedCourtIdByDate(date);
    };

    public List<Time> getStartedTimeByDate(String date, Long id){
        return repo.findStartedTimeByDate(date, id);
    };

    public List<Float> getStartedNumberOfHoursByDate(String date, Long id){
        return repo.findStartedNumberOfHoursByDate(date, id);
    };

    public List<Long> getStartedCourtIdByDate(String date, Long id){
        return repo.findStartedCourtIdByDate(date, id);
    };

}
