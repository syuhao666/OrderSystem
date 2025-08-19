package tw.syuhao.ordersystem.controller;

import java.util.List;

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
public class MemberCenterController {

    private final UserService userService;

    /** 會員中心 */
@GetMapping("/member")
public String member(HttpSession session, Model model) {
    Users u = (Users) session.getAttribute("user");
    if (u == null) return "redirect:/login";
    model.addAttribute("member", u);
    return "member"; // 對應 templates/member.html 或你的 memberCenter.html(靜態版則用 /memberCenter.html)
}

    /** 個資頁 */
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        Users current = (Users) session.getAttribute("user");
        if (current == null) return "redirect:/login";
        Users fresh = userService.loadByPrincipal(
                current.getEmail() != null ? current.getEmail() : current.getUsername());
        if (fresh == null) return "redirect:/login";
        fresh.setPassword(null);
        model.addAttribute("member", fresh);
        return "profile";
    }

    /** 更新個資 */
    @PostMapping("/profile")
    public String updateProfile(HttpSession session,
                                @RequestParam String username,
                                @RequestParam String email,
                                Model model) {
        Users current = (Users) session.getAttribute("user");
        if (current == null) return "redirect:/login";
        try {
            Users updated = userService.updateProfile(current.getId(), username, email);
            updated.setPassword(null);
            session.setAttribute("user", updated); // 同步 Session
            return "redirect:/member";
        } catch (IllegalStateException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("member", current);
            return "profile";
        }
    }

    /** 修改密碼頁 */
    @GetMapping("/change-password")
    public String changePasswordPage(HttpSession session) {
        Users current = (Users) session.getAttribute("user");
        if (current == null) return "redirect:/login";
        return "change-password";
    }

    /** 修改密碼動作 */
    @PostMapping("/change-password")
    public String changePassword(HttpSession session,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Model model) {
        Users current = (Users) session.getAttribute("user");
        if (current == null) return "redirect:/login";

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "新密碼與確認密碼不一致");
            return "change-password";
        }
        try {
            userService.changePassword(current.getId(), oldPassword, newPassword);
            session.invalidate(); // 強制重新登入
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "change-password";
        }
    }

    /** 訂單頁（示範資料，之後串你的資料） */
    @GetMapping("/orders")
    public String ordersPage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        record Row(String no, String date, String status, int amount) {}
        List<Row> rows = List.of(
            new Row("A20250813-001","2025-08-13","已出貨",1280),
            new Row("A20250810-002","2025-08-10","處理中",560)
        );
        model.addAttribute("orders", rows);
        return "orders";
    }

    /** 優惠與積分頁（示範資料） */
    @GetMapping("/coupons")
    public String couponsPage(HttpSession session, Model model) {
        Users current = (Users) session.getAttribute("user");
        if (current == null) return "redirect:/login";
        Users fresh = userService.loadByPrincipal(
                current.getEmail() != null ? current.getEmail() : current.getUsername());
        if (fresh == null) return "redirect:/login";
        fresh.setPassword(null);
        model.addAttribute("member", fresh);
        model.addAttribute("coupons", List.of("滿 1000 折 100", "新會員免運券"));
        return "coupons";
    }
}
