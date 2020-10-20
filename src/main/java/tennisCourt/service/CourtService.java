package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.Court;
import tennisCourt.repo.CourtRepository;

import java.util.List;

@Service
@Transactional
public class CourtService {

    private CourtRepository repo;

    @Autowired
    public CourtService(CourtRepository courtRepository) {
        this.repo = courtRepository;
    }

    public List<Court> listAll() {
        return repo.findAllCourt();
    }

    public Court get(long id) {
        return repo.findByIdCourt(id);
    }

    public void save(Court court){
        repo.save(court);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

}
