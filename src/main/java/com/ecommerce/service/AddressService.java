package com.ecommerce.service;

import com.ecommerce.dto.AddressDto;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.AppUser;
import com.ecommerce.exceptions.AddressLimitExceededException;
import com.ecommerce.exceptions.UserNotFoundException;
import com.ecommerce.mapper.AddressMapper;
import com.ecommerce.repository.AddressRepo;
import com.ecommerce.repository.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepo addressRepo;
    private final AddressMapper addressMapper;
    private final AppUserRepo appUserRepo;
    public Address addAddress(Long userId,AddressDto addressDto) {
        Long countAddresses = addressRepo.countByUserUserId(userId);
        if(countAddresses < 3){
            AppUser appUser = appUserRepo.findById(userId).orElseThrow(()-> new UserNotFoundException("user not found"));
            Address address = addressMapper.dtoToEntity(addressDto);
            System.out.println(addressDto.toString());
            System.out.println(address.toString());
            address.setUser(appUser);
            return addressRepo.save(address);
        }


        else {
            throw new AddressLimitExceededException("address limit exceeded");
        }
    }
    public List<Address> getAllAddressesByUserId(Long userId) {
        return addressRepo.findAllByUserUserId(userId);
    }
}
