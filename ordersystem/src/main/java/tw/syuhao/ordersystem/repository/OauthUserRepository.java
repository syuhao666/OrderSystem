package tw.syuhao.ordersystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.OauthUser;

public interface OauthUserRepository extends JpaRepository<OauthUser, Long> {
    Optional<OauthUser> findByProviderUserId(String providerUserId);
    Optional<OauthUser> findByEmail(String email);
}