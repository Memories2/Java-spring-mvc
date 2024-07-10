package vn.hoidanit.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        model.addAttribute("eric", "from controller width model");
        model.addAttribute("hoidanit", "from controller width model");
        return "hello";
    }

    @RequestMapping("/admin/user") // day la ten mien // Ở đây chúng ta đang sử dụng method get
    public String getUserPage(Model model) {
        model.addAttribute("newUser", new User()); 
        model.addAttribute("hoidanit", "from controller width model"); 
        return "admin/user/create";
    }

    @RequestMapping(value = "/admin/user/create1", method = RequestMethod.POST) // đây là tên miền và ta đang nhận form // với menthod = POST
    public String createUser(Model model, @ModelAttribute("newUser") User hoidanit) {
        this.userService.handleSaveUser(hoidanit);
        return "hello";
    }

}

// @RestController
// public class UserController{
// private UserService userService;
// public UserController(UserService userService) {
// this.userService = userService;
// }
// @GetMapping("")
// public String getMethodName() {
// return this.userService.handleHello();
// }
// }
