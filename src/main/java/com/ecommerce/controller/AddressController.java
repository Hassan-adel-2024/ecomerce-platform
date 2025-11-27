package com.ecommerce.controller;

import com.ecommerce.dto.AddressDto;
import com.ecommerce.entity.Address;
import com.ecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {
    private final AddressService addressService;
    @PostMapping("/add/{id}")
    public ResponseEntity<?> addAddress(@PathVariable("id") Long userId, @RequestBody AddressDto addressDto) {
        Address address = addressService.addAddress(addressDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAddress(@PathVariable("id") Long userId) {
        List<Address> addressList = addressService.getAllAddressesByUserId(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(addressList);
    }

    @GetMapping
    public ResponseEntity<?> getCurrentUserAddresses() {
        Long userId = com.ecommerce.utility.SecurityUtils.getAuthenticatedUserId();
        List<Address> addressList = addressService.getAllAddressesByUserId(userId);
        return ResponseEntity.ok(addressList);
    }

}
