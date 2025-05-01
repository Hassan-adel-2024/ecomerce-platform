package com.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressDto {
    private String fullAddress;
    private Long apartmentNumber;
    private String city;
    private String country;
    @JsonProperty("isDefault")
    private boolean isDefault;
    private Long userId;

}
