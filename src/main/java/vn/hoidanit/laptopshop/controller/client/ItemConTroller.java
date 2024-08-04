package vn.hoidanit.laptopshop.controller.client;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemConTroller {

    private final ProductService productService;

    public ItemConTroller(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{id}")
    public String getProductPage(Model model, @PathVariable long id) {

        Product pr = this.productService.getProductById(id);
        model.addAttribute("product", pr);
        model.addAttribute("id", id);

        return "client/product/detail";
    }

    @PostMapping("/add-product-to-cart/{id}")
    public String addProductToCart(@PathVariable long id, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");

        // lay san pham
        long productId = id;
        this.productService.handleAddProductToCart(email, productId, session);
        return "redirect:/";
    }

    @GetMapping("/cart")
    public String getCartPage(Model model, HttpServletRequest request) {

        // lay id tu cua nguoi dung tu session
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");

        // tao moi user va gan id vao user
        User currentUser = new User();
        currentUser.setId(id);

        // tim cart tu user
        Cart cart = this.productService.fetchByUser(currentUser);

        // lay Cartdetails tu cart

        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>(): cart.getCartDetails();

        double totalPrice = 0;
        for (CartDetail cartDetail : cartDetails) {
            totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
        }

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        return "client/cart/show";
    }

}
