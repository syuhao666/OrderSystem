package tw.syuhao.ordersystem.Ddto;

import lombok.Data;

@Data
public class ECPayLogisticsRequest {
    private String MerchantID;       // 商店代號
    private String MerchantTradeNo;  // 自訂交易編號
    private String MerchantTradeDate;
    private String LogisticsType;    // CVS / Home
    private String LogisticsSubType; // FAMI / UNIMART / TCAT
    private String GoodsAmount;
    private String CollectionAmount;
    private String IsCollection;     // Y/N
    private String GoodsName;
    private String SenderName;
    private String SenderCellPhone;
    private String ReceiverName;
    private String ReceiverCellPhone;
    private String ReceiverStoreID;  // 超商取貨才需要
    private String ServerReplyURL;   // 綠界通知用
    private String ClientReplyURL;   // 使用者回傳
    private String PlatformID;
    private String CheckMacValue;
}
