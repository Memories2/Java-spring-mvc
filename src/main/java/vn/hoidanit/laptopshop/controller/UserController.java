package vn.hoidanit.laptopshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        List<User> arrUsers = this.userService.getAllUserByEmail("1@gmail.com");
        System.out.println(arrUsers);
        model.addAttribute("eric", "from controller width model");
        model.addAttribute("hoidanit", "from controller width model");
        return "hello";
    }

    @RequestMapping("/admin/user") // hien thi danh sach nguoi dung
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUser();
        model.addAttribute("users1", users);
        return "admin/user/table-user";
    }

    @RequestMapping("/admin/user/{id}") // hien thi chi tiet nguoi dung
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user); // có vẻ như tác dụng của model là truyền các biến từ sever qua cho web tĩnh -
                                          // truyền id từ controller qua view
        model.addAttribute("id", id);
        return "admin/user/show";
    }

    @RequestMapping("/admin/user/update/{id}") // Get update nguoi dung
    public String getUpdateUserPage(Model model) {

        model.addAttribute("newUser", new User());
        return "admin/user/update";
    }

     @RequestMapping(value = "/admin/user/update/{id}", method = RequestMethod.POST)  // save update nguoi dung
    public String updateUser(Model model, @ModelAttribute("newUser") User hoidanit) {
        this.userService.handleSaveUser(hoidanit); //
        return "redirect:/admin/user";
    }


    @RequestMapping("/admin/user/create") // day la ten mien // Ở đây chúng ta đang sử dụng method get - tao moi user
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST) // đây là tên miền và ta đang nhận form
                                                                               // với menthod = POST
    public String createUser(Model model, @ModelAttribute("newUser") User hoidanit) {
        this.userService.handleSaveUser(hoidanit); //
        return "redirect:/admin/user";
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
