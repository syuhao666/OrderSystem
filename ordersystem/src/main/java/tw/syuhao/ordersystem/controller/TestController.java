package tw.syuhao.ordersystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.syuhao.ordersystem.service.LogisticsService;

@RestController
public class TestController {

    private final LogisticsService logisticsService;

    public TestController(LogisticsService logisticsService) {
        this.logisticsService = logisticsService;
    }

    @GetMapping("/test/logistics")
    public String testLogistics() {
        logisticsService.test();
        return "OK";
    }
}
