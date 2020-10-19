package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.UserTournamentApplication;
import tennisCourt.repo.UserTournamentApplicationRepository;

import java.util.List;
import java.util.Optional;

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

    public Optional<UserTournamentApplication> getById(Long id){
       return repo.findById(id);
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

    public UserTournamentApplication getByTournamentAndUserId(Long tournamentId, Long userId) {
        return repo.findByTournamentAndUserId(tournamentId, userId);
    }

    public void deleteByTournamentAndUserId(Long tournamentId, Long userId) {
        repo.deleteByTournamentAndUserId(tournamentId, userId);
    }

    public boolean hasUserApplicationInTournament(Long tournamentId, Long userId) {
        return repo.hasUserApplicationInTournament(tournamentId, userId);
    }
}
