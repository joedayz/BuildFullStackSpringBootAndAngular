package pe.joedayz.backend.exceptions;

public class CartItemDoesNotExistsException extends RuntimeException {
  public CartItemDoesNotExistsException(String message) {
    super(message);
  }

}
