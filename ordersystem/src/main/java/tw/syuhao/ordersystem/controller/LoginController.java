package tw.syuhao.ordersystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.service.UserService;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
        Users u = userService.login(email, password);
        if (u == null) {
            model.addAttribute("error", "帳號或密碼錯誤");
            return "login";
        }
        u.setPassword(null);                     // 不把密碼放 Session
        session.setAttribute("currentUser", u);  // 標記已登入
        return "redirect:/member";               // → 會員中心
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
