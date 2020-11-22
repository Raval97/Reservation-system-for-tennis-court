package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.Services;
import tennisCourt.repo.ServicesRepository;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
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
        return repo.findAllService();
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

    public List<Time> getReservedTimeByDate(String date) throws ParseException{
        Date d =  new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return repo.findReservedTimeByDate(d);
    };

    public List<Float> getReservedNumberOfHoursByDate(String date) throws ParseException{
        Date d =  new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return repo.findReservedNumberOfHoursByDate(d);
    };

    public List<Long> getReservedCourtIdByDate(String date) throws ParseException {
        Date d =  new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return repo.findReservedCourtIdByDate(d);
    };

    public List<Time> getStartedTimeByDate(String date, Long id) throws ParseException{
        Date d =  new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return repo.findStartedTimeByDate(d, id);
    };

    public List<Float> getStartedNumberOfHoursByDate(String date, Long id) throws ParseException{
        Date d =  new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return repo.findStartedNumberOfHoursByDate(d, id);
    };

    public List<Long> getStartedCourtIdByDate(String date, Long id) throws ParseException{
        Date d =  new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return repo.findStartedCourtIdByDate(d, id);
    };

    public List<Services> getInStartedReservationByUserId(Long id) {
        return repo.findInStartedReservationByUserId(id);
    }

    public void updateIfBalls(Long id, Boolean ifBalls) {
        repo.updateIfBalls(id, ifBalls);
    }

    public void updateIfRocket(Long id, Boolean ifRocket) {
        repo.updateIfRocket(id,ifRocket);
    }

    public void updateIfShoes(Long id, Boolean ifShoes) {
        repo.updateIfShoes(id, ifShoes);
    }

    public void updatePrice(Long id, float price) {
        repo.updatePrice(id, price);
    }

    public void deleteAllByDate(LocalDate date) {
        Date d = java.sql.Date.valueOf(date);
        repo.deleteAllByDate(d);
    }
}
