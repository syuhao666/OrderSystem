package tw.syuhao.ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

}
