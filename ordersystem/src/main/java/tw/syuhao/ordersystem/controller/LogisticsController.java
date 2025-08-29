package tw.syuhao.ordersystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.syuhao.ordersystem.service.LogisticsService;

@RestController
@RequestMapping("/api/logistics")
public class LogisticsController {

    private final LogisticsService logisticsService;

    public LogisticsController(LogisticsService logisticsService) {
        this.logisticsService = logisticsService;
    }

    @GetMapping("/create")
    public String createLogisticsOrder() {
        return logisticsService.createOrder(
                "王小明",      // ReceiverName
                "0912345678", // ReceiverPhone
                "123456",     // ReceiverStoreID (測試超商店號)
                1000          // 金額
        );
    }
}