package pe.joedayz.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.joedayz.backend.exceptions.UserNotFoundException;
import pe.joedayz.backend.model.User;
import pe.joedayz.backend.repo.UserRepository;

@Service
public class UserService {
  private final UserRepository repo;

  public UserService(UserRepository repo) {
    this.repo = repo;
  }

  public List<User> getUsers() {
    return repo.findAll();
  }

  @Autowired
  private PasswordEncoder bcryptEncoder;

  public User getUser(Long id) {
    return repo.findById(id).orElseThrow(()->
        new UserNotFoundException("User by id "+ id + " was not found"));
  }

  public User updateUser(Long id, User user){
    User oldUser = getUser(id);
    oldUser.setUsername(user.getUsername());
    oldUser.setEmail(user.getEmail());
    oldUser.setName(user.getName());
    oldUser.setAddress(user.getAddress());
    oldUser.setPhone(user.getPhone());

    return repo.save(oldUser);
  }

  public void deleUser(Long id){
    repo.deleteById(id);
  }

}
