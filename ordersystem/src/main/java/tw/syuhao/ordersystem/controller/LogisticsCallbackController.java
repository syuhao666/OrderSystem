package tw.syuhao.ordersystem.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logistics")
public class LogisticsCallbackController {

    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam Map<String, String> payload) {
        // payload 會包含 AllPayLogisticsID, RtnCode, RtnMsg, LogisticsStatus 等等
        String tradeNo = payload.get("MerchantTradeNo");
        String logisticsId = payload.get("AllPayLogisticsID");
        String rtnCode = payload.get("RtnCode"); // 1=成功
        String logisticsStatus = payload.get("LogisticsStatus");

        // TODO: 更新 shipment 資料表狀態
        // ex: shipped, delivered

        return ResponseEntity.ok("1|OK"); // 綠界要求固定回傳
    }
}
