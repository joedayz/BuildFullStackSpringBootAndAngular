package pe.joedayz.backend.service;

import java.util.ArrayList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.joedayz.backend.model.User;
import pe.joedayz.backend.repo.UserRepository;

@Service
public class JwtUserDetailsService implements
    UserDetailsService {

  private UserRepository repo;
  private PasswordEncoder bcryptEncoder;

  public JwtUserDetailsService(UserRepository repo, PasswordEncoder bcryptEncoder) {
    this.repo = repo;
    this.bcryptEncoder = bcryptEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = repo.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    return new org.springframework.security.core.userdetails.User(user.getUsername(),
        user.getPassword(),
        new ArrayList<>());
  }

  public User save(User user) {
    User newUser = new User(user.getUsername(), bcryptEncoder.encode(user.getPassword()),
        user.getEmail(), user.getName(), user.getAddress(), user.getPhone());
    return repo.save(newUser);
  }

}
