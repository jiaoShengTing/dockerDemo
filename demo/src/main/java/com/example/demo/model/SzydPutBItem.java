package com.example.demo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "szyd_put_bitem")
@Entity
public class SzydPutBItem implements Serializable {
    @Id
    private String id;

    private String companyId;


}
