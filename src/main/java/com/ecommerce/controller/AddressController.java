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
public class AddressController {
    private final AddressService addressService;
    @PostMapping("addAdress/{id}")
    public ResponseEntity<?> addAddress(@PathVariable("id") Long userId, @RequestBody AddressDto addressDto) {
        Address address = addressService.addAddress(userId, addressDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }
    @GetMapping("addresses/{id}")
    public ResponseEntity<?> getAddress(@PathVariable("id") Long userId) {
        List<Address> addressList = addressService.getAllAddressesByUserId(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(addressList);
    }
}
