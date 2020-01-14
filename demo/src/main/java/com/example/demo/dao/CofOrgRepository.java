package com.example.demo.dao;

import com.example.demo.model.CofOrg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface CofOrgRepository extends JpaRepository<CofOrg, BigInteger> {
    public List<CofOrg> findAllByParentOrgCodeAndSysCode(String parentOrgCode,String sysCode);

    public List<CofOrg> findByParentOrgCodeIn(List<String> parentOrgCode);
}
