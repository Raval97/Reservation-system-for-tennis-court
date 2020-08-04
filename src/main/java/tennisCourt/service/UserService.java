package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.User;
import tennisCourt.repo.UserRepository;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return repo.findByUsername(s);
    }

    public User findUserByUsername(String s) {
        return repo.findByUsername(s);
    }
}
