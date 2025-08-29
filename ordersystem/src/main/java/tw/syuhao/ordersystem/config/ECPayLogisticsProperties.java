package tw.syuhao.ordersystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "ecpay.logistics")
@Data
public class ECPayLogisticsProperties {
    private String merchantId;
    private String hashKey;
    private String hashIV;
    private String apiUrl;
    private String callbackUrl;
}
