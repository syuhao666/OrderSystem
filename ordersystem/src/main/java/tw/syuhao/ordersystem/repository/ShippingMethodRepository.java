package tw.syuhao.ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.ShippingMethod;

public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Long> {}
