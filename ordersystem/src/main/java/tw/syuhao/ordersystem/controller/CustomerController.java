package tw.syuhao.ordersystem.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import tw.syuhao.ordersystem.entity.Address;
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.repository.UserRepository;
import tw.syuhao.ordersystem.utils.BCrypt;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/customers")
public class CustomerController {
      private final UserRepository userRepository;

    // 列表：一定從 users 出發，LEFT JOIN address
    @GetMapping
    public String listCustomers(Model model) {
        // var users = userRepository.findAllWithAddress();
        model.addAttribute("users", userRepository.findAllWithAddress());
        return "customerList";
    }

    // 新增表單
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        var user = Users.builder().role("customer").build();
        user.attachAddress(new Address()); // 讓表單可綁定 *{address.address}
        model.addAttribute("user", user);
        return "customerForm";
    }

    // 編輯表單
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        if (user.getAddress() == null) user.attachAddress(new Address());
        model.addAttribute("user", user);
        return "customerForm";
    }

    // 儲存（新增或更新）
    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute("users") Users formUser) {
        if (formUser.getId() == null) {
            formUser.setCreatedAt(LocalDateTime.now());
        } else {
            var existing = userRepository.findById(formUser.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + formUser.getId()));
            formUser.setCreatedAt(existing.getCreatedAt());
        }

         // ========== 密碼處理 ==========
        if (formUser.getId() == null) {
            // 新增：一定加密
            String raw = safeTrim(formUser.getPassword());
            formUser.setPassword(BCrypt.hashpw(raw, BCrypt.gensalt()));
        } else {
            // 更新：依照使用者是否輸入新密碼決定
            Users existing = userRepository.findById(formUser.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + formUser.getId()));

            String input = safeTrim(formUser.getPassword());
            if (input.isEmpty()) {
                // 沒填新密碼 → 沿用舊雜湊
                formUser.setPassword(existing.getPassword());
            } else if (!isBcryptHash(input)) {
                // 有填且不是雜湊 → 重新雜湊
                formUser.setPassword(BCrypt.hashpw(input, BCrypt.gensalt()));
            } else {
                // 已是雜湊（極少見）→ 直接沿用，避免二次加密
                formUser.setPassword(input);
            }
        }

        // 維護雙向關聯：Address.user 指回來 Users
        if (formUser.getAddress() != null) {
            formUser.getAddress().setUsers(formUser);
        }
        userRepository.save(formUser); // cascade=ALL → 一起存 Address

        return "redirect:/admin/customers";
    }

    // 刪除
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        userRepository.deleteById(id); // orphanRemoval=true → 連帶刪 Address
        return "redirect:/admin/customers";
    }

    // ===== Utils =====

    /** 判斷是否為常見的 BCrypt 雜湊前綴（$2a$/$2b$/$2y$） */
    private static boolean isBcryptHash(String value) {
        if (value == null) return false;
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    /** 只有在 Users 有 createdAt 欄位時才會生效；沒有則忽略 */
    private static void trySetCreatedAt(Users u) {
        try {
            var field = Users.class.getDeclaredField("createdAt");
            field.setAccessible(true);
            if (field.get(u) == null) field.set(u, LocalDateTime.now());
        } catch (NoSuchFieldException ignored) {
        } catch (IllegalAccessException e) {
            // ignore
        }
    }

    /** 複製舊資料的 createdAt（若存在） */
    private static void tryCopyCreatedAt(Users from, Users to) {
        try {
            var field = Users.class.getDeclaredField("createdAt");
            field.setAccessible(true);
            Object createdAt = field.get(from);
            field.set(to, createdAt);
        } catch (NoSuchFieldException ignored) {
        } catch (IllegalAccessException e) {
            // ignore
        }
    }
}
