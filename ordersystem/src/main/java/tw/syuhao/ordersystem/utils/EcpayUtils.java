package tw.syuhao.ordersystem.utils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EcpayUtils {

    public static String genCheckMacValue(Map<String, String> params, String hashKey, String hashIV) {
        // 1. 移除 CheckMacValue 參數
        Map<String, String> filtered = new HashMap<>(params);
        filtered.remove("CheckMacValue");

        // 2. 按照 Key 值排序（大小寫不分）
        List<String> keys = new ArrayList<>(filtered.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);

        // 3. 組字串
        StringBuilder sb = new StringBuilder();
        sb.append("HashKey=").append(hashKey);
        for (String key : keys) {
            sb.append("&").append(key).append("=").append(filtered.get(key));
        }
        sb.append("&HashIV=").append(hashIV);

        try {
            // 4. URL Encode 並轉成小寫
            String encoded = URLEncoder.encode(sb.toString(), "UTF-8")
                    .toLowerCase()
                    .replaceAll("%21", "!")
                    .replaceAll("%28", "(")
                    .replaceAll("%29", ")")
                    .replaceAll("%2a", "*")
                    .replaceAll("%2d", "-")
                    .replaceAll("%2e", ".")
                    .replaceAll("%5f", "_");

            // 5. MD5 雜湊轉大寫
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(encoded.getBytes("UTF-8"));

            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                String hexStr = Integer.toHexString(0xff & b);
                if (hexStr.length() == 1) {
                    hex.append("0");
                }
                hex.append(hexStr);
            }
            return hex.toString().toUpperCase();

        } catch (Exception e) {
            throw new RuntimeException("CheckMacValue 計算錯誤", e);
        }
    }
}
