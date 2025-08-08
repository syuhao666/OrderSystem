package tw.syuhao.ordersystem.Dcontroller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.syuhao.ordersystem.Dentity.OrderD;
import tw.syuhao.ordersystem.Drepository.OrderDRepository;
import tw.syuhao.ordersystem.utils.EcpayUtils;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PaymentCallbackController {

    @Value("${ecpay.hashKey}")
    private String hashKey;

    @Value("${ecpay.hashIV}")
    private String hashIV;

    @Autowired
    private OrderDRepository orderRepository;

    @PostMapping("/payment/notify")
    public String ecpayNotify(@RequestParam Map<String, String> params) {
        System.out.println("綠界回傳參數：" + params);

       

        // 使用你的工具類別計算 CheckMacValue，範例:
        String receivedCheckMacValue = params.get("CheckMacValue");
        String calculatedCheckMacValue = EcpayUtils.genCheckMacValue(params, hashKey, hashIV);

        if (!calculatedCheckMacValue.equalsIgnoreCase(receivedCheckMacValue)) {
            System.out.println("CheckMacValue 驗證失敗");
            return "0|CheckMacValue驗證失敗";
        }

        // Step 2: 判斷付款是否成功
        if ("1".equals(params.get("RtnCode"))) {
            String orderId = params.get("MerchantTradeNo");

            // 更新訂單狀態為已付款
            OrderD order = orderRepository.findById(Long.valueOf(orderId))
                    .orElse(null);

            if (order != null) {
                // order.setStatus("PAID"); // 假設有 status 欄位
                orderRepository.save(order);
                System.out.println("訂單 " + orderId + " 狀態更新為 PAID");
            } else {
                System.out.println("找不到訂單 ID: " + orderId);
            }
        } else {
            System.out.println("付款失敗，RtnCode: " + params.get("RtnCode"));
        }

        // Step 3: 按綠界要求，回傳成功訊息
        return "1|OK";
    }
}
