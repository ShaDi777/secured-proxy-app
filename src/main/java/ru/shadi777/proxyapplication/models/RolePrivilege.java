package ru.shadi777.proxyapplication.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.shadi777.proxyapplication.enums.PrivilegeType;
import ru.shadi777.proxyapplication.enums.RoleType;

@Entity
@Data
@Table(name = "role_privilege")
public class RolePrivilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @JsonManagedReference
    private Role role;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "privilege_id", referencedColumnName = "id")
    @JsonManagedReference
    private Privilege privilege;

    public RolePrivilege() {
        super();
    }

    public RolePrivilege(Role role, Privilege privilege) {
        super();
        this.role = role;
        this.privilege = privilege;
    }

    public static String Authority(RoleType roleType, PrivilegeType privilegeType) {
        return roleType.name() + "_" + privilegeType.name();
    }
}
