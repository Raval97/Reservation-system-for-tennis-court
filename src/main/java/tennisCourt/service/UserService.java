package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tennisCourt.model.Users;
import tennisCourt.repo.UserRepository;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserRepository repo;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.repo = userRepository;
    }

    public List<Users> listAll() {
        return repo.findAll();
    }

    public Users get(long id) {
        return repo.findByIdUser(id);
    }

    public void save(Users user){
        repo.save(user);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return repo.findByUsername(s);
    }

    public Users findUserByUsername(String s) {
        return repo.findByUsername(s);
    }
}
