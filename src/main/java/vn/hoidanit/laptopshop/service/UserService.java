package vn.hoidanit.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleSaveUser(User user) {
         User eric = this.userRepository.save(user); // ket noi xuong database va luu user
         return eric;
    }

    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    public List<User> getAllUserByEmail(String email){
        return this.userRepository.findByEmail(email);
    }
    
    public User getUserById(long id){
        return this.userRepository.findOneById(id);
    }

    public void deleteAUser(long id){
        this.userRepository.deleteById(id);
    }

}
