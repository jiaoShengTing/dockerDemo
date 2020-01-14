package com.example.demo.dao;

import com.example.demo.model.SzydCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<SzydCompany,String> {

}
