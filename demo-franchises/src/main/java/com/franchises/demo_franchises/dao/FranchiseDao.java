package com.franchises.demo_franchises.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.franchises.demo_franchises.entity.Franchise;

public interface FranchiseDao extends JpaRepository<Franchise, Long>{

}
