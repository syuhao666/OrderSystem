package tw.syuhao.ordersystem.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import tw.syuhao.ordersystem.entity.Product;
import tw.syuhao.ordersystem.service.ProductService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService service;

    @GetMapping("/")
    public String adminHome(Model model) {
        model.addAttribute("activePage", "admin");
        return "adminHome";
    }

    @GetMapping("/products")
    public String listProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Model model) {

        int pageSize = 10;

        Page<Product> productPage = service.findProducts(name, category, page, pageSize, minPrice, maxPrice);

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", productPage.getNumber() + 1); // 修正頁碼
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("name", name);
        model.addAttribute("category", category);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("activePage", "product"); // 補上 activePage
        return "adminProduct";
    }

    @GetMapping("/product/new")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping("/product/save")
    public String saveProduct(@ModelAttribute Product product,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        // 取得原商品（編輯時才有id）
        Product original = null;
        if (product.getId() != null) {
            original = service.findById(product.getId());
        }

        // 如果有上傳圖片
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            product.setImageUrl(fileName);
        } else if (original != null) {
            // 沒有上傳新圖片，保留原圖片
            product.setImageUrl(original.getImageUrl());
        }

        if (product.getEnabled() == true) {
            product.setStatus("上架");
        } else {
            product.setStatus("未上架");
        }

        service.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/product/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = service.findById(id);
        model.addAttribute("product", product);
        return "product-form";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        Product product = service.findById(id);
        if (product != null) {
            // 刪圖片
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Path imagePath = Paths.get(System.getProperty("user.dir"), "uploads", product.getImageUrl());
                System.out.println("Deleting image: " + imagePath.toAbsolutePath());

                try {
                    if (Files.exists(imagePath)) {
                        Files.delete(imagePath);
                        System.out.println("Image deleted.");
                    } else {
                        System.out.println("Image not found.");
                    }
                } catch (IOException e) {
                    System.err.println("Failed to delete image:");
                    e.printStackTrace();
                }
            }

            // 刪商品
            service.deleteById(id);
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/product/off/{id}")
    public String offProduct(@PathVariable Long id) {
        Product product = service.findById(id);
        if (product != null) {
            product.setEnabled(false);
            product.setStatus("下架");
            service.save(product);
        }
        return "redirect:/admin/products";
    }
}
