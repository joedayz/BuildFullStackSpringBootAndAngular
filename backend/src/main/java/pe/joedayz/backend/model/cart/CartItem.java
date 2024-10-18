package pe.joedayz.backend.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import java.util.Date;
import java.util.Objects;
import pe.joedayz.backend.model.Product;
import pe.joedayz.backend.model.User;

@Entity
@Table(name = "cart_item")
public class CartItem {

  @EmbeddedId
  @JsonIgnore
  private CartItemPK pk;


  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date addedOn = new Date();

  @Column(nullable = false)
  private int quantity = 1;


  public CartItem () {

  }

  public CartItem (User user, Product product, int quantity) {
    pk = new CartItemPK();
    pk.setUser(user);
    pk.setProduct(product);
    this.quantity = quantity;
  }

  @Transient
  public Product getProduct () {
    return pk.getProduct();
  }

  @Transient
  public double getTotalPrice () {
    return quantity * getProduct().getPrice();
  }

  public CartItemPK getPk() {
    return pk;
  }

  public void setPk(CartItemPK pk) {
    this.pk = pk;
  }

  public Date getAddedOn() {
    return addedOn;
  }

  public void setAddedOn(Date addedOn) {
    this.addedOn = addedOn;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass())
      return false;

    CartItem that = (CartItem) o;
    return Objects.equals(pk.getUser().getId(), that.pk.getUser().getId()) &&
        Objects.equals(getProduct().getId(), that.getProduct().getId());
  }

}
