package pe.joedayz.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.joedayz.backend.model.cart.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
