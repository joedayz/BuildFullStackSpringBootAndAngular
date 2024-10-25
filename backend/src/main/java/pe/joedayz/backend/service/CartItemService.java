package pe.joedayz.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import pe.joedayz.backend.exceptions.CartItemAlreadyExistsException;
import pe.joedayz.backend.exceptions.CartItemDoesNotExistsException;
import pe.joedayz.backend.model.cart.CartItem;
import pe.joedayz.backend.repo.CartItemRepository;

@Service
public class CartItemService {

  private CartItemRepository repo;

  public CartItemService(CartItemRepository repo) {
    this.repo = repo;
  }

  public List<CartItem> getCartItems() {
    return repo.findAll();
  }

  public CartItem getCartItem(Long userId, Long productId) {
    for (CartItem item : getCartItems()) {
      if (item.getPk().getUser().getId() == userId
          && item.getPk().getProduct().getId() == productId) {
        return item;
      }
    }

    throw new CartItemDoesNotExistsException(
        "CartItem by userId " + userId + " and productId " + productId + " was not found");
  }

  public CartItem addCartItem(CartItem cartItem) {
    for (CartItem item : getCartItems()) {
      if (item.equals(cartItem)) {
        throw new CartItemAlreadyExistsException(
            "CartItem by userId " + cartItem.getPk().getUser().getId() + " and productId "
                + cartItem.getPk().getProduct().getId() + " already exists");
      }
    }
    return this.repo.save(cartItem);
  }

  public CartItem updateCartItem(CartItem cartItem) {
    for (CartItem item : getCartItems()) {
      if (item.equals(cartItem)) {
        item.setQuantity(cartItem.getQuantity());
        return repo.save(item);
      }
    }

    throw new CartItemDoesNotExistsException(
        "Cart item w/ user id " + cartItem.getPk().getUser().getId() + " and product id " +
            cartItem.getProduct().getId() + " does not exist."
    );
  }

  public void deleteCartItem (Long userId, Long productId) {
    for (CartItem item : getCartItems()) {
      if (item.getPk().getUser().getId() == userId && item.getPk().getProduct().getId() == productId) {
        repo.delete(item);
        return;
      }
    }

    throw new CartItemDoesNotExistsException(
        "Cart item w/ user id " + userId + " and product id " + productId + " does not exist."
    );
  }
}
