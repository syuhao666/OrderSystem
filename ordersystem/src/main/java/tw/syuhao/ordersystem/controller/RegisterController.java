package tw.syuhao.ordersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.service.UserService;

@Controller
public class RegisterController {

    @Autowired
    private UserService service;

    // 顯示註冊表單頁面
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; 
    }

    // 接收註冊表單資料並存進資料庫
    @PostMapping("/register")
    public String apiRegister(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String email) throws Exception {
        Users u = service.register(username, password, email);
        u.setPassword(null);
        return "redirect:/login";
    }
}
