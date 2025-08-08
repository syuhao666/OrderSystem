package tw.syuhao.ordersystem.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.syuhao.ordersystem.entity.Customer;
import tw.syuhao.ordersystem.repository.CustomerRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/customers")
public class CustomerController {
    private final CustomerRepository customerRepository;

    // 顯示列表
    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        return "customerList";
    }

    // 新增表單
    @PostMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customerForm";
    }

    // 編輯表單
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer ID:" + id));
        model.addAttribute("customer", customer);
        return "customerForm";
    }

    // 儲存（新增或更新）
    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute Customer customer) {
        customerRepository.save(customer);
        return "redirect:/admin/customers";
    }

    // 刪除
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerRepository.deleteById(id);
        return "redirect:/admin/customers";
    }
}
