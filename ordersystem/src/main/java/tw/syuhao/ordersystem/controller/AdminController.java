package tw.syuhao.ordersystem.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String adminHome() {
        return "admin-home";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        List<Product> products = service.getAllProducts();
        model.addAttribute("products", products);
        return "admin-product";
    }

    @GetMapping("/product/new")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping("/product/save")
    public String saveProduct(@ModelAttribute Product product,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        // 如果有上傳圖片
        if (!imageFile.isEmpty()) {

            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File saved to: " + filePath.toAbsolutePath());

            // 將圖片路徑存到 product，例如 "/uploads/abc.jpg"
            product.setImageUrl(fileName);
        }

        // 儲存商品
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

}
