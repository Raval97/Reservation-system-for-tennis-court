package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.UserTournament;
import tennisCourt.repo.UserTournamentRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserTournamentService {

    private UserTournamentRepository repo;

    @Autowired
    public UserTournamentService(UserTournamentRepository userTournamentRepository) {
        this.repo = userTournamentRepository;
    }

    public List<UserTournament> listAll() {
        return repo.findAllParticipant();
    }

    public void save(UserTournament userTournament){
        repo.save(userTournament);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public List<UserTournament> listAllByTournamentId(Long id) {
        return repo.findAllByTournamentId(id);
    }

    public int countElementsByTournamentId(Long id) {
        return repo.countElementsByTournamentId(id);
    }

    public Optional<UserTournament> getById(long id) {
        return repo.findById(id);
    }

    public void deleteByTournamentAndUserId(Long tournamentId, Long userId) {
        repo.deleteByTournamentAndUserId(tournamentId, userId);
    }
}
