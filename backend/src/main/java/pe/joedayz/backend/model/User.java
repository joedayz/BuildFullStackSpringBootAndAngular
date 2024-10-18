package pe.joedayz.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import pe.joedayz.backend.model.cart.CartItem;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 35)
  private String username;

  @Column(nullable = false, length = 128)
  private String password;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 128)
  private String address;

  @Column(nullable = false, length = 15)
  private String phone;

  @JsonManagedReference
  @OneToMany(mappedBy = "pk.user", cascade = CascadeType.ALL)
  private List<CartItem> cartItems = new ArrayList<>();

  public User() {
  }

  public User (String username, String password, String email, String name,
      String address, String phone) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.name = name;
    this.address = address;
    this.phone = phone;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public List<CartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<CartItem> cartItems) {
    this.cartItems = cartItems;
  }
}
