package tw.syuhao.ordersystem.utils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ECPayUtils {
    public static String generateCheckMacValue(Map<String, String> params, String hashKey, String hashIV) {
        try {
            // 1. 排序參數
            List<String> sortedKeys = new ArrayList<>(params.keySet());
            Collections.sort(sortedKeys, String.CASE_INSENSITIVE_ORDER);

            StringBuilder sb = new StringBuilder();
            sb.append("HashKey=").append(hashKey);
            for (String key : sortedKeys) {
                sb.append("&").append(key).append("=").append(params.get(key));
            }
            sb.append("&HashIV=").append(hashIV);

            // 2. URL encode & 小寫
            String raw = URLEncoder.encode(sb.toString(), "UTF-8")
                                   .toLowerCase();

            // 3. MD5 / SHA256
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(raw.getBytes("UTF-8"));

            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                hex.append(String.format("%02X", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
