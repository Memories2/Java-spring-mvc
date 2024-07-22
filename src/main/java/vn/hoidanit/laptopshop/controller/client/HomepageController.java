package vn.hoidanit.laptopshop.controller.client;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.RegisterDTO;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class HomepageController {

    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public HomepageController(ProductService productService,UserService userService,PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
    
    @PostMapping("/register")
    public String handleRegister(Model model, @ModelAttribute("registerUser") RegisterDTO registerDTO ) {
        
        User user = this.userService.registerDTOtoUser(registerDTO);
        String hashPassWord = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassWord);
        // rolde la mot doi tuong
        user.setRole(this.userService.getRoleByName("USER"));
        //save
        this.userService.handleSaveUser(user);

        return "redirect:/login";
    }
    
    
    @GetMapping("/login")
    public String getLoginPage(Model model){
        
        return "client/auth/login";
    }

}
