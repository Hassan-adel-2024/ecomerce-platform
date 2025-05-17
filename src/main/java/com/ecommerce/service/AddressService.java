package com.ecommerce.service;

import com.ecommerce.dto.AddressDto;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.AppUser;
import com.ecommerce.exceptions.AddressLimitExceededException;
import com.ecommerce.exceptions.UserNotFoundException;
import com.ecommerce.mapper.AddressMapper;
import com.ecommerce.repository.AddressRepo;
import com.ecommerce.repository.AppUserRepo;
import com.ecommerce.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {
    private static final int MAX_ADDRESSES_PER_USER = 3;
    private final AddressRepo addressRepo;
    private final AddressMapper addressMapper;
    private final AppUserRepo appUserRepo;

    public Address addAddress(AddressDto addressDto) {
        Long userId = getAuthenticatedUserId();
        validateAddressLimit(userId);

        Address newAddress = addressMapper.dtoToEntity(addressDto);

        AppUser appUser = appUserRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        newAddress.setUser(appUser);

        List<Address> addressList = addressRepo.findAllByUserUserId(userId);
        boolean setAsDefaultAddress = newAddress.isDefault();

        handleDefaultAddressLogic(newAddress, addressList, setAsDefaultAddress);

        return addressRepo.save(newAddress);
    }

    public List<Address> getAllAddressesByUserId(Long userId) {
        return addressRepo.findAllByUserUserId(userId);
    }

    private void validateAddressLimit(Long userId) {
        Long countAddresses = addressRepo.countByUserUserId(userId);
        if (countAddresses >= MAX_ADDRESSES_PER_USER) {
            System.out.println("______________________address limit exceded");
            throw new AddressLimitExceededException("Address limit exceeded");
        }
    }

    public Long getAuthenticatedUserId() {
        return ((CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getUserId();
    }

    private void handleDefaultAddressLogic(Address newAddress, List<Address> addressList, boolean setAsDefaultAddress) {
        if (!setAsDefaultAddress && addressList.isEmpty()) {
            newAddress.setDefault(true);
        }

        if (setAsDefaultAddress) {
            for (Address existing : addressList) {
                if (existing.isDefault()) {
                    existing.setDefault(false);
                    addressRepo.save(existing);
                }
            }
        }
    }
}
