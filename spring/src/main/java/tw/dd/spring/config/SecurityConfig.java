package tw.dd.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.openid.connect.sdk.claims.Address;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/","/cart.html" ,"/index.html","/login.html", "/static/**", "/js/**", "/css/**", "/images/**", "/oauth2/callback", "/products.html", "/products.json", "/favicon.ico","/api/checkout","/address.json","/form.html" ).permitAll()
                .anyRequest().authenticated()
            )
            // 拿掉 oauth2Login()，因為你是用 JS 手動 redirect
            .csrf().disable();

        return http.build();
    }
}