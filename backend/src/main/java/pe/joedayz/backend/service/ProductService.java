package pe.joedayz.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import pe.joedayz.backend.exceptions.ProductNotFoundException;
import pe.joedayz.backend.model.Product;
import pe.joedayz.backend.repo.ProductRepository;

@Service
public class ProductService {

  private final ProductRepository repo;

  public ProductService(ProductRepository repo) {
    this.repo = repo;
  }

  public List<Product> getProducts(){
    return repo.findAll();
  }

  public Product getProduct(Long id){
    return repo.findById(id).orElseThrow(()->
        new ProductNotFoundException("Product by id "+ id + " was not found"));
  }

  public Product addProduct(Product product){
    return repo.save(product);
  }

  public Product updateProduct(Long id, Product product){
    Product oldProduct = getProduct(id);
    oldProduct.setName(product.getName());
    oldProduct.setPrice(product.getPrice());
    oldProduct.setDescription(product.getDescription());
    oldProduct.setImage(product.getImage());
    return repo.save(oldProduct);
  }

  public void deleteProduct(Long id) {
    repo.deleteById(id);
  }
}
