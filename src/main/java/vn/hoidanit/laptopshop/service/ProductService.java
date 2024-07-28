package vn.hoidanit.laptopshop.service;

import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;
import vn.hoidanit.laptopshop.repository.CartRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;

    ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, UserService userService) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
    }

    public Product handleSaveProduct(Product product) {
        Product eric = this.productRepository.save(product);
        return eric;
    }

    public List<Product> getAllProduct() {
        return this.productRepository.findAll();
    }

    public Product getProductById(long id) {
        return this.productRepository.findOneById(id);
    }

    public void deleteAProduct(long id) {
        this.productRepository.deleteById(id);
    }

    public void handleAddProductToCart(String email, long id) {

        User user = this.userService.getUserByEmail(email);

        if (user != null) {
            // check user co cart chua ? neu chua > tao moi
            Cart cart = this.cartRepository.findByUser(user);
            if (cart == null) {
                // tao moi cart
                Cart otherCart = new Cart();
                otherCart.setUser(user);
                otherCart.setSum(1);
                // save
                cart = this.cartRepository.save(otherCart);
            }

            // luu cart_detail
            // tim product by id
            Product realProduct = this.productRepository.findOneById(id);
            CartDetail cd = new CartDetail();
            cd.setCart(cart);
            cd.setProduct(realProduct);
            cd.setPrice(realProduct.getPrice());
            cd.setQuantity(1);

            this.cartDetailRepository.save(cd);
        }

    }

}
