package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.Client;
import tennisCourt.repo.ClientRepository;

import java.util.List;

@Service
@Transactional
public class ClientService {

    private ClientRepository repo;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.repo = clientRepository;
    }

    public List<Client> listAll() {
        return repo.findAll();
    }

    public Client get(long id) {
        return repo.findByIdClient(id);
    }

    public void save(Client client){
        repo.save(client);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public void deleteByUserId(long id) {
        repo.deleteByUserId(id);
    }

    public List<Client> getAllWhoHasActiveApplication() {
        return repo.findAllWhoHasActiveApplication();
    }

    public List<Client> getAllWhoInClub() {
        return repo.findAllWhoInClub();
    }

    public Client getByUserId(Long id) {
        return repo.findBuUserId(id);
    }

    public List<Client> listAllByInTournamentApplicationByThemID(Long id) {
        return repo.findAllByInTournamentApplicationByThemID(id);
    }

    public List<Client> listAllByInTournamentByThemID(Long id) {
        return repo.findAllByInTournamentByThemID(id);
    }
}
