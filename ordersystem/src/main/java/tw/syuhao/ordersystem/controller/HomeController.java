package tw.syuhao.ordersystem.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 首頁導向管理後台首頁
    @GetMapping("/")
    public String home() {
        return "admin-home"; // 對應 src/main/resources/templates/admin-home.html
    }

    // 商品管理頁面
    @GetMapping("/admin/products")
    public String productPage() {
        return "admin-product"; // 對應 admin-product.html
    }
}
