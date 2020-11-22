package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.PriceList;
import tennisCourt.repo.PriceListRepository;

import java.util.List;

@Service
@Transactional
public class PriceListService {

    private PriceListRepository repo;

    @Autowired
    public PriceListService(PriceListRepository priceListRepository) {
        this.repo = priceListRepository;
    }

    public List<PriceList> listAll() {
        return repo.findAllPrice();
    }

    public PriceList get(long id) {
        return repo.findByIdPriceList(id);
    }

    public void save(PriceList priceList){
        repo.save(priceList);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

}
