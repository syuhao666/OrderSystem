package tw.syuhao.ordersystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "oauth_users")
public class OauthUser {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;           // google / facebook ...
    private String providerUserId;     // Google 的 sub
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
    private String email;
    private Boolean emailVerified;
}
