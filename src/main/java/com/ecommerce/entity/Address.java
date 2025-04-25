package com.ecommerce.entity;

import jakarta.persistence.*;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String full_address;
    private Long apartmentNumber;
    private String city;
    private String country;
    private boolean isDefault;
    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "userId")
    private AppUser user;
    @Override
    public String toString() {
        return "Address [id=" + addressId + ", full_address=" +
                full_address+" is default"+ isDefault;
    }


}
