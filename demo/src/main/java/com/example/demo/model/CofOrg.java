package com.example.demo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Data
@Table(name = "cof_org")
public class CofOrg implements Serializable {
    @Id
    private BigInteger orgId;
    private String code;
    private String parentOrgCode;
    private String name;
    private String sysCode;


}
