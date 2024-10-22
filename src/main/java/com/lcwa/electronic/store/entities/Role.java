package com.lcwa.electronic.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    private String roleId;
    private String name;  // admin,normal as  a name lelunga


    // role fetch kru to user ni aaye
    // inverse side of the relationship
    @ManyToMany(mappedBy ="roles",fetch = FetchType.LAZY)
    private List<User> users=new ArrayList<>();

}
