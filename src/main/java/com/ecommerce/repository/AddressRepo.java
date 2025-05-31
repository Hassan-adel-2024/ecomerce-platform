package com.ecommerce.repository;

import com.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepo  extends JpaRepository<Address, Long> {
    Address findByUserUserId(Long userId);
    Long countByUserUserId(Long userId);
    List<Address> findAllByUserUserId(Long userId);



}
