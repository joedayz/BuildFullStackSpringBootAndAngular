package pe.joedayz.backend.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.joedayz.backend.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  void deleteById(Long id);
  Optional<Product> findById(Long id);
}
