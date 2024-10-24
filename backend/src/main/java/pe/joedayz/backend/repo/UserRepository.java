package pe.joedayz.backend.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.joedayz.backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  void deleteById(Long id);

  User findByUsername(String username);

  @Override
  Optional<User> findById(Long id);
}
