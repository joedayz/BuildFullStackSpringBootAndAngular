package pe.joedayz.backend.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import pe.joedayz.backend.model.cart.CartItem;

public class UserDto {
  private Long id;
  private String username;
  private String password;
  private String email;
  private String name;
  private String address;
  private String phone;

  @JsonManagedReference
  @OneToMany(mappedBy = "pk.user")
  private List<CartItem> cartItems = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
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
}
