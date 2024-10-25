package pe.joedayz.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.joedayz.backend.config.JwtUtil;
import pe.joedayz.backend.model.Product;
import pe.joedayz.backend.model.User;
import pe.joedayz.backend.model.cart.CartItem;
import pe.joedayz.backend.service.CartItemService;
import pe.joedayz.backend.service.JwtUserDetailsService;
import pe.joedayz.backend.service.ProductService;
import pe.joedayz.backend.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class APIController {

  private final UserService userService;
  private final ProductService productService;
  private final CartItemService cartItemService;
  private JwtUserDetailsService jwtUserDetailsService;
  private JwtUtil jwtUtil;



  public APIController(UserService userService, ProductService productService,
      CartItemService cartItemService, JwtUserDetailsService jwtUserDetailsService,JwtUtil jwtUtil) {
    this.userService = userService;
    this.productService = productService;
    this.cartItemService = cartItemService;
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtUtil = jwtUtil;
  }


  @PostMapping("/create-token")
  public ResponseEntity<?> createToken(@RequestBody Map<String, String> user) throws Exception{
    Map<String, Object> tokenResponse = new HashMap<>();
    final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(user.get("username"));
    String token = jwtUtil.generateToken(userDetails);
    tokenResponse.put("token", token);
    return ResponseEntity.ok(tokenResponse);
  }

  @GetMapping("/users")
  public ResponseEntity<?> getUsers() {
    return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
    return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
  }

  @PutMapping("/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody Map<String, String> user) {
    User newUser = new User(
        (String) user.get("username"),
        (String) user.get("password"),
        (String) user.get("email"),
        (String) user.get("name"),
        (String) user.get("address"),
        (String) user.get("phone")
    );
    return new ResponseEntity<>(userService.updateUser(id, newUser), HttpStatus.OK);
  }

  @GetMapping("/users/{id}/cart")
  public ResponseEntity<List<CartItem>> getCartItems(@PathVariable("id") Long id) {
    return new ResponseEntity<>(userService.getUser(id).getCartItems(), HttpStatus.OK);
  }

  @PostMapping("/users/{id}/cart/add/{productId}")
  public ResponseEntity<User> addToUserCart(@PathVariable("id") Long id, @PathVariable("productId") Long productId) {

    User user = userService.getUser(id);
    Product product = productService.getProduct(productId);

    CartItem cartItem = new CartItem(user, product, 1);
    cartItemService.addCartItem(cartItem);

    return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
  }

}
