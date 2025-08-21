package tw.syuhao.ordersystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.syuhao.ordersystem.entity.Inventory;
import tw.syuhao.ordersystem.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    public Inventory getInventory(@PathVariable Integer productId) {
        return inventoryService.getInventoryByProductId(productId);
    }

    @PostMapping("/{productId}/update")
    public Inventory updateStock(@PathVariable Integer productId, @RequestParam Integer quantity) {
        return inventoryService.updateStock(productId, quantity);
    }

    @PostMapping("/{productId}/increase")
    public void increaseStock(@PathVariable Integer productId, @RequestParam Integer amount) {
        inventoryService.increaseStock(productId, amount);
    }

    @PostMapping("/{productId}/decrease")
    public void decreaseStock(@PathVariable Integer productId, @RequestParam Integer amount) {
        inventoryService.decreaseStock(productId, amount);
    }
}
