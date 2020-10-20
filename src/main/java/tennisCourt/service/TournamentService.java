package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.Tournament;
import tennisCourt.repo.TournamentRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TournamentService {

    private TournamentRepository repo;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository) {
        this.repo = tournamentRepository;
    }

    public List<Tournament> listAll() {
        return repo.findAllTournament();
    }

    public void save(Tournament tournament){
        repo.save(tournament);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public Optional<Tournament> getById(Long id) {
        return repo.findById(id);
    }

    public Tournament getByTitle(String title) {
        return repo.findByTitle(title);
    }
}
