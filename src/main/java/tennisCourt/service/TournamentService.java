package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.Court;
import tennisCourt.model.Tournament;
import tennisCourt.repo.CourtRepository;
import tennisCourt.repo.TournamentRepository;

import java.util.List;

@Service
@Transactional
public class TournamentService {

    private TournamentRepository repo;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository) {
        this.repo = tournamentRepository;
    }

    public List<Tournament> listAll() {
        return repo.findAll();
    }

    public void save(Tournament tournament){
        repo.save(tournament);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

}
