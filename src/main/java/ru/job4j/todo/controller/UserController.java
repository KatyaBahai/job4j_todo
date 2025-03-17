package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
@ThreadSafe
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute User wowUser) {
        var savedUser = userService.save(wowUser);
        if (savedUser.isEmpty()) {
            model.addAttribute("message", "This email already exists in the system.");
            return "errors/404";
        }
        return "redirect:/index";
    }

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "users/register";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User wowUser, Model model, HttpServletRequest request) {
        var userOptional = userService.findByEmailAndPassword(wowUser.getEmail(), wowUser.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "The username or password that you have entered is invalid.");
            return "users/login";
        }
        HttpSession session = request.getSession();
        session.setAttribute("wowUser", userOptional.get());
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }
}
