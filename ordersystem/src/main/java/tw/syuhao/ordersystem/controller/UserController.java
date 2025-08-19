package tw.syuhao.ordersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.repository.UserRepository;
import tw.syuhao.ordersystem.service.UserService;

@RestController
@RequestMapping("/api") // ★ 統一把這支變成 API Controller
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    private UserService service;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 註冊（表單頁還是用 /register 的 Thymeleaf；這支給前端 AJAX 用也行）
    @PostMapping("/register")
    public Users apiRegister(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String email) throws Exception {
        Users u = service.register(username, password, email);
        u.setPassword(null);
        return u;
    }

    // 目前登入的使用者（給前端 JS 用）
    @GetMapping("/currentUser")
    public Users getCurrentUser(HttpSession session) {
        Users user = (Users) session.getAttribute("user"); // ★ 統一用 currentUser
        if (user != null) {
            user.setPassword(null);
        }
        return user; // 未登入會回 null
    }

    // （可選）API 版的登入，不要佔用 /login
    @PostMapping("/login")
    public Users apiLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session) {
        Users user = service.login(email, password);
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
        }
        return user;
    }

    // （可選）API 版登出
    @PostMapping("/logout")
    public void apiLogout(HttpSession session) {
        session.invalidate();
    }

	
	@RequiredArgsConstructor
	public class UserApiController {

    @GetMapping("/currentUser")
    public Users getCurrentUser(HttpSession session) {
        Users user = (Users) session.getAttribute("user"); // ★ 與登入一致
        if (user != null) {
            user.setPassword(null);
        }
        return user; // 未登入回 null
    }
}


}
