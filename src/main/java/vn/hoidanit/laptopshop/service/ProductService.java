package vn.hoidanit.laptopshop.service;

import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.repository.ProductRepository;
import java.util.List;

@Service
public class ProductService {
    
    private final ProductRepository productRepository ;

    ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product handleSaveProduct(Product product){
        Product eric = this.productRepository.save(product);
        return eric;
    }

    public List <Product> getAllProduct(){
        return this.productRepository.findAll();
    }

}
