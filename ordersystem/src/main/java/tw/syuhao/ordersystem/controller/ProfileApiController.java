// src/main/java/tw/syuhao/ordersystem/controller/ProfileApiController.java
package tw.syuhao.ordersystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import tw.syuhao.ordersystem.Ddto.ChangePasswordRequest;
import tw.syuhao.ordersystem.Ddto.ProfileDTO;
import tw.syuhao.ordersystem.Ddto.UpdateProfileRequest;
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileApiController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<?> getProfile(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(401, "未登入"));

        // 從 DB 讀最新（可選，但建議）
        Users fresh = profileService.findFreshById(user.getId());

        ProfileDTO dto = new ProfileDTO();
        dto.setId(fresh.getId());
        dto.setUsername(fresh.getUsername());
        dto.setEmail(fresh.getEmail());
        dto.setRemark(fresh.getRemark());
        dto.setCreatedAt(fresh.getCreatedAt());
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest req, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(401, "未登入"));
        try {
            Users saved = profileService.updateProfile(user, req);
            saved.setPassword(null); // 不回傳密碼
            session.setAttribute("user", saved); // 讓導覽列即時顯示新名稱/Email
            return ResponseEntity.ok(new ApiOk("更新成功", saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiError(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiError(500, "更新失敗"));
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest req, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(401, "未登入"));

        try {
            profileService.changePassword(user, req);

            // 兩種策略擇一：
            // A) 強制重新登入（安全）：
            // session.invalidate();
            // return ResponseEntity.ok(new ApiOk("密碼已更新，請重新登入", null));

            // B) 保持登入（方便）：
            return ResponseEntity.ok(new ApiOk("密碼已更新", null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiError(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiError(500, "密碼更新失敗"));
        }
    }

    record ApiOk(String message, Object data) {}
    record ApiError(int status, String message) {}
}
