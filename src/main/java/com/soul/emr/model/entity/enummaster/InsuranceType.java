package com.soul.emr.model.entity.enummaster;

//public enum InsuranceType
//{
//    Self, TPA, Corporate, PED, Insurance
//}


import lombok.Getter;

@Getter
public enum InsuranceType {
    SELF(1, "Self"),
    TPA(2, "TPA"),
    CORPORATE(3, "Corporate"),
    PED(4, "PED"),
    INSURANCE(5, "Insurance");

    private final int id;
    private final String value;

    InsuranceType(int id, String value) {
        this.id = id;
        this.value = value;
    }

}