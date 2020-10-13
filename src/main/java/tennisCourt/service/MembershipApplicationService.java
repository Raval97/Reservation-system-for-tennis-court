package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.MembershipApplication;
import tennisCourt.model.Reservation;
import tennisCourt.model.UserReservation;
import tennisCourt.repo.MembershipApplicationRepository;
import tennisCourt.repo.UserReservationRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MembershipApplicationService {

    private MembershipApplicationRepository repo;

    @Autowired
    public MembershipApplicationService(MembershipApplicationRepository membershipApplicationRepository) {
        this.repo = membershipApplicationRepository;
    }

    public List<MembershipApplication> listAll() {
        return repo.findAll();
    }

    public Optional<MembershipApplication> get(long id) {
        return repo.findById(id);
    }

    public void save(MembershipApplication membershipApplication){
        repo.save(membershipApplication);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public String getLatestMembershipApplicationDecision(Long id){
        List<String> decisionList = repo.getDecisionByUserId(id);
        if (decisionList.size() == 0)
            return "none";
        else
            return decisionList.get(0);
    }

    public List<MembershipApplication> listAllActive() {
        return repo.finAllActive();
    }

    public MembershipApplication getByID(Long id) {
        return repo.findByIdParam(id);
    }
}
