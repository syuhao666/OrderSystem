package tw.dd.spring.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;


@Controller
// @RestController ****用法1
public class OAuth2CallbackController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @PostConstruct
    public void init() {
        System.out.println("Google OAuth redirect URI is /oauth2/callback");
    }

    @GetMapping("/oauth2/callback")

    // public ResponseEntity<?> callback(@RequestParam String code)   ****用法1

    public RedirectView callback(@RequestParam String code, HttpSession session) throws Exception {
        // 1. 用授權碼換取 access token
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", "http://localhost:8080/oauth2/callback");
        form.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
            "https://oauth2.googleapis.com/token", request, Map.class);

        String accessToken = (String) response.getBody().get("access_token");

        // 2. 用 access token 取得使用者資料
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);

        HttpEntity<?> userInfoRequest = new HttpEntity<>(userInfoHeaders);
        ResponseEntity<Map> userInfo = restTemplate.exchange(
            "https://www.googleapis.com/oauth2/v3/userinfo",
            HttpMethod.GET,
            userInfoRequest,
            Map.class);

        // return ResponseEntity.ok(userInfo.getBody());   ****用法1


        session.setAttribute("user", userInfo.getBody());
        return new RedirectView("/");
    }
}