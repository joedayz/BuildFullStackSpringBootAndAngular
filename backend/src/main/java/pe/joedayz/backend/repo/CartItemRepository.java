package pe.joedayz.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.joedayz.backend.model.cart.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
