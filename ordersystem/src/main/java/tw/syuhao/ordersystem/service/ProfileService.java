package tw.syuhao.ordersystem.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.syuhao.ordersystem.Ddto.ProfileDTO;
import tw.syuhao.ordersystem.Ddto.UpdateProfileRequest;
import tw.syuhao.ordersystem.entity.Address;
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ProfileDTO getMyProfile(Users user) {
        // 進交易範圍內取 LAZY 欄位，避免 LazyInitializationException
        Address addr = user.getAddress(); // 可能為 null
        return new ProfileDTO(
            user.getId(),
            safe(user.getUsername()),
            safe(user.getEmail()),
            safe(user.getRemark()),
            addr != null ? safe(addr.getPhone()) : null,
            addr != null ? safe(addr.getAddress()) : null
        );
    }

    @Transactional
    public void updateMyProfile(Long userId, UpdateProfileRequest req) {
        Users u = userRepository.findById(userId).orElseThrow();

        // email 檢查（若有變更）
        if (notBlank(req.getEmail())) {
            String newEmail = req.getEmail().trim().toLowerCase();
            if (userRepository.existsByEmailIgnoreCaseAndIdNot(newEmail, userId)) {
                throw new IllegalStateException("Email 已被使用");
            }
            u.setEmail(newEmail);
        }

        if (notBlank(req.getUsername())) {
            u.setUsername(req.getUsername().trim());
        }
        if (req.getRemark() != null) {
            u.setRemark(req.getRemark().trim());
        }

        // Address：允許 phone/address 為空；若兩者皆空但已有 Address，保留 row 但清空欄位（避免 FK/唯一鍵問題）
        boolean hasPhone = notBlank(req.getPhone());
        boolean hasAddr  = notBlank(req.getAddress());

        Address addr = u.getAddress();
        // 無論有沒有 phone/address，只要 Address 尚未建立就新建
        if (addr == null) {
            addr = new Address();
            addr.setUsers(u);   // 維護雙向關係（Users <-> Address）
            u.setAddress(addr);
        }

        // Address 欄位允許為空，若資料庫不允許，給預設值
        addr.setPhone(hasPhone ? req.getPhone().trim() : "-");
        addr.setAddress(hasAddr ? req.getAddress().trim() : "-");

        try {
            // 交由 Users 端 CascadeType.ALL 保存 Address
            userRepository.saveAndFlush(u);
        } catch (DataIntegrityViolationException ex) {
            log.warn("資料庫限制違反", ex);
            throw new IllegalStateException("更新失敗：資料庫限制違反");
        } catch (RuntimeException ex) {
            // 把其他未預期錯誤收束成可讀訊息，避免回 500
            log.error("更新個資發生未預期錯誤", ex);
            throw new IllegalStateException("更新失敗：系統錯誤");
        }
    }

    private static boolean notBlank(String s) { return s != null && !s.trim().isEmpty(); }
    private static String safe(String s) { return s == null ? "" : s; }
}
