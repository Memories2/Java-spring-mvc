package vn.hoidanit.laptopshop.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;
import vn.hoidanit.laptopshop.repository.CartRepository;
import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    private final OrderRepository oderRepository;
    private final OrderDetailRepository oderDetailRepository;

    ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, UserService userService, OrderRepository oderRepository,
            OrderDetailRepository oderDetailRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
        this.oderRepository = oderRepository;
        this.oderDetailRepository = oderDetailRepository;
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

    public void handleAddProductToCart(String email, long id, HttpSession session) {

        User user = this.userService.getUserByEmail(email);

        if (user != null) {
            // check user co cart chua ? neu chua > tao moi
            Cart cart = this.cartRepository.findByUser(user);
            if (cart == null) {
                // tao moi cart
                Cart otherCart = new Cart();
                otherCart.setUser(user);
                otherCart.setSum(0);
                // save
                cart = this.cartRepository.save(otherCart);
            }

            // luu cart_detail
            // tim product by id
            Product realProduct = this.productRepository.findOneById(id);

            // check sản phẩm đã từng được thêm vào giỏ hàng trước đây chưa
            CartDetail oldDetail = this.cartDetailRepository.findByCartAndProduct(cart, realProduct);

            if (oldDetail == null) {
                CartDetail cd = new CartDetail();
                cd.setCart(cart);
                cd.setProduct(realProduct);
                cd.setPrice(realProduct.getPrice());
                cd.setQuantity(1);
                this.cartDetailRepository.save(cd);
                // update cart sum
                int s = cart.getSum() + 1;
                cart.setSum(s);
                this.cartRepository.save(cart);
                session.setAttribute("sum", s);

            } else {
                oldDetail.setQuantity(oldDetail.getQuantity() + 1);
                this.cartDetailRepository.save(oldDetail);
            }

        }

    }

    public Cart fetchByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    public void handleRemoveCartDetail(long cartDetailId, HttpSession session) {
        Optional<CartDetail> cartDetaiOptional = this.cartDetailRepository.findById(cartDetailId);
        if (cartDetaiOptional.isPresent()) {
            CartDetail cartDetail = cartDetaiOptional.get();
            Cart currentCart = cartDetail.getCart();

            // delete CardDetail
            this.cartDetailRepository.deleteById(cartDetailId);
            ;

            // update Cart
            if (currentCart.getSum() > 1) {
                int s = currentCart.getSum() - 1;
                currentCart.setSum(s);

                // update cho session
                session.setAttribute("sum", s);

                // save cart
                this.cartRepository.save(currentCart);
            } else {
                // dele cart sum = 1
                this.cartRepository.deleteById(currentCart.getId());
                session.setAttribute("sum", 0);
            }
        }
    }

    public void handleUpdateCartBeforeCheckout(List<CartDetail> cartDetails) {
        for (CartDetail cartDetail : cartDetails) {
            Optional<CartDetail> cdOptional = this.cartDetailRepository.findById(cartDetail.getId());
            if (cdOptional.isPresent()) {
                CartDetail currenCartDetail = cdOptional.get();
                currenCartDetail.setQuantity(cartDetail.getQuantity());
                this.cartDetailRepository.save(currenCartDetail);
            }
        }
    }

    public void handlePlaceOrder(User user, HttpSession session, String receiverName, String receiverAddress,
            String receiverPhone) {
        Order order = new Order();

        // Step 1: get cart by user
        Cart cart = this.cartRepository.findByUser(user);
        if (cart != null) {
            List<CartDetail> cartDetails = cart.getCartDetails();
            if (cartDetails != null) {

                // create oder  
                order.setUser(user);
                order.setReceiverName(receiverName);
                order.setReceiverAdress(receiverAddress);
                order.setReceiverPhone(receiverPhone);
                order.setStatus("PENDING");

                double sum = 0;
                for (CartDetail cd : cartDetails) {
                    sum += cd.getPrice()*cd.getQuantity();
                }
                order.setTotalPrice(sum);
                order = this.oderRepository.save(order);


                // create oderDetail
                for (CartDetail cd : cartDetails) {

                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(cd.getProduct());
                    orderDetail.setPrice(cd.getPrice());
                    orderDetail.setQuantity(cd.getQuantity());
                    this.oderDetailRepository.save(orderDetail);
                }

                // Step 2: delete cart_detail and cart
                for (CartDetail cd : cartDetails) {
                    this.cartDetailRepository.deleteById(cd.getId());
                }

                this.cartRepository.deleteById(cart.getId());
                ;
            }

            // Step 3: update session
            session.setAttribute("sum", 0);
        }

    }

}
