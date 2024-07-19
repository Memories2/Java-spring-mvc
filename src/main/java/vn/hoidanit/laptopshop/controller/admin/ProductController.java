package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import javax.naming.Binding;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import vn.hoidanit.laptopshop.domain.Product;


import jakarta.validation.Valid;

@Controller
public class ProductController {

    @GetMapping("/admin/product")
    public String getProduct() {
        return "admin/product/show";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String createProduct(Model model, @ModelAttribute("newProduct") @Valid Product hoidanit2,BindingResult newProductBindingResult){

        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        for(FieldError error : errors){
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }
        if(newProductBindingResult.hasErrors()){
            return "/admin/product/create";
        }
          
        return "redirect:/admin/product";
    }

}
