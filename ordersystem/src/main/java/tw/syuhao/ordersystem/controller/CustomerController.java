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
        var user = Users.builder().role("USER").build();
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
    public String saveCustomer(@ModelAttribute("user") Users formUser) {
        if (formUser.getId() == null) {
            formUser.setCreatedAt(LocalDateTime.now());
        } else {
            var existing = userRepository.findById(formUser.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + formUser.getId()));
            formUser.setCreatedAt(existing.getCreatedAt());
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
}
