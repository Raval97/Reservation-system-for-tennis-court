package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.Services;
import tennisCourt.repo.ServicesRepository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Time> getTimeByDate(String date){
        return repo.findTimeByDate(date);
    };

    public List<Float> getNumberOfHoursByDate(String date){
        return repo.findNumberOfHoursByDate(date);
    };

    public List<Long> getCourtIdByDate(String date){
        return repo.findCourtIdByDate(date);
    };

}
