package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.Payment;
import tennisCourt.model.PriceList;
import tennisCourt.repo.PaymentRepository;
import tennisCourt.repo.PriceListRepository;

import java.util.List;

@Service
@Transactional
public class PaymentService {

    private PaymentRepository repo;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.repo = paymentRepository;
    }

    public List<Payment> listAll() {
        return repo.findAll();
    }

    public void save(Payment payment){
        repo.save(payment);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public List<Payment> listAllByUserId(Long id) {
        return repo.findAlByUserId(id);
    }
}
