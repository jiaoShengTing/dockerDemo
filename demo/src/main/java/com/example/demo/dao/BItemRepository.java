package com.example.demo.dao;

import com.example.demo.model.SzydPutBItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BItemRepository extends JpaRepository<SzydPutBItem,String> {

}
