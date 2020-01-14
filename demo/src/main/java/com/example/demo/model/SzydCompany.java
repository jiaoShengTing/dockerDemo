package com.example.demo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "szyd_company")
@Data
public class SzydCompany implements Serializable {
    @Id
    private String id;
    private String orgCode;
    private String name;
    private String decisionFlag;
    private String itemFlag;

}
