package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.Services;
import tennisCourt.repo.ServicesRepository;

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

}
