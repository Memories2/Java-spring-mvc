package vn.hoidanit.laptopshop.controller.client;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.dto.RegisterDTO;
import vn.hoidanit.laptopshop.service.ProductService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class HomepageController {

    private final ProductService productService;

    public HomepageController(ProductService productService) {
        this.productService = productService;
    }



    @GetMapping("/")
    public String getHomePage(Model model){
        List<Product> products = productService.getAllProduct();
        model.addAttribute("products", products);
        return "client/homepage/show";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }
    
    @PostMapping("path")
    public String handleRegister(Model model, @ModelAttribute("registerUser") RegisterDTO registerDTO ) {
       
        return "client/auth/register";
    }
    
    

}
