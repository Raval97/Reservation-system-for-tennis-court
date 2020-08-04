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

}
