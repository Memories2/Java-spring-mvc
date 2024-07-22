package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import javax.naming.Binding;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProductController {

    private final UploadService uploadService;
    private final ProductService productService;

    ProductController(UploadService uploadService, ProductService productService) {
        this.uploadService = uploadService;
        this.productService = productService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        List<Product> products = this.productService.getAllProduct();
        model.addAttribute("products1", products);
        return "/admin/product/show";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "/admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String createProduct(Model model, @ModelAttribute("newProduct") @Valid Product hoidanit2,
            BindingResult newProductBindingResult, @RequestParam("hoidanitFile") MultipartFile file) {

        // validate
        if (newProductBindingResult.hasErrors()) {
            return "/admin/product/create";
        }
        // upload file
        String nameProductImage = this.uploadService.handleSaveUploadFile(file, "product");
        hoidanit2.setImage(nameProductImage);
        // save product
        this.productService.handleSaveProduct(hoidanit2);

        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/{id}") // hien thi chi tieng san pham
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        return "/admin/product/detail";
    }

    @GetMapping("/admin/product/delete/{id}") // get xoa san pham
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "/admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("newProduct") Product product) { // Post xoa san pham
        this.productService.deleteAProduct(product.getId());
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/update/{id}") // get update
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Product currentProduct = this.productService.getProductById(id);
        model.addAttribute("newProduct", currentProduct);
        return "/admin/product/update";
    }


    
    @PostMapping("/admin/product/update")
    public String postUpdateProduct(Model model, @ModelAttribute("currentProduct") @Valid Product hoidanit,
            BindingResult currentProductBindingResult, @RequestParam("hoidanitFile") MultipartFile file) {
        if (currentProductBindingResult.hasErrors()) {
            return "/admin/product/update";
        }
        Product currentProduct = this.productService.getProductById(hoidanit.getId());

        if (currentProduct != null) {
            //update new image
            if(!file.isEmpty()){
                String img = this.uploadService.handleSaveUploadFile(file, "product");
                currentProduct.setImage(img);
            }

            currentProduct.setName(hoidanit.getName());
            currentProduct.setPrice(hoidanit.getPrice());
            currentProduct.setQuantity(hoidanit.getQuantity());
            currentProduct.setDetailDesc(hoidanit.getDetailDesc());
            currentProduct.setShortDesc(hoidanit.getShortDesc());
            currentProduct.setFactory(hoidanit.getFactory());
            currentProduct.setTarget(hoidanit.getTarget());
            
            this.productService.handleSaveProduct(currentProduct);
        }

        return "redirect:/admin/product";
    }

   
}
