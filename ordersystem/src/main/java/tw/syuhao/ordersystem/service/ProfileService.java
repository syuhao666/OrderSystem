// src/main/java/tw/syuhao/ordersystem/service/ProfileService.java
package tw.syuhao.ordersystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import tw.syuhao.ordersystem.Ddto.ChangePasswordRequest;
import tw.syuhao.ordersystem.Ddto.UpdateProfileRequest;
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.repository.UserRepository;
import tw.syuhao.ordersystem.utils.BCrypt;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    /** 一定從 DB 讀取最新使用者，再更新（避免只依賴 session 裡的快照） */
    @Transactional(readOnly = true)
    public Users findFreshById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("找不到使用者"));
    }

    /** 更新基本資料（也會 hit DB） */
    @Transactional
    public Users updateProfile(Users sessionUser, UpdateProfileRequest req) {
        if (sessionUser == null) throw new IllegalArgumentException("未登入");

        if (!StringUtils.hasText(req.getUsername())) throw new IllegalArgumentException("使用者名稱不可為空");
        if (!StringUtils.hasText(req.getEmail()))    throw new IllegalArgumentException("Email 不可為空");

        // 從 DB 取最新使用者
        Users dbUser = findFreshById(sessionUser.getId());

        // Email 唯一性（排除自己）
        if (userRepository.existsByEmailAndIdNot(req.getEmail(), dbUser.getId())) {
            throw new IllegalArgumentException("Email 已被使用");
        }

        dbUser.setUsername(req.getUsername().trim());
        dbUser.setEmail(req.getEmail().trim());
        dbUser.setRemark(req.getRemark() == null ? null : req.getRemark().trim());

        return userRepository.save(dbUser); // ← 真正寫回 DB
    }

    /** 變更密碼（hit DB 取得 hash，驗證舊密碼、產生新 hash、save） */
    @Transactional
    public void changePassword(Users sessionUser, ChangePasswordRequest req) {
        if (sessionUser == null) throw new IllegalArgumentException("未登入");

        if (!StringUtils.hasText(req.getCurrentPassword())
                || !StringUtils.hasText(req.getNewPassword())
                || !StringUtils.hasText(req.getConfirmPassword())) {
            throw new IllegalArgumentException("請完整填寫舊密碼 / 新密碼 / 確認密碼");
        }
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new IllegalArgumentException("新密碼與確認密碼不一致");
        }
        if (req.getNewPassword().length() < 6) {
            throw new IllegalArgumentException("新密碼長度至少 6 碼");
        }

        // 一定從 DB 取得最新的 hash
        Users dbUser = findFreshById(sessionUser.getId());

        // 用 DB 中的 hash 驗證舊密碼
        if (!BCrypt.checkpw(req.getCurrentPassword(), dbUser.getPassword())) {
            throw new IllegalArgumentException("舊密碼不正確");
        }

        // 產生新 hash、寫回 DB
        String newHash = BCrypt.hashpw(req.getNewPassword(), BCrypt.gensalt());
        dbUser.setPassword(newHash);
        userRepository.save(dbUser);
    }
}
