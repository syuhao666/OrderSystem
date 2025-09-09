package tw.syuhao.ordersystem.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import tw.syuhao.ordersystem.entity.OauthUser;
import tw.syuhao.ordersystem.repository.OauthUserRepository;

@Service
public class OauthUserService {

    private final OauthUserRepository repository;

    public OauthUserService(OauthUserRepository repository) {
        this.repository = repository;
    }

    public Optional<OauthUser> findByProviderUserId(String providerUserId) {
        return repository.findByProviderUserId(providerUserId);
    }
}