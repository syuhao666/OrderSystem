package tw.syuhao.ordersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tw.syuhao.ordersystem.entity.Product;
import tw.syuhao.ordersystem.repository.ProductRepository;
import tw.syuhao.ordersystem.service.ProductService;
import tw.syuhao.ordersystem.service.StockService;

@Controller
@RequestMapping("/admin/stock")
public class StockController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String stockPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String name,
            Model model) {
        int pageSize = 10;
        int pageIndex = page - 1;
        Page<Product> productPage = productService.findProducts(name, null, page, pageSize, null, null, null);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber() + 1);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("name", name);
        model.addAttribute("stockChanges", stockService.getMovementsForAllProducts());
        return "stock";
    }

    @GetMapping("/{id}")
    public String stockPage(@PathVariable Long id, Model model) {
        var product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("movements", stockService.getMovements(id));
        return "stockChange";
    }

    @PostMapping("/adjust")
    public String adjustStock(@RequestParam Long productId,
            @RequestParam int quantity,
            @RequestParam String changeType,
            @RequestParam(required = false) String note) {
        stockService.adjustStock(productId, quantity, changeType, note);

        productService.adjustStock(productId, changeType, quantity);

        return "redirect:/admin/stock/" + productId;
    }
}
