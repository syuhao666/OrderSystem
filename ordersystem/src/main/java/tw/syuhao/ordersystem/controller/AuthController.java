package tw.syuhao.ordersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.syuhao.ordersystem.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        // 這裡請自行驗證帳密
        // 通過後產生 token
        return jwtUtil.generateToken(username);
    }
}
