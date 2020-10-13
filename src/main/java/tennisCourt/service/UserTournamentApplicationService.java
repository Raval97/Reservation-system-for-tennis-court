package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.UserTournament;
import tennisCourt.model.UserTournamentApplication;
import tennisCourt.repo.UserTournamentApplicationRepository;
import tennisCourt.repo.UserTournamentRepository;

import java.util.List;

@Service
@Transactional
public class UserTournamentApplicationService {

    private UserTournamentApplicationRepository repo;

    @Autowired
    public UserTournamentApplicationService(UserTournamentApplicationRepository userTournamentApplicationRepository) {
        this.repo = userTournamentApplicationRepository;
    }

    public List<UserTournamentApplication> listAll() {
        return repo.findAll();
    }

    public void save(UserTournamentApplication userTournamentApplication){
        repo.save(userTournamentApplication);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public List<String> getAllStatusTournamentToUser(Long id) {
        return repo.findAllStatusTournamentToUser(id);
    }

    public List<UserTournamentApplication> listAllByTournamentId(Long id) {
        return repo.findAllByTournamentId(id);
    }

    public List<String> listAllStatusByTournamentId(Long id) {
        return repo.findAllStatusByTournamentId(id);
    }

    public int countElementsByTournamentId(Long id) {
        return repo.countElementsByTournamentId(id);
    }
}
