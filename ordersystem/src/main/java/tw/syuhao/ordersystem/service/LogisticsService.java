package tw.syuhao.ordersystem.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import tw.syuhao.ordersystem.config.ECPayLogisticsProperties;
import tw.syuhao.ordersystem.utils.ECPayUtils;

@Service
public class LogisticsService {
    private final ECPayLogisticsProperties props;

    public LogisticsService(ECPayLogisticsProperties props) {
        this.props = props;
    }

    public void test() {
        System.out.println("MerchantId = " + props.getMerchantId());
        System.out.println("API URL = " + props.getApiUrl());
    }

    public String createOrder(String receiverName, String receiverPhone, String storeId, int amount) {
        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", props.getMerchantId());
        params.put("MerchantTradeNo", "LGS" + System.currentTimeMillis());
        params.put("MerchantTradeDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        params.put("LogisticsType", "CVS");       // CVS = 超商取貨, Home = 宅配
        params.put("LogisticsSubType", "FAMI");   // FAMI=全家, UNIMART=7-11, TCAT=黑貓
        params.put("GoodsAmount", String.valueOf(amount));
        params.put("IsCollection", "N");          // 是否代收貨款 Y/N
        params.put("GoodsName", "家具商品");
        params.put("SenderName", "簡約家居");
        params.put("SenderCellPhone", "0911222333");
        params.put("ReceiverName", receiverName);
        params.put("ReceiverCellPhone", receiverPhone);
        params.put("ReceiverStoreID", storeId);   // 超商店號
        params.put("ServerReplyURL", props.getCallbackUrl());

        // 產生檢查碼
        String checkMac = ECPayUtils.generateCheckMacValue(params, props.getHashKey(), props.getHashIV());
        params.put("CheckMacValue", checkMac);

        // 使用 RestTemplate 呼叫 API
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.setAll(params);

        String response = restTemplate.postForObject(props.getApiUrl(), body, String.class);
        return response;
    }
}
