package com.soul.emr.model.entity.masterentity.masterdb;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "EMR_FND_ROLE_MASTER")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMasterDB extends WhoseColumnsEntity implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_role_master")
    @SequenceGenerator(name = "seq_role_master", sequenceName = "seq_role_master", allocationSize = 1)
    @Column(name = "role_master_id")
    private Long roleMasterId;

    @JsonProperty("roleMasterName")
    @Column(name = "role_master_name", length = 25)
    private String roleMasterName;

    @JsonProperty("roleMasterCode")
    @Column(name = "role_master_code", unique = true, nullable = false, length = 25)
    private String roleMasterCode;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "rolesMasterDB", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List <PrivilegesMasterDB> privileges = new ArrayList <>();
    

}
