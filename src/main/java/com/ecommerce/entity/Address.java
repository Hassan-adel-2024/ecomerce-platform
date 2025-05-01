package com.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String fullAddress;
    private Long apartmentNumber;
    private String city;
    private String country;
    private boolean isDefault;
    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "userId")
    @JsonIgnore
    private AppUser user;
    @Override
    public String toString() {
        return "Address [id=" + addressId + ", full_address=" +
                fullAddress+" is default"+ isDefault;
    }


}
