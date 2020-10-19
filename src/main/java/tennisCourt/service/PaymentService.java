package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.Payment;
import tennisCourt.repo.PaymentRepository;

import java.util.List;
import java.util.Optional;

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

    public Payment getByTitleAndUser(String title, Long id) {
        return repo.findByTitleAndUser(title, id);
    }

    public void deleteByTitleAndUser(String title, Long id) {
        repo.deleteByTitleAndUser(title, id);
    }

    public Optional<Payment> getById(Long id) {
        return repo.findById(id);
    }
}
