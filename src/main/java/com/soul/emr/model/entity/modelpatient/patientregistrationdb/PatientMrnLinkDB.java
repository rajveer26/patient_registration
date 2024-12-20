package com.soul.emr.model.entity.modelpatient.patientregistrationdb;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false, exclude = {"patientDetailDB"})
@Entity
@Table(name = "EMR_TXN_PATIENT_MRN_LINKS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientMrnLinkDB extends WhoseColumnsEntity implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "emr_patient_mrn_link_info_seq")
    @SequenceGenerator(name = "emr_patient_mrn_link_info_seq", sequenceName = "emr_patient_mrn_link_info_seq", allocationSize = 1)
    @JsonProperty("patientMrnLinkId")
    @Column(name = "patient_mrn_link_id", length = 19)
    private Long patientMrnLinkId;

    @JsonProperty("familyMrn")
    @Column(name = "family_mrn")
    private String familyMrn;

    @JsonProperty("familyMrnRelation")
    @Column(name = "family_mrn_relation")
    private String familyMrnRelation;

    @JsonBackReference
    @JsonProperty("patientDetailDB")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_Id", referencedColumnName = "patient_Id")
    private PatientDetailsDB patientDetailDB;
}
