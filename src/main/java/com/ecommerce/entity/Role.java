package com.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    private String roleName;
    private String roleDesc;
    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<AppUser> users;
    public Role(String roleName, String roleDesc) {
        this.roleName = roleName;
        this.roleDesc = roleDesc;
    }
    public Role(Long roleId,String roleName, String roleDesc) {
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.roleId = roleId;
    }
    @Override
    public String toString() {
        return "Role [roleId=" + roleId + ", roleName=" + roleName + ", roleDesc=";
    }



}
