package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.User;
import tennisCourt.repo.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserService {

    private UserRepository repo;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.repo = userRepository;
    }

    public List<User> listAll() {
        return repo.findAll();
    }

    public User get(long id) {
        return repo.findByIdUser(id);
    }

    public void save(User user){
        repo.save(user);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

}
