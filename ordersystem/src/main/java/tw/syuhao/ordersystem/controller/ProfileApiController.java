// src/main/java/tw/syuhao/ordersystem/controller/ProfileApiController.java
package tw.syuhao.ordersystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // ...（你原本 /me 相關方法可保留）

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
            if ("未登入".equals(e.getMessage())) return ResponseEntity.status(401).build();
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IllegalArgumentException e){
            // 例如：舊密碼不正確
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            log.error("變更密碼異常", e);
            return ResponseEntity.status(500).body("系統錯誤");
        }
    }
}
