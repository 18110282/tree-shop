package com.treeshop.controller.admin;

import com.treeshop.controller.CommonController;
import com.treeshop.entity.UserEntity;
import com.treeshop.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(path = "/admin")
public class UserController {
    @Autowired
    private UsersService userService;

    @Autowired
    private CommonController commonController;

    //Begin Admin Management
    @GetMapping(value = {"/login", "/logout"})
    public String log(HttpServletRequest request, HttpSession session) {
        String url = request.getServletPath();
        Object adminEntity = session.getAttribute("adminEntity");
        if (url.equals("/admin/login")) {
            if (adminEntity == null) {
                return "/views/admin/login";
            } else {
                return "redirect:/admin/home";
            }
        } else if (url.equals("/admin/logout")) {
            session = request.getSession();
            session.removeAttribute("adminEntity");
            session.invalidate();
            return "/views/admin/login";
        }
        return "redirect:/error";
    }

    @GetMapping("/home")
    public String showAdminHome(HttpSession session) {
        UserEntity adminEntity = (UserEntity) session.getAttribute("adminEntity");
        if (adminEntity == null) {
            return "redirect:/admin/login";
        } else {
            UserEntity updatedAdminEntity = userService.findByUserName(adminEntity.getUsername());
            session.setAttribute("adminEntity", updatedAdminEntity);
            return "/views/admin/home";
        }
    }

    @GetMapping("/user/list")
    public String showListUser(HttpSession session,
                               Model model) {
        Object adminEntity = session.getAttribute("adminEntity");
        if (adminEntity == null) {
            return "redirect:/admin/login";
        } else {
            List<UserEntity> userEntityList = userService.findAll();
            model.addAttribute("listUser", userEntityList);
            return "/views/admin/users/list-user";
        }
    }

    @GetMapping("/user/add-user")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new UserEntity());
        model.addAttribute("titlePage", "Thêm người dùng");
        return "/views/admin/users/manage-users";
    }

    @GetMapping("/user/edit/{username}")
    public String showEditUserForm(@PathVariable("username") String username,
                                   Model model) {
        UserEntity userEntity = userService.findByUserName(username);
        model.addAttribute("user", userEntity);
        model.addAttribute("titlePage", "Chỉnh sửa user: " + username);
        model.addAttribute("edit", "");
        return "/views/admin/users/manage-users";
    }

    @GetMapping("/user/delete/{username}")
    public String deleteUser(@PathVariable("username") String username,
                             RedirectAttributes ra,
                             HttpSession session){
        UserEntity admin = (UserEntity) session.getAttribute("adminEntity");
        String currentUser = admin.getUsername();
        if (currentUser.equals(username)) {
            ra.addFlashAttribute("errorMessage", "Không thể xóa user đang đăng nhập<strong class='red'>(" + currentUser +"</strong>).");
        } else {
            userService.deleteUser(username);
            ra.addFlashAttribute("successMessage", "Xóa user: <strong> " + username + "</strong> thành công.");
        }
        return "redirect:/admin/user/list";
    }

    @PostMapping("/check-login")
    public String loginCheck(@RequestParam(value = "username", required = false) String username,
                             @RequestParam(value = "password", required = false) String password,
                             RedirectAttributes ra, HttpSession session) {
        if (userService.checkLogin(username, password)) {
            UserEntity admin = userService.findByUserName(username);
            if (Objects.equals(admin.getRoleId(), "1")) {
                session.setAttribute("adminEntity", admin);
                return "redirect:/admin/home";
            } else {
                ra.addFlashAttribute("errorMessage", "Không có quyền đăng nhập");
                return "redirect:/admin/login";
            }
        } else {
            ra.addFlashAttribute("errorMessage", "Tài khoản hoặc mật khẩu sai");
            return "redirect:/admin/login";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam(value = "newPass", required = false) String password,
                                 RedirectAttributes ra, HttpSession session) {
        UserEntity admin = (UserEntity) session.getAttribute("adminEntity");
        userService.changePassword(admin, password);
        ra.addFlashAttribute("message", "Thay đổi mật khẩu thành công");
        return "redirect:/admin/home";
    }

    @PostMapping("/user/save")
    public String saveUser(@ModelAttribute("user") UserEntity user,
                           RedirectAttributes ra,
                           HttpServletRequest request) {
        String previousUrl = commonController.getHeaderURL(request);
        String url = previousUrl.substring((request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()).length());
        String username = user.getUsername();
        if (url.equals("/admin/user/add-user")) {
            if (userService.checkUsername(username)) {
                ra.addFlashAttribute("errorMessage", username);
                return "redirect:/admin/user/add-user";
            } else {
                ra.addFlashAttribute("successMessage", "Thêm user: <strong> " + username +"</strong> thành công.");
            }
        } else if (url.contains("/admin/user/edit")) {
            ra.addFlashAttribute("successMessage", "Chỉnh sửa user: <strong> " + username + "</strong> thành công.");
        }
        userService.saveUser(user);
        return "redirect:/admin/user/list";
    }
    //End Admin Management
}
