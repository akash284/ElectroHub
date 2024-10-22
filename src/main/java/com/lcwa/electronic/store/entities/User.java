package com.lcwa.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity  // ye class ek entity bngayi mtlb iske corresponding ek table banjayegi database mein
@Table(name="users")
public class User   implements UserDetails {

    // IS USER K PASS KONSE KONSE ROLES HOGE YE BHI BTANA HOGA
    // ADMIN
    // NORMAL
    @Id
    private String userId;

    @Column(name="user_name")
    private String name;

    @Column(name="user_email",unique = true)
    private String email;

    @Column(name="user_password",length=500)
    private String password;


    private String gender;

    @Column(length = 1000)
    private String about;

    @Column(name="user_image_name")
    private String imageName;

    // USER REMOVE KARE TO USKE SARE ORDERS REMOVE HOJAYE
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Order> orders=new ArrayList<>();


    // user fetch karu to turant uska role ajaye
    // one user can have many roles like normal,admin
//
    // owning side of the relationship
    @ManyToMany(cascade = CascadeType.ALL,fetch =FetchType.EAGER)
    @JoinTable(
            name = "user_role", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key referencing User
            inverseJoinColumns = @JoinColumn(name = "role_id") // Foreign key referencing Role
    )
    private List<Role> roles=new ArrayList<>();



    // spring getAuthority ko use karta he ye dekhne k lie ki User ki kya kya authorities he
    // hum hrek role par traverse krege then use Authority mein convert krke return krdege

    // important for getting the roles :: ADMIN,NORMAL
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // ROLE --> AUTHORITEIS MEIN CONVERT KARDIA
        Set<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

        return authorities;
    }

    // important for getting the username
    @Override
    public String getUsername() {
        return this.getEmail();
    }

    // important
    @Override
    public String getPassword(){
        return this.password ;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


