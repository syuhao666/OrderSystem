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
        u.setPassword(null);
        // ★ 統一使用這個 key
        session.setAttribute("user", u);

         // 根據角色導向不同頁面
        if ("ADMIN".equalsIgnoreCase(u.getRole())) {
            session.setAttribute("admin", u); // 若 adminHome.html 需用 admin 變數
            return "redirect:/admin/";
        } else {
            return "redirect:/index.html";
        }

        // ★ 登入後回首頁（你的 index.html 會自動 fetch /api/user 來切 UI）
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
