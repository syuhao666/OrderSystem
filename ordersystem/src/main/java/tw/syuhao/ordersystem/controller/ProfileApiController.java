// src/main/java/tw/syuhao/ordersystem/controller/ProfileApiController.java
package tw.syuhao.ordersystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.syuhao.ordersystem.Ddto.ChangePasswordRequest;
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.repository.UserRepository;
import tw.syuhao.ordersystem.service.UserService;

@Slf4j
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileApiController {

    private final UserRepository userRepository;
    private final UserService userService;

    private Users requireLogin(HttpSession session){
        Users u = (Users) session.getAttribute("user");
        if (u == null) throw new IllegalStateException("未登入");
        // 重新查一次避免 detached
        return userRepository.findById(u.getId()).orElseThrow();
    }

    /** 補上 /me，避免 404 */
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session){
        try {
            Users u = requireLogin(session);
            // 只回需要的欄位，避免 Lazy 序列化踩雷
            record Me(Long id, String username, String email) {}
            return ResponseEntity.ok(new Me(u.getId(), u.getUsername(), u.getEmail()));
        } catch (IllegalStateException e){
            if ("未登入".equals(e.getMessage())) {
                return ResponseEntity.status(401).body("未登入");
            }
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e){
            log.error("讀取個資異常", e);
            return ResponseEntity.status(500).body("系統錯誤");
        }
    }

    /** 變更密碼 */
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest req, HttpSession session){
        try{
            Users user = requireLogin(session);

            if (req.getNewPassword() == null || req.getNewPassword().trim().length() < 6) {
                return ResponseEntity.badRequest().body("新密碼至少 6 碼");
            }

            userService.changePassword(user.getId(), req.getOldPassword(), req.getNewPassword());
            return ResponseEntity.noContent().build(); // 204

        } catch (IllegalStateException e){
            if ("未登入".equals(e.getMessage())) return ResponseEntity.status(401).body("未登入");
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            log.error("變更密碼異常", e);
            return ResponseEntity.status(500).body("系統錯誤");
        }
    }
}
