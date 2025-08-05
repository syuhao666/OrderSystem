package tw.syuhao.ordersystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.syuhao.ordersystem.entity.DProduct;
import tw.syuhao.ordersystem.repository.DProductRepository;


@RestController
@RequestMapping("/api")
public class DProductController {

    @Autowired
    private DProductRepository dproductRepository;

    @GetMapping("/products")
    public List<DProduct> getAllProducts() {
        return dproductRepository.findAll();
    }
    
}
