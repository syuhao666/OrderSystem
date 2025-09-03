package tw.syuhao.ordersystem.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.syuhao.ordersystem.service.OauthUserService;

@RestController
@RequestMapping("/auth")
public class OauthController {

    private final OauthUserService service;

    public OauthController(OauthUserService service) {
        this.service = service;
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> payload,
                                             HttpSession session) {

        String providerUserId = payload.get("sub");

        var optionalUser = service.findByProviderUserId(providerUserId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body("User not found");
        }

        var user = optionalUser.get();

        // 建立 session
        session.setAttribute("userId", user.getId());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("name", user.getName());

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "redirect", "/member-center"
        ));
    }
}