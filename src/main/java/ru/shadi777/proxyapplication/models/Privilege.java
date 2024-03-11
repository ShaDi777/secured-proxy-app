package ru.shadi777.proxyapplication.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import lombok.Data;

import java.util.Collection;

@Entity
@Data
@Table(name = "privileges")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "privilege", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonBackReference
    private Collection<RolePrivilege> rolePrivilegeCollection;

    public Privilege() {
        super();
    }

    public Privilege(String name) {
        super();
        this.name = name;
    }
}