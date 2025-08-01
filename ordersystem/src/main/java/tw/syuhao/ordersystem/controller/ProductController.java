package tw.syuhao.ordersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tw.syuhao.ordersystem.entity.Product;
import tw.syuhao.ordersystem.service.ProductService;

// @Controller
// @RequestMapping("/admin/products")
// public class ProductController {

//     @Autowired
//     private ProductService productService;

//     @PostMapping("/admin/products/add")
//     public String addProduct(@ModelAttribute Product product) {
//         productService.save(product);
//         return "redirect:/admin/products";
//     }
    
// }
